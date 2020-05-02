package com.gnepux.wsgo.protocol;

import com.gnepux.wsgo.WsConfig;
import com.gnepux.wsgo.constant.Constants;
import com.gnepux.wsgo.dispatch.dispatcher.Dispatcher;
import com.gnepux.wsgo.dispatch.message.event.Event;
import com.gnepux.wsgo.dispatch.message.event.OnCloseEvent;
import com.gnepux.wsgo.dispatch.message.event.OnConnectEvent;
import com.gnepux.wsgo.dispatch.message.event.OnDisConnectEvent;
import com.gnepux.wsgo.dispatch.message.event.OnMessageEvent;
import com.gnepux.wsgo.dispatch.message.event.OnRetryEvent;
import com.gnepux.wsgo.dispatch.message.event.OnSendEvent;
import com.gnepux.wsgo.util.WsGoLog;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Manage WebSocket channel's connect/disconnect/reconnect etc.
 *
 * @author gnepux
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

    private volatile STATE state = STATE.IDLE;

    private Dispatcher<Event> eventDispatcher;

    private volatile long retryCount = 0;

    private WsConfig mConfig;

    private CountDownLatch latch;

    private volatile long lastRetryTimeStamp;

    private volatile long lastDelayTime;

    public ChannelManager(Dispatcher<Event> eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    public void connect(WebSocket webSocket, WsConfig config, boolean reConnect) {
        WsGoLog.d("[connect] reConnect=[" + reConnect + "]");

        // channel is connected already
        if (state == STATE.CONNECTED) {
            return;
        }

        if (webSocket == null || config == null) {
            return;
        }

        latch = new CountDownLatch(1);

        state = STATE.CONNECTING;

        try {
            mWebSocket = webSocket;
            mConfig = config;
            if (reConnect) {
                webSocket.reconnect(config, this);
            } else {
                webSocket.connect(config, this);
            }
        } catch (Exception e) {
            // some exception due to the code error
            e.printStackTrace();
            state = STATE.IDLE;
        }

        try {
            latch.await(config.connectTimeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void reconnect(long retryCount) {
        WsGoLog.d("[reconnect] retryCount=[" + retryCount + "]");

        if (state == STATE.CONNECTED) {
            WsGoLog.d("[reconnect] WsGo has been connected. skip");
            return;
        }

        connect(mWebSocket, mConfig, true);
    }

    public void disconnect(int code, String reason) {
        WsGoLog.d("[disconnect] code=[" + code + "] reason=[" + reason + "]");

        if (mWebSocket != null) {
            mWebSocket.disconnect(code, reason);
            mWebSocket = null;
        }
    }

    public void changePingInterval(long time, TimeUnit unit) {
        WsGoLog.d("[changePingInterval] time=[" + time + "] unit=[" + unit.toString() + "]");

        if (mWebSocket != null) {
            mWebSocket.changePingInterval(time, unit);
        }
    }

    public void send(String text) {
        WsGoLog.d("[send] " + text);

        boolean success = false;

        if (mWebSocket != null) {
            success = mWebSocket.send(text);
        }

        eventDispatcher.sendMessage(new OnSendEvent(success, text));
    }

    @Override
    public void onConnect(WebSocket webSocket) {
        WsGoLog.d("[onConnect] connect success");
        eventDispatcher.sendMessage(new OnConnectEvent());

        state = STATE.CONNECTED;

        retryCount = 0;
        mWebSocket = webSocket;

        latch.countDown();
    }

    @Override
    public void onMessage(String text) {
        WsGoLog.d("[onMessage] " + text);
        eventDispatcher.sendMessage(new OnMessageEvent(text));
    }

    @Override
    public void onClose(int code, String reason) {
        WsGoLog.d("[onClose] code=[" + code + "] reason=[" + reason + "]");
        eventDispatcher.sendMessage(new OnCloseEvent(code, reason));

        state = STATE.IDLE;

        // Not normally close, need to reconnect
        if (code != Constants.CODE_NORMAL_CLOSE) {
            sendReconnectMsg();
        }

        latch.countDown();
    }

    /**
     * EOFException: maybe due to Server disconnect
     * SocketTimeoutException: maybe due to tcp connect exception
     * else: some other reasons
     */
    @Override
    public void onDisconnect(Throwable t) {
        WsGoLog.d("[onDisconnect] " + t.getMessage());

        eventDispatcher.sendMessage(new OnDisConnectEvent(t));

        state = STATE.IDLE;
        sendReconnectMsg();

        latch.countDown();
    }

    private synchronized void sendReconnectMsg() {
        // Skip frequent retry request.
        // Because some WebSocket libraries could call onClose or onDisconnect many times when disconnect,
        // it may cause 2^ times retry.
        // I DO NOT say he is Java WebSocket :)
        long retryTime = System.currentTimeMillis();
        if (retryTime - lastRetryTimeStamp < lastDelayTime) {
            WsGoLog.d("retry request too frequent, skip");
            return;
        }

        lastRetryTimeStamp = retryTime;
        lastDelayTime = mConfig.retryStrategy.onRetry(retryCount);

        WsGoLog.d(retryCount + " times reconnect try after " + lastDelayTime + "ms");

        Event onRetry = new OnRetryEvent(retryCount++, lastDelayTime);
        eventDispatcher.sendMessage(onRetry);
    }

}
