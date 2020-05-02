package com.gnepux.wsgo;

/**
 * @author gnepux
 */
public interface EventListener {

    void onConnect();

    void onDisConnect(Throwable throwable);

    void onClose(int code, String reason);

    void onMessage(String text);

    void onReconnect(long retryCount, long delayMillSec);

    void onSend(String text, boolean success);

}
