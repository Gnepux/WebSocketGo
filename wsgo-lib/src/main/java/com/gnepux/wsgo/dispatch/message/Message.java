package com.gnepux.wsgo.dispatch.message;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public abstract class Message implements Delayed {
    /**
     * delay time of the message in queue
     */
    private long delay;

    /**
     * the timestamp of message create
     */
    private long currentTime;

    {
        currentTime = System.currentTimeMillis();
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return delay - (System.currentTimeMillis() - currentTime);
    }

    @Override
    public int compareTo(Delayed o) {
        return Long.compare(this.getDelay(TimeUnit.MILLISECONDS), o.getDelay(TimeUnit.MILLISECONDS));
    }

}
