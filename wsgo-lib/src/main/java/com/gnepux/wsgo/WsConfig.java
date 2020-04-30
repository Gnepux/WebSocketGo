package com.gnepux.wsgo;

import com.gnepux.wsgo.protocol.WebSocket;
import com.gnepux.wsgo.protocol.okhttp.OkWebSocket;
import com.gnepux.wsgo.retry.RetryStrategy;

import java.util.HashMap;

import static com.gnepux.wsgo.constant.Constants.DEFAULT_CONNECT_TIMEOUT;
import static com.gnepux.wsgo.constant.Constants.DEFAULT_PING_INTERVAL;
import static com.gnepux.wsgo.constant.Constants.DEFAULT_READ_TIMEOUT;
import static com.gnepux.wsgo.constant.Constants.DEFAULT_WRITE_TIMEOUT;

public class WsConfig {

    public WebSocket mWebSocket = null;

    public EventListener mEventListener = null;

    public long mPingInterval = DEFAULT_PING_INTERVAL;

    public long mConnectTimeout = DEFAULT_CONNECT_TIMEOUT;

    public long mReadTimeout = DEFAULT_READ_TIMEOUT;

    public long mWriteTimeout = DEFAULT_WRITE_TIMEOUT;

    public RetryStrategy mRetryStrategy;

    public HashMap<String, String> mHeaders = new HashMap<>();

    public String mUrl = "";

    private WsConfig() {
    }

    public static class Builder {

        private WsConfig mConfig = new WsConfig();

        public Builder setEventListener(EventListener listener) {
            this.mConfig.mEventListener = listener;
            return this;
        }

        public Builder setRetryStrategy(RetryStrategy retryStrategy) {
            this.mConfig.mRetryStrategy = retryStrategy;
            return this;
        }

        public Builder setPingInterval(long pingInterval) {
            this.mConfig.mPingInterval = pingInterval;
            return this;
        }

        public Builder setConnectTimeout(long connectTimeout) {
            this.mConfig.mConnectTimeout = connectTimeout;
            return this;
        }

        public Builder setReadTimeout(long readTimeout) {
            this.mConfig.mReadTimeout = readTimeout;
            return this;
        }

        public Builder setWriteTimeout(long writeTimeout) {
            this.mConfig.mWriteTimeout = writeTimeout;
            return this;
        }

        public Builder setHeaders(HashMap<String, String> headers) {
            this.mConfig.mHeaders = headers;
            return this;
        }

        public Builder setUrl(String url) {
            this.mConfig.mUrl = url;
            return this;
        }

        public WsConfig build() {
            if (mConfig.mWebSocket == null) {
                mConfig.mWebSocket = new OkWebSocket.Factory().create();
            }
            return mConfig;
        }
    }
}
