package com.gnepux.wsgo.dispatch.message.event;

import com.gnepux.wsgo.dispatch.message.Message;

/**
 * @author gnepux
 */
public class Event extends Message {

    private static final int BASE = 1;

    public static final int ON_CONNECT = BASE;
    public static final int ON_DISCONNECT = BASE + 1;
    public static final int ON_CLOSE = BASE + 3;
    public static final int ON_MESSAGE = BASE + 4;
    public static final int ON_RETRY = BASE + 5;
    public static final int ON_SEND = BASE + 6;

    private int event;

    public Event(int event) {
        this.event = event;
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }
}
