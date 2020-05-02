package com.gnepux.wsgo.okwebsocket;

import com.gnepux.wsgo.WsConfig;
import com.gnepux.wsgo.protocol.ChannelCallback;
import com.gnepux.wsgo.protocol.WebSocket;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocketListener;
import okhttp3.internal.Util;
import okhttp3.internal.ws.RealWebSocket;

public class OkWebSocket implements WebSocket {

    private okhttp3.WebSocket mWebSocket;

    private OkWebSocket() {

    }

    @Override
    public void connect(WsConfig config, final ChannelCallback callback) {
        Request request = OkUtils.generateRequest(config.httpHeaders, config.url);
        OkHttpClient client = OkUtils.generateClient(config.pingInterval, config.connectTimeout,
                config.readTimeout, config.writeTimeout);

        if (client == null) {
            return;
        }

        client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(okhttp3.WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                mWebSocket = webSocket;
                callback.onConnect(OkWebSocket.this);
            }

            @Override
            public void onMessage(okhttp3.WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                callback.onMessage(text);
            }

            @Override
            public void onClosed(okhttp3.WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                callback.onClose(code, reason);
            }

            @Override
            public void onFailure(okhttp3.WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                callback.onDisconnect(t);
            }
        });
    }

    @Override
    public void reconnect(WsConfig config, final ChannelCallback callback) {
        connect(config, callback);
    }

    @Override
    public boolean disconnect(int code, String reason) {
        return mWebSocket != null && mWebSocket.close(code, reason);
    }

    /**
     * OkHttp does not support change ping interval from outer, here use reflect to change ping interval
     * and create a new ping runnable, or we must establish a new connect if we want to change ping interval. -_-!
     */
    @Override
    public void changePingInterval(long interval, TimeUnit unit) {
        if (mWebSocket != null) {
            Class clazz;
            try {
                clazz = Class.forName("okhttp3.internal.ws.RealWebSocket");
                Field field = clazz.getDeclaredField("executor");
                field.setAccessible(true);
                ScheduledExecutorService oldService = (ScheduledExecutorService) field.get(mWebSocket);

                Class[] innerClasses = Class.forName("okhttp3.internal.ws.RealWebSocket").getDeclaredClasses();
                for (Class innerClass : innerClasses) {
                    if ("PingRunnable".equals(innerClass.getSimpleName())) {
                        // create a new ping runnable
                        Constructor constructor = innerClass.getDeclaredConstructor(RealWebSocket.class);
                        constructor.setAccessible(true);
                        Object pingRunnable = constructor.newInstance(mWebSocket);

                        // create new scheduled thread and set to okhttp
                        ScheduledThreadPoolExecutor newService = new ScheduledThreadPoolExecutor(1, Util.threadFactory("ws-ping", false));
                        newService.scheduleAtFixedRate((Runnable) pingRunnable, interval, interval, unit);
                        field.set(mWebSocket, newService);

                        // shut down old service
                        oldService.shutdown();
                    }
                }
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                    NoSuchFieldException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean send(String msg) {
        if (mWebSocket != null) {
            return mWebSocket.send(msg);
        }
        return false;
    }

    public static class Factory implements WebSocket.Factory {

        @Override
        public WebSocket create() {
            return new OkWebSocket();
        }
    }

    public static WebSocket create() {
        return new Factory().create();
    }

}
