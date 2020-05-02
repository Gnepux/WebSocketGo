package com.gnepux.wsgo.dispatch.resolver;

import com.gnepux.wsgo.dispatch.message.Message;

/**
 * Interface of message resolver
 *
 * @author gnepux
 */
public interface Resolver<E extends Message> {

    /**
     * Resolve the message
     * @param e message
     */
    void resolve(E e);

}
