package com.gnepux.wsgo.dispatch.message.event;

/**
 * @author gnepux
 */
public class OnCloseEvent extends Event {

    private int code;

    private String reason;

    public OnCloseEvent(int code, String reason) {
        super(ON_CLOSE);
        this.code = code;
        this.reason = reason;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
