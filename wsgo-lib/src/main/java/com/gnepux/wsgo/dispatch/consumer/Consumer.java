package com.gnepux.wsgo.dispatch.consumer;

import com.gnepux.wsgo.dispatch.message.Message;

/**
 * Interface of the Consumer for the Queue
 */
public interface Consumer<E extends Message> {

    /**
     * get the message from the queue
     */
    void handleMessage(E e);

}
