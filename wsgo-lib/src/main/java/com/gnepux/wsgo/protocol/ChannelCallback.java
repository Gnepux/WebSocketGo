package com.gnepux.wsgo.protocol;

/**
 * WsGo Client callback. Custom WebSocket library should call the callback.
 *
 * @author gnepux
 */
public interface ChannelCallback {

    void onConnect(WebSocket webSocket);

    void onMessage(String text);

    void onClose(int code, String reason);

    void onDisconnect(Throwable t);

}
