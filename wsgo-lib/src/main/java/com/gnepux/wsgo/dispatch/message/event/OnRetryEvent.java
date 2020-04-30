package com.gnepux.wsgo.dispatch.message.event;

public class OnRetryEvent extends Event {

    private int retryCount;

    private long delayMillSec;

    public OnRetryEvent(int retryCount, long delayMillSec) {
        super(ON_RETRY);
        this.retryCount = retryCount;
        this.delayMillSec = delayMillSec;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public long getDelayMillSec() {
        return delayMillSec;
    }

    public void setDelayMillSec(long delayMillSec) {
        this.delayMillSec = delayMillSec;
    }
}
