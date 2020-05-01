package com.gnepux.wsgo;

/**
 * @author gnepux
 */
public interface EventListener {

    void onConnect();

    void onDisConnect();

    void onClose();

    void onMessage(String text);

    void onRetry(long retryCount, long delayMillSec);

    void onSend(boolean success);

}
