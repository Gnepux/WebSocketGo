package com.gnepux.wsgo.dispatch.message.command;

/**
 * @author gnepux
 */
public class DisconnectCmd extends Command {

    private int code;

    private String reason;

    public DisconnectCmd(int code, String reason) {
        super(DISCONNECT);
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
