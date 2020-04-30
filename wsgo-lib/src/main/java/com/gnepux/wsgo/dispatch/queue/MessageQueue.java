package com.gnepux.wsgo.dispatch.queue;

import com.gnepux.wsgo.dispatch.message.Message;

import java.util.concurrent.DelayQueue;

/**
 * Interface of Message Queue
 */
public class MessageQueue<E extends Message> implements Queue<E> {

    private DelayQueue<E> queue = new DelayQueue<>();

    @Override
    public void offer(E e) {
        offer(e, 0);
    }

    @Override
    public void offer(E message, long delay) {
        message.setDelay(delay);
        queue.put(message);
    }

    @Override
    public E poll() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
