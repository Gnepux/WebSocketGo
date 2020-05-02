package com.gnepux.wsgo.dispatch.message.event;

/**
 * @author gnepux
 */
public class OnDisConnectEvent extends Event {

    Throwable throwable;

    public OnDisConnectEvent(Throwable throwable) {
        super(ON_DISCONNECT);
        this.throwable = throwable;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
