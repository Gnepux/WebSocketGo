package com.gnepux.wsgo.dispatch.dispatcher;

import com.gnepux.wsgo.EventListener;
import com.gnepux.wsgo.dispatch.message.event.Event;
import com.gnepux.wsgo.dispatch.resolver.EventResolver;
import com.gnepux.wsgo.dispatch.resolver.Resolver;

public class EventDispatcher extends Dispatcher<Event> {

    public EventDispatcher(final EventListener listener) {
        Resolver<Event> resolver = new EventResolver(listener);
        loop("event", resolver);
    }

    public EventDispatcher(Resolver<Event> resolver) {
        loop("event", resolver);
    }

}
