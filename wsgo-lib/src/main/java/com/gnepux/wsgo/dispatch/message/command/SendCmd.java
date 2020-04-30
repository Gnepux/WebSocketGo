package com.gnepux.wsgo.dispatch.message.command;

public class SendCmd extends Command {

    private String text;

    public SendCmd(String text) {
        super(SEND);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
