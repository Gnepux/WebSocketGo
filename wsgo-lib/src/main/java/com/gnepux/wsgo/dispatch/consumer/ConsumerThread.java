package com.gnepux.wsgo.dispatch.consumer;

import com.gnepux.wsgo.dispatch.message.Message;
import com.gnepux.wsgo.dispatch.queue.MessageQueue;
import com.gnepux.wsgo.dispatch.resolver.Resolver;

/**
 * Implementation of Consumer Thread
 */
public class ConsumerThread<E extends Message> extends Thread implements Consumer<E> {

    private Resolver<E> resolver;

    private volatile boolean isAlive;

    private MessageQueue<E> queue;

    public ConsumerThread(String name, MessageQueue<E> queue, Resolver<E> resolver) {
        super(name);
        this.queue = queue;
        this.isAlive = true;
        this.resolver = resolver;
    }

    @Override
    public void run() {
        while (isAlive) {
            E e = queue.poll();
            handleMessage(e);
        }
    }

    public void shutdown() {
        isAlive = false;
    }

    @Override
    public void handleMessage(E e) {
        resolver.resolve(e);
    }
}
