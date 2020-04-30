package com.gnepux.wsgo.dispatch.resolver;

import android.util.Log;

import com.gnepux.wsgo.EventListener;
import com.gnepux.wsgo.dispatch.message.event.Event;
import com.gnepux.wsgo.dispatch.message.event.OnMessageEvent;
import com.gnepux.wsgo.dispatch.message.event.OnRetryEvent;
import com.gnepux.wsgo.dispatch.message.event.OnSendEvent;

public class EventResolver implements Resolver<Event> {

    private EventListener listener;

    public EventResolver(EventListener listener) {
        this.listener = listener;
    }

    @Override
    public void resolve(Event event) {
        switch (event.getEvent()) {
            case Event.ON_CONNECT:
                Log.e("xupeng", "event: onConnect");
                listener.onConnect();
                break;
            case Event.ON_DISCONNECT:
                Log.e("xupeng", "event: disconnect");
                listener.onDisConnect();
                break;
            case Event.ON_CLOSE:
                Log.e("xupeng", "event: close");
                listener.onClose();
                break;
            case Event.ON_MESSAGE:
                Log.e("xupeng", "event: message");
                listener.onMessage(((OnMessageEvent)event).getText());
                break;
            case Event.ON_RETRY:
                Log.e("xupeng", "event: retry");
                OnRetryEvent retryEvent = (OnRetryEvent) event;
                listener.onRetry(retryEvent.getRetryCount(), retryEvent.getDelayMillSec());
                break;
            case Event.ON_SEND:
                Log.e("xupeng", "event: onSend");
                listener.onSend(((OnSendEvent)event).isSuccess());
                break;
            default:
                break;
        }
    }
}
