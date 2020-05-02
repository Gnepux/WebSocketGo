package com.gnepux.wsgo.jwebsocket;

import com.gnepux.wsgo.WsConfig;
import com.gnepux.wsgo.protocol.ChannelCallback;
import com.gnepux.wsgo.protocol.WebSocket;
import com.gnepux.wsgo.util.WsGoLog;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

public class JWebSocket implements WebSocket {

    private WebSocketClient client;

    @Override
    public void connect(WsConfig config, final ChannelCallback callback) {
        try {
            URI uri = new URI(config.url);
            client = new WebSocketClient(uri, new Draft_6455(), config.httpHeaders, (int) config.connectTimeout) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    callback.onConnect(JWebSocket.this);
                }

                @Override
                public void onMessage(String message) {
                    callback.onMessage(message);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    callback.onClose(code, reason);
                }

                @Override
                public void onError(Exception ex) {
                    callback.onDisconnect(ex);
                }

                @Override
                public void onWebsocketPong(org.java_websocket.WebSocket conn, Framedata f) {
                    super.onWebsocketPong(conn, f);
                    WsGoLog.d("onWebsocketPong");
                }
            };
            client.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reconnect(WsConfig config, final ChannelCallback callback) {
        if (client != null) {
            client.reconnect();
        }
    }

    @Override
    public boolean disconnect(int code, String reason) {
        if (client != null) {
            client.close(code, reason);
            return true;
        }
        return false;
    }

    @Override
    public void changePingInterval(long interval, TimeUnit unit) {
        if (client != null) {
            client.sendPing();
        }
    }

    @Override
    public boolean send(String msg) {
        if (client != null) {
            client.send(msg);
            return true;
        }
        return false;
    }

    public static class Factory implements WebSocket.Factory {

        @Override
        public WebSocket create() {
            return new JWebSocket();
        }
    }

    public static WebSocket create() {
        return new Factory().create();
    }
}
