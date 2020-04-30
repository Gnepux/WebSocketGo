package com.gnepux.wsgo.dispatch.message.command;

import java.util.concurrent.TimeUnit;

public class ChangePingCmd extends Command {

    private long time;

    private TimeUnit unit;

    public ChangePingCmd(long time, TimeUnit unit) {
        super(CHANGE_PING);
        this.time = time;
        this.unit = unit;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public void setUnit(TimeUnit unit) {
        this.unit = unit;
    }
}
