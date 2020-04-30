package com.gnepux.wsgo.dispatch.message.event;

public class OnMessageEvent extends Event {

    private String text;

    public OnMessageEvent(String text) {
        super(ON_MESSAGE);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
