package com.gnepux.wsgo.dispatch.message.command;

import com.gnepux.wsgo.dispatch.message.Message;

public class Command extends Message {

    private static final int BASE = 1;

    public static final int CONNECT = BASE;
    public static final int DISCONNECT = BASE + 1;
    public static final int RECONNECT = BASE + 2;
    public static final int CHANGE_PING = BASE + 3;
    public static final int SEND = BASE + 4;

    /**
     * Code of the Command
     */
    private int cmd;

    public Command(int code) {
        this.cmd = code;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }
}
