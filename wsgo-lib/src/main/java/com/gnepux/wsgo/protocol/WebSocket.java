package com.gnepux.wsgo.protocol;

import com.gnepux.wsgo.WsConfig;

import java.util.concurrent.TimeUnit;

/**
 * Interface of a WsGo Client. Custom WebSocket Client should implement the interface.
 *
 * @author gnepux
 */
public interface WebSocket {

    void connect(WsConfig config, ChannelCallback callback);

    void reconnect(WsConfig config, ChannelCallback callback);

    boolean disconnect(int code, String reason);

    void changePingInterval(long interval, TimeUnit unit);

    boolean send(String msg);

    interface Factory {
        WebSocket create();
    }

}
