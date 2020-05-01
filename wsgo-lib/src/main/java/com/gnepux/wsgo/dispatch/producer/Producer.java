package com.gnepux.wsgo.dispatch.producer;

import com.gnepux.wsgo.dispatch.message.Message;

/**
 * Interface of the Producer for the Queue.
 *
 * @author gnepux
 */
public interface Producer<E extends Message> {

    /**
     * send message into the queue
     *
     * @param e Message
     */
    void sendMessage(E e);

    /**
     * send message into the queue with delay.
     * The delay time is MILLISECONDS for simple
     *
     * @param e     Message
     * @param delay delay time in milliseconds
     */
    void sendMessageDelay(E e, long delay);
}
