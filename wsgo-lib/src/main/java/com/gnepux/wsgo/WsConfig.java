package com.gnepux.wsgo;

import com.gnepux.wsgo.protocol.WebSocket;
import com.gnepux.wsgo.retry.DefaultRetryStrategy;
import com.gnepux.wsgo.retry.RetryStrategy;
import com.gnepux.wsgo.util.WsGoLog;

import java.util.HashMap;

import static com.gnepux.wsgo.constant.Constants.DEFAULT_CONNECT_TIMEOUT;
import static com.gnepux.wsgo.constant.Constants.DEFAULT_PING_INTERVAL;
import static com.gnepux.wsgo.constant.Constants.DEFAULT_READ_TIMEOUT;
import static com.gnepux.wsgo.constant.Constants.DEFAULT_WRITE_TIMEOUT;

/**
 * WsGo config
 *
 * @author gnepux
 */
public class WsConfig {

    /**
     * WebSocket url
     */
    public String url;

    /**
     * Http headers
     */
    public HashMap<String, String> httpHeaders;

    /**
     * The WebSocket client that WsGo used, must implement {@link WebSocket} interface
     */
    public WebSocket websocket;

    /**
     * The ping interval of WebSocket connection
     */
    public long pingInterval;

    /**
     * The connect timeout of WebSocket handshake
     */
    public long connectTimeout;

    /**
     * The read timeout of WebSocket connection
     */
    public long readTimeout;

    /**
     * The write timeout of WebSocket connection. (not must use in some WebSocket client)
     */
    public long writeTimeout;

    /**
     * Retry strategy when reconnection
     */
    public RetryStrategy retryStrategy;

    /**
     * Event listener of {@link WebSocket}
     */
    public EventListener eventListener;

    private WsConfig(Builder builder) {
        this.url = builder.url;
        this.httpHeaders = builder.httpHeaders;
        this.websocket = builder.websocket;
        this.pingInterval = builder.pingInterval;
        this.connectTimeout = builder.connectTimeout;
        this.readTimeout = builder.readTimeout;
        this.retryStrategy = builder.retryStrategy;
        this.eventListener = builder.eventListener;

        WsGoLog.DEBUG = builder.debug;
    }

    public static class Builder {

        String url;

        HashMap<String, String> httpHeaders;

        WebSocket websocket;

        long pingInterval = DEFAULT_PING_INTERVAL;

        long connectTimeout = DEFAULT_CONNECT_TIMEOUT;

        long readTimeout = DEFAULT_READ_TIMEOUT;

        long writeTimeout = DEFAULT_WRITE_TIMEOUT;

        RetryStrategy retryStrategy;

        EventListener eventListener;

        boolean debug = true;

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setHttpHeaders(HashMap<String, String> headers) {
            this.httpHeaders = headers;
            return this;
        }

        public Builder setWebSocket(WebSocket webSocket) {
            this.websocket = webSocket;
            return this;
        }

        public Builder setPingInterval(long pingInterval) {
            this.pingInterval = pingInterval;
            return this;
        }

        public Builder setConnectTimeout(long connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Builder setReadTimeout(long readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public Builder setWriteTimeout(long writeTimeout) {
            this.writeTimeout = writeTimeout;
            return this;
        }

        public Builder setRetryStrategy(RetryStrategy retryStrategy) {
            this.retryStrategy = retryStrategy;
            return this;
        }

        public Builder setEventListener(EventListener listener) {
            this.eventListener = listener;
            return this;
        }

        /**
         * Debug mode
         * true(default) - print log
         * false - print nothing
         */
        public Builder debugMode(boolean debug) {
            this.debug = debug;
            return this;
        }

        public WsConfig build() {
            if (url == null) {
                throw new IllegalStateException("url == null");
            }

            if (websocket == null) {
                throw new IllegalStateException("websocket == null");
            }

            if (httpHeaders == null) {
                httpHeaders = new HashMap<>();
            }

            if (retryStrategy == null) {
                retryStrategy = new DefaultRetryStrategy();
            }

            return new WsConfig(this);
        }
    }
}
