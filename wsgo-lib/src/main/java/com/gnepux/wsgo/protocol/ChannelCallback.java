package com.gnepux.wsgo.protocol;

/**
 * WebSocket callbacks
 */
public interface ChannelCallback {

    void onConnect(WebSocket webSocket);

    void onMessage(String text);

    void onClose(int code, String reason);

    void onDisconnect(Throwable t);

}
