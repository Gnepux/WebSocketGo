package com.gnepux.wsgo.dispatch.producer;

import com.gnepux.wsgo.dispatch.message.Message;
import com.gnepux.wsgo.dispatch.queue.MessageQueue;

/**
 * Producer thread.
 *
 * @author gnepux
 */
public class ProducerThread<E extends Message> extends Thread implements Producer<E> {

    private volatile boolean isAlive;

    private MessageQueue<E> queue;

    public ProducerThread(String name, MessageQueue<E> queue) {
        super(name);
        this.queue = queue;
        this.isAlive = true;
    }

    @Override
    public void run() {
        while (isAlive) {

        }
    }

    public void shutdown() {
        isAlive = false;
        interrupt();
    }

    @Override
    public void sendMessage(E e) {
        queue.offer(e);
    }

    @Override
    public void sendMessageDelay(E e, long delay) {
        queue.offer(e, delay);
    }
}
