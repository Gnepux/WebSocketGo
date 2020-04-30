package com.gnepux.wsgo.dispatch.message.event;

public class OnSendEvent extends Event {

    private boolean success;

    public OnSendEvent(boolean success) {
        super(ON_SEND);
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
