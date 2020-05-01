package com.gnepux.wsgo.dispatch.consumer;

import com.gnepux.wsgo.dispatch.message.Message;

/**
 * Interface of the Consumer for the Queue.
 *
 * @author gnepux
 */
public interface Consumer<E extends Message> {

    /**
     * handle the message from the queue
     *
     * @param e message
     */
    void handleMessage(E e);

}
