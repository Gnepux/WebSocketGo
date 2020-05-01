package com.gnepux.wsgo.dispatch.dispatcher;

import com.gnepux.wsgo.dispatch.consumer.ConsumerThread;
import com.gnepux.wsgo.dispatch.message.Message;
import com.gnepux.wsgo.dispatch.producer.Producer;
import com.gnepux.wsgo.dispatch.producer.ProducerThread;
import com.gnepux.wsgo.dispatch.queue.MessageQueue;
import com.gnepux.wsgo.dispatch.resolver.Resolver;

/**
 * Dispatcher of the message queue.
 *
 * @author gnepux
 */
public class Dispatcher<E extends Message> implements Producer<E> {

    private Producer<E> producerProxy;

    private ProducerThread<E> producerThread;

    private ConsumerThread<E> consumerThread;

    /**
     * Start producer and consumer thread
     *
     * @param type     "event" or "command"
     * @param resolver Message resolver
     */
    public void loop(String type, Resolver<E> resolver) {
        MessageQueue<E> queue = new MessageQueue<>();
        producerThread = new ProducerThread<>("wsgo-" + type + "-producer", queue);
        consumerThread = new ConsumerThread<>("wsgo-" + type + "-consumer", queue, resolver);
        producerThread.start();
        consumerThread.start();

        setProxy(producerThread);
    }

    private void setProxy(Producer<E> producer) {
        this.producerProxy = producer;
    }

    /**
     * Stop the dispatcher
     */
    public void stop() {
        producerThread.shutdown();
        consumerThread.shutdown();
    }

    @Override
    public void sendMessage(E e) {
        producerProxy.sendMessage(e);
    }

    @Override
    public void sendMessageDelay(E message, long delay) {
        producerProxy.sendMessageDelay(message, delay);
    }
}
