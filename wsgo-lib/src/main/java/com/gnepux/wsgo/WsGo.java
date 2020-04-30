package com.gnepux.wsgo;

import com.gnepux.wsgo.constant.Constants;
import com.gnepux.wsgo.dispatch.dispatcher.CommandDispatcher;
import com.gnepux.wsgo.dispatch.dispatcher.EventDispatcher;
import com.gnepux.wsgo.dispatch.message.command.ChangePingCmd;
import com.gnepux.wsgo.dispatch.message.command.ConnectCmd;
import com.gnepux.wsgo.dispatch.message.command.DisconnectCmd;
import com.gnepux.wsgo.dispatch.message.command.SendCmd;

import java.util.concurrent.TimeUnit;

public class WsGo {

    private CommandDispatcher commandDispatcher;
    private EventDispatcher eventDispatcher;

    private WsConfig config;

    private WsGo(WsConfig config) {
        this.eventDispatcher = new EventDispatcher(config.mEventListener);
        this.commandDispatcher = new CommandDispatcher(eventDispatcher);
        this.config = config;
    }

    private static volatile WsGo sInstance;

    public synchronized static void init(WsConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("config can not be null");
        }

        if (sInstance == null) {
            sInstance = new WsGo(config);
        }
    }

    public static WsGo getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException("WsGo should be init first");
        }

        return sInstance;
    }

    public void connect() {
        if (config != null) {
            commandDispatcher.sendMessage(new ConnectCmd(config));
        }
    }

    public void disconnect(int code, String reason) {
        commandDispatcher.sendMessage(new DisconnectCmd(code, reason));
    }

    public void disconnectNormal(String reason) {
        disconnect(Constants.CODE_NORMAL_CLOSE, reason);
    }

    public void changePingInterval(long interval, TimeUnit unit) {
        commandDispatcher.sendMessage(new ChangePingCmd(interval, unit));
    }

    public void send(String text) {
        commandDispatcher.sendMessage(new SendCmd(text));
    }
}
