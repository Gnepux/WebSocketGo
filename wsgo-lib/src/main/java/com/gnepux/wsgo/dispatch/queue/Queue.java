package com.gnepux.wsgo.dispatch.queue;

/**
 * Basic operation of a queue
 *
 * @author gnepux
 */
public interface Queue<E> {

    /**
     * offer a message
     * @param e message
     */
    void offer(E e);

    /**
     * offer a message with a delay
     * @param e message
     * @param delay  delay time in milliseconds
     */
    void offer(E e, long delay);

    /**
     * poll a message
     * @throws InterruptedException when WsGo destroy
     * @return message
     */
    E poll() throws InterruptedException;

}
