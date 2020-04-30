package com.gnepux.wsgo;

public abstract class EventListener {

    public void onConnect() {

    }

    public void onDisConnect() {

    }

    public void onClose() {

    }

    public void onMessage(String text) {

    }

    public void onRetry(int retryCount, long delayMillSec) {

    }

    public void onSend(boolean success) {

    }

}
