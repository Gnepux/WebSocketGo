package com.gnepux.wsgo.dispatch.queue;

/**
 * Basic operation of a queue
 *
 * @author gnepux
 */
public interface Queue<E> {

    /**
     * offer a message
     */
    void offer(E e);

    /**
     * offer a message with a delay
     */
    void offer(E message, long delay);

    /**
     * poll a message
     */
    E poll() throws InterruptedException;

}
