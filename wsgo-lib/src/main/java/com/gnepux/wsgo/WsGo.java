package com.gnepux.wsgo;

import com.gnepux.wsgo.dispatch.dispatcher.Dispatcher;
import com.gnepux.wsgo.dispatch.message.command.ChangePingCmd;
import com.gnepux.wsgo.dispatch.message.command.Command;
import com.gnepux.wsgo.dispatch.message.command.ConnectCmd;
import com.gnepux.wsgo.dispatch.message.command.DisconnectCmd;
import com.gnepux.wsgo.dispatch.message.command.SendCmd;
import com.gnepux.wsgo.dispatch.message.event.Event;
import com.gnepux.wsgo.dispatch.resolver.CommandResolver;
import com.gnepux.wsgo.dispatch.resolver.EventResolver;
import com.gnepux.wsgo.dispatch.resolver.Resolver;
import com.gnepux.wsgo.util.WsGoLog;

import java.util.concurrent.TimeUnit;

import static com.gnepux.wsgo.constant.Constants.CODE_NORMAL_CLOSE;
import static com.gnepux.wsgo.constant.Constants.CODE_VALID_MAX;
import static com.gnepux.wsgo.constant.Constants.CODE_VALID_MIN;

/**
 * @author gnepux
 */
public class WsGo {

    /**
     * The config of WsGo.
     */
    private WsConfig config;

    /**
     * The Dispatcher of command message queue.
     */
    private Dispatcher<Command> commandDispatcher;

    /**
     * The Dispatcher of event message queue.
     */
    private Dispatcher<Event> eventDispatcher;

    private WsGo(WsConfig config) {
        this.config = config;

        commandDispatcher = new Dispatcher<>();
        eventDispatcher = new Dispatcher<>();

        Resolver<Command> commandResolver = new CommandResolver(eventDispatcher);
        Resolver<Event> eventResolver = new EventResolver(config.eventListener, commandDispatcher);

        commandDispatcher.loop("command", commandResolver);
        eventDispatcher.loop("event", eventResolver);

        WsGoLog.d("WsGo init success");
    }

    private static volatile WsGo sInstance;

    public synchronized static void init(WsConfig config) {
        if (config == null) {
            WsGoLog.d("WsGo init failed: config can not be null");
            return;
        }

        if (sInstance == null) {
            sInstance = new WsGo(config);
        }
    }

    /**
     * Get the instance of WsGo
     */
    public static WsGo getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException("WsGo must call init() first.");
        }

        return sInstance;
    }

    /**
     * Destroy the instance of WsGo.
     * Need to call {@link #init(WsConfig)} if want to use WsGo again.
     */
    public void destroyInstance() {
        commandDispatcher.stop();
        eventDispatcher.stop();

        sInstance = null;

        WsGoLog.d("WsGo destroy");
    }

    /**
     * Connect.
     */
    public void connect() {
        if (config != null) {
            commandDispatcher.sendMessage(new ConnectCmd(config));
        }
    }

    /**
     * Disconnect the WebSocket connection.
     */
    public void disconnect(int code, String reason) {
        if (code < CODE_VALID_MIN || code > CODE_VALID_MAX) {
            return;
        }
        commandDispatcher.sendMessage(new DisconnectCmd(code, reason));
    }

    /**
     * Disconnect with a normal code.
     */
    public void disconnectNormal(String reason) {
        disconnect(CODE_NORMAL_CLOSE, reason);
    }

    /**
     * Change ping interval.
     */
    public void changePingInterval(long interval, TimeUnit unit) {
        commandDispatcher.sendMessage(new ChangePingCmd(interval, unit));
    }

    /**
     * Send a text.
     */
    public void send(String text) {
        commandDispatcher.sendMessage(new SendCmd(text));
    }
}
