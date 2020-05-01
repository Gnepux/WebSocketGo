package com.gnepux.wsgo.dispatch.message.event;

/**
 * @author gnepux
 */
public class OnSendEvent extends Event {

    private String text;

    private boolean success;

    public OnSendEvent(boolean success, String text) {
        super(ON_SEND);
        this.success = success;
        this.text = text;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
