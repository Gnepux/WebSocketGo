package com.gnepux.wsgo.dispatch.dispatcher;

import com.gnepux.wsgo.dispatch.consumer.ConsumerThread;
import com.gnepux.wsgo.dispatch.message.Message;
import com.gnepux.wsgo.dispatch.producer.Producer;
import com.gnepux.wsgo.dispatch.producer.ProducerThread;
import com.gnepux.wsgo.dispatch.queue.MessageQueue;
import com.gnepux.wsgo.dispatch.resolver.Resolver;

public class Dispatcher<E extends Message> implements Producer<E> {

    private Producer<E> producerProxy;

    void loop(String type, Resolver<E> resolver) {
        MessageQueue<E> queue = new MessageQueue<>();
        ProducerThread<E> producerThread = new ProducerThread<>("wsgo-" + type + "-producer", queue);
        ConsumerThread<E> consumerThread = new ConsumerThread<>("wsgo-" + type + "-consumer", queue, resolver);
        producerThread.start();
        consumerThread.start();

        setProxy(producerThread);
    }

    private void setProxy(Producer<E> producer) {
        this.producerProxy = producer;
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
