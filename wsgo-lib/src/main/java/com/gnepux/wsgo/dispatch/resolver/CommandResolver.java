package com.gnepux.wsgo.dispatch.resolver;

import com.gnepux.wsgo.dispatch.dispatcher.Dispatcher;
import com.gnepux.wsgo.dispatch.message.command.ChangePingCmd;
import com.gnepux.wsgo.dispatch.message.command.Command;
import com.gnepux.wsgo.dispatch.message.command.ConnectCmd;
import com.gnepux.wsgo.dispatch.message.command.DisconnectCmd;
import com.gnepux.wsgo.dispatch.message.command.ReconnectCmd;
import com.gnepux.wsgo.dispatch.message.command.SendCmd;
import com.gnepux.wsgo.dispatch.message.event.Event;
import com.gnepux.wsgo.protocol.ChannelManager;

import static com.gnepux.wsgo.dispatch.message.command.Command.CHANGE_PING;
import static com.gnepux.wsgo.dispatch.message.command.Command.CONNECT;
import static com.gnepux.wsgo.dispatch.message.command.Command.DISCONNECT;
import static com.gnepux.wsgo.dispatch.message.command.Command.RECONNECT;
import static com.gnepux.wsgo.dispatch.message.command.Command.SEND;

public class CommandResolver implements Resolver<Command> {

    ChannelManager mChannelManager;

    public CommandResolver(Dispatcher<Command> commandDispatcher, Dispatcher<Event> eventDispatcher) {
        mChannelManager = new ChannelManager(commandDispatcher, eventDispatcher);
    }

    @Override
    public void resolve(Command command) {
        int cmd = command.getCmd();
        switch (cmd) {
            case CONNECT:
                handleConnect((ConnectCmd) command);
                break;
            case RECONNECT:
                handleReconnect((ReconnectCmd) command);
                break;
            case DISCONNECT:
                handleDisconnect((DisconnectCmd) command);
                break;
            case CHANGE_PING:
                handleChangePing((ChangePingCmd) command);
                break;
            case SEND:
                handleSend((SendCmd) command);
            default:
                break;
        }
    }

    void handleConnect(ConnectCmd command) {
        mChannelManager.connect(command.getConfig().mWebSocket, command.getConfig());
    }

    void handleReconnect(ReconnectCmd command) {
        mChannelManager.reconnect();
    }

    void handleDisconnect(DisconnectCmd command) {
        mChannelManager.disconnect(command.getCode(), command.getReason());
    }

    void handleChangePing(ChangePingCmd command) {
        mChannelManager.changePingInterval(command.getTime(), command.getUnit());
    }

    void handleSend(SendCmd command) {
        mChannelManager.send(command.getText());
    }
}
