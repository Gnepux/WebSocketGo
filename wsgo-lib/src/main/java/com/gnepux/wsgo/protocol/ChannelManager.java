package com.gnepux.wsgo.protocol;

import android.util.Log;

import com.gnepux.wsgo.WsConfig;
import com.gnepux.wsgo.constant.Constants;
import com.gnepux.wsgo.dispatch.dispatcher.Dispatcher;
import com.gnepux.wsgo.dispatch.message.command.Command;
import com.gnepux.wsgo.dispatch.message.command.ReconnectCmd;
import com.gnepux.wsgo.dispatch.message.event.Event;
import com.gnepux.wsgo.dispatch.message.event.OnCloseEvent;
import com.gnepux.wsgo.dispatch.message.event.OnConnectEvent;
import com.gnepux.wsgo.dispatch.message.event.OnDisConnectEvent;
import com.gnepux.wsgo.dispatch.message.event.OnMessageEvent;
import com.gnepux.wsgo.dispatch.message.event.OnRetryEvent;
import com.gnepux.wsgo.dispatch.message.event.OnSendEvent;
import com.gnepux.wsgo.retry.DefaultRetryStategy;
import com.gnepux.wsgo.retry.RetryStrategy;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Manage WebSocket channel's connect、disconnect and reconnect etc
 */
public class ChannelManager implements ChannelCallback {

    /**
     * channel state
     */
    public enum STATE {
        IDLE,
        CONNECTING,
        CONNECTED
    }

    private WebSocket mWebSocket;

    private STATE state;

    private Dispatcher<Command> commandDispatcher;

    private Dispatcher<Event> eventDispatcher;

    private int retryCount = 0;

    private WsConfig mConfig;

    private CountDownLatch mLatch;

    public ChannelManager(Dispatcher<Command> commandDispatcher, Dispatcher<Event> eventDispatcher) {
        this.commandDispatcher = commandDispatcher;
        this.eventDispatcher = eventDispatcher;
    }

    public void connect(WebSocket webSocket, WsConfig config) {
        // channel is connected already
        if (state == STATE.CONNECTED) {
            return;
        }

        if (webSocket == null || config == null) {
            return;
        }

        mLatch = new CountDownLatch(1);

        state = STATE.CONNECTING;

        try {
            mWebSocket = webSocket;
            mConfig = config;
            webSocket.connect(config, this);
        } catch (Exception e) {
            // some exception due to the code error
            e.printStackTrace();
            state = STATE.IDLE;
        }

        try {
            mLatch.await(config.mConnectTimeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void reconnect() {
        if (state == STATE.CONNECTED) {
            return;
        }
        connect(mWebSocket, mConfig);
    }

    public void disconnect(int code, String reason) {
        if (mWebSocket != null) {
            mWebSocket.disconnect(code, reason);
            mWebSocket = null;
        }
    }

    public void changePingInterval(long time, TimeUnit unit) {
        if (mWebSocket != null) {
            mWebSocket.changePingInterval(time, unit);
        }
    }

    public void send(String text) {
        boolean success = false;

        if (mWebSocket != null) {
            success = mWebSocket.send(text);
        }

        eventDispatcher.sendMessage(new OnSendEvent(success));
    }

    @Override
    public void onConnect(WebSocket webSocket) {
        Log.e("xupeng", "[onConnect]");
        eventDispatcher.sendMessage(new OnConnectEvent());

        state = STATE.CONNECTED;

        retryCount = 0;
        mWebSocket = webSocket;

        mLatch.countDown();
    }

    @Override
    public void onMessage(String text) {
        Log.e("xupeng", "[onMessage]" + text);
        eventDispatcher.sendMessage(new OnMessageEvent(text));
    }

    @Override
    public void onClose(int code, String reason) {
        Log.e("xupeng", "[onClose]" + code + ":" + reason);
        eventDispatcher.sendMessage(new OnCloseEvent());

        state = STATE.IDLE;

        // Not normally close, need to reconnect
        if (code != Constants.CODE_NORMAL_CLOSE) {
            sendReconnectMsg(++retryCount);
        }

        mLatch.countDown();
    }

    /**
     * EOFException: maybe due to Server disconnect
     * SocketTimeoutException: maybe due to tcp connect exception
     * else: some other reasons
     */
    @Override
    public void onDisconnect(Throwable t) {
        Log.e("xupeng", "[onDisconnect]");
        t.printStackTrace();
        eventDispatcher.sendMessage(new OnDisConnectEvent());

        state = STATE.IDLE;
        sendReconnectMsg(++retryCount);

        mLatch.countDown();
    }

    private void sendReconnectMsg(int retryCount) {
        RetryStrategy retryStrategy = mConfig.mRetryStrategy;
        if (retryStrategy == null) {
            retryStrategy = new DefaultRetryStategy();
        }

        long delay = retryStrategy.onRetry(retryCount);
        String retryInfo = "retry after " + delay + "ms";
        Log.e("xupeng", retryInfo);
        eventDispatcher.sendMessage(new OnRetryEvent(retryCount, delay));

        commandDispatcher.sendMessageDelay(new ReconnectCmd(), delay);
    }

}
