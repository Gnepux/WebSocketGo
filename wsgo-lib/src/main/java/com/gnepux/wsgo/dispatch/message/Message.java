package com.gnepux.wsgo.dispatch.message;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * WsGo Message
 *
 * @author gnepux
 */
public abstract class Message implements Delayed {

    /**
     * The priority of the Message. The high priority message will be put before normal message in the queue.
     * true - high
     * false(default) - normal
     */
    private boolean highPriority;

    /**
     * The Delay time of the message in queue.
     */
    private long delay;

    /**
     * The timestamp of message create.
     */
    private long currentTime;

    {
        currentTime = System.currentTimeMillis();
        highPriority = false;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public void highPriority(boolean highPriority) {
        this.highPriority = highPriority;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return delay - (System.currentTimeMillis() - currentTime);
    }

    @Override
    public int compareTo(Delayed o) {
        if (o instanceof Message) {
            if (highPriority && !((Message) o).highPriority) {
                return -1;
            } else if (!highPriority && ((Message) o).highPriority) {
                return 1;
            }
        }
        return Long.compare(this.getDelay(TimeUnit.MILLISECONDS), o.getDelay(TimeUnit.MILLISECONDS));
    }

}
