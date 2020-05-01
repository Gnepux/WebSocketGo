package com.gnepux.wsgo.dispatch.message.event;

/**
 * @author gnepux
 */
public class OnRetryEvent extends Event {

    private long retryCount;

    private long delayMillSec;

    public OnRetryEvent(long retryCount, long delayMillSec) {
        super(ON_RETRY);
        this.retryCount = retryCount;
        this.delayMillSec = delayMillSec;
    }

    public long getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(long retryCount) {
        this.retryCount = retryCount;
    }

    public long getDelayMillSec() {
        return delayMillSec;
    }

    public void setDelayMillSec(long delayMillSec) {
        this.delayMillSec = delayMillSec;
    }
}
