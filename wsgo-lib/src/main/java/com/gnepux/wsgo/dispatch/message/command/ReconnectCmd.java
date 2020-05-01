package com.gnepux.wsgo.dispatch.message.command;

/**
 * @author gnepux
 */
public class ReconnectCmd extends Command {

    private long retryCount;

    public ReconnectCmd(long retryCount) {
        super(RECONNECT);
        this.retryCount = retryCount;
    }

    public long getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(long retryCount) {
        this.retryCount = retryCount;
    }
}
