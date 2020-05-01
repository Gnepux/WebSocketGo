package com.gnepux.wsgo.protocol.androidAsync;

import com.gnepux.wsgo.WsConfig;
import com.gnepux.wsgo.protocol.ChannelCallback;
import com.gnepux.wsgo.protocol.WebSocket;

import java.util.concurrent.TimeUnit;

public class AAWebSocket implements WebSocket {

    public AAWebSocket() {

    }

    @Override
    public void connect(WsConfig config, ChannelCallback callback) {

    }

    @Override
    public void reconnect(WsConfig config, ChannelCallback callback) {

    }

    @Override
    public boolean disconnect(int code, String reason) {
        return false;
    }

    @Override
    public void changePingInterval(long interval, TimeUnit unit) {

    }

    @Override
    public boolean send(String msg) {
        return false;
    }

    public static class Factory implements WebSocket.Factory {

        @Override
        public WebSocket create() {
            return new AAWebSocket();
        }
    }
}
