package com.gnepux.wsgo.dispatch.message.command;

import com.gnepux.wsgo.WsConfig;

/**
 * @author gnepux
 */
public class ConnectCmd extends Command {

    private WsConfig config;

    public ConnectCmd(WsConfig config) {
        super(CONNECT);
        this.config = config;
    }

    public WsConfig getConfig() {
        return config;
    }

    public void setConfig(WsConfig config) {
        this.config = config;
    }
}
