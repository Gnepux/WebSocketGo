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

/**
 * Resolver for command type message.
 *
 * @author gnepux
 */
public class CommandResolver implements Resolver<Command> {

    private ChannelManager mChannelManager;

    public CommandResolver(Dispatcher<Event> eventDispatcher) {
        mChannelManager = new ChannelManager(eventDispatcher);
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

    private void handleConnect(ConnectCmd command) {
        mChannelManager.connect(command.getConfig().websocket, command.getConfig(), false);
    }

    private void handleReconnect(ReconnectCmd command) {
        mChannelManager.reconnect(command.getRetryCount());
    }

    private void handleDisconnect(DisconnectCmd command) {
        mChannelManager.disconnect(command.getCode(), command.getReason());
    }

    private void handleChangePing(ChangePingCmd command) {
        mChannelManager.changePingInterval(command.getTime(), command.getUnit());
    }

    private void handleSend(SendCmd command) {
        mChannelManager.send(command.getText());
    }
}
