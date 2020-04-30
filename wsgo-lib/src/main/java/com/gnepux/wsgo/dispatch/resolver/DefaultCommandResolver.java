package com.gnepux.wsgo.dispatch.resolver;

import android.util.Log;

import com.gnepux.wsgo.dispatch.dispatcher.Dispatcher;
import com.gnepux.wsgo.dispatch.message.command.ChangePingCmd;
import com.gnepux.wsgo.dispatch.message.command.Command;
import com.gnepux.wsgo.dispatch.message.command.ConnectCmd;
import com.gnepux.wsgo.dispatch.message.command.DisconnectCmd;
import com.gnepux.wsgo.dispatch.message.command.ReconnectCmd;
import com.gnepux.wsgo.dispatch.message.command.SendCmd;
import com.gnepux.wsgo.dispatch.message.event.Event;

public class DefaultCommandResolver extends CommandResolver {

    public DefaultCommandResolver(Dispatcher<Command> commandDispatcher, Dispatcher<Event> eventDispatcher) {
        super(commandDispatcher, eventDispatcher);
    }

    @Override
    void handleConnect(ConnectCmd command) {
        Log.d("xupeng", "[connect]");
        mChannelManager.connect(command.getConfig().mWebSocket, command.getConfig());
    }

    @Override
    void handleReconnect(ReconnectCmd command) {
        Log.d("xupeng", "[reconnect]");
        mChannelManager.reconnect();
    }

    @Override
    void handleDisconnect(DisconnectCmd command) {
        Log.d("xupeng", "[disconnect]" + command.getReason());
        mChannelManager.disconnect(command.getCode(), command.getReason());
    }

    @Override
    void handleChangePing(ChangePingCmd command) {
        Log.d("xupeng", "[change ping]" + command.getTime());
        mChannelManager.changePingInterval(command.getTime(), command.getUnit());
    }

    @Override
    void handleSend(SendCmd command) {
        Log.d("xupeng", "[send]" + command.getText());
        mChannelManager.send(command.getText());
    }
}
