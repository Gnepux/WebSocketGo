package com.gnepux.wsgo.dispatch.resolver;

import com.gnepux.wsgo.dispatch.message.Message;

public interface Resolver<E extends Message> {

    void resolve(E e);

}
