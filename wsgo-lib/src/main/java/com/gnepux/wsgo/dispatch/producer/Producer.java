package com.gnepux.wsgo.dispatch.producer;

import com.gnepux.wsgo.dispatch.message.Message;

/**
 * Interface of the Producer for the Queue
 */
public interface Producer<E extends Message> {

    /**
     * send message into the queue
     */
    void sendMessage(E e);

    /**
     * send message into the queue with delay.
     * The delay time is MILLISECONDS for simple
     */
    void sendMessageDelay(E message, long delay);
}
