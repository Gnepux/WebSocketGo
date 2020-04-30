package com.gnepux.wsgo.protocol.okhttp;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class OkUtils {

    /**
     * generate an OkHttp Client
     */
    public static OkHttpClient generateClient(long interval, long connectTimeout, long readTimeout, long writeTimeout) {
        try {
            SSLContext sslContext;
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, null, null);
            SSLSocketFactory socketFactory = sslContext.getSocketFactory();
            return new OkHttpClient.Builder()
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    })
                    .pingInterval(interval, TimeUnit.MILLISECONDS)
                    .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                    .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                    .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS)
                    .sslSocketFactory(socketFactory)
                    .build();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * generate an OkHttp Request
     */
    public static Request generateRequest(HashMap<String, String> headers, String url) throws IllegalArgumentException, NullPointerException {
        Request.Builder requestBuilder = new Request.Builder();
        if (headers != null) {
            for (HashMap.Entry<String, String> entry : headers.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                requestBuilder.addHeader(key, value);
            }
        }
        requestBuilder.url(url);
        return requestBuilder.build();
    }
}
