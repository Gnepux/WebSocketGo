package com.gnepux.wsgo.dispatch.dispatcher;

import com.gnepux.wsgo.dispatch.message.command.Command;
import com.gnepux.wsgo.dispatch.message.event.Event;
import com.gnepux.wsgo.dispatch.resolver.CommandResolver;
import com.gnepux.wsgo.dispatch.resolver.Resolver;

public class CommandDispatcher extends Dispatcher<Command> {

    public CommandDispatcher(Dispatcher<Event> eventDispatcher) {
        Resolver<Command> resolver = new CommandResolver(this, eventDispatcher);
        loop("command", resolver);
    }

    public void loop(Resolver<Command> resolver) {
        loop("command", resolver);
    }
}
