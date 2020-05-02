# Introduction

WsGo is a WebSocket channel management.

1. It helps you to connect, disconnect, auto reconnect, change ping interval.
2. Support OkHttp、Java WebSocket and any other custom WebSocket library.
3. Both Android and pure Java platform are supported.
4. Thread safe.

## Download

```groovy
implementation 'com.gnepux:wsgo:1.0.0'
// use okhttp
implementation 'com.gnepux:wsgo-okwebsocket:1.0.0'
// use java websocket
implementation 'com.gnepux:wsgo-jwebsocket:1.0.0'
```

## Usage

### 1. Init

```java
WsConfig config = new WsConfig.Builder()
                .debugMode(true)    // true to print log
                .setUrl(pushUrl)    // ws url
                .setHttpHeaders(headerMap)  // http headers
                .setConnectTimeout(10 * 1000L)  // connect timeout
                .setReadTimeout(10 * 1000L)     // read timeout
                .setWriteTimeout(10 * 1000L)    // write timeout
                .setPingInterval(10 * 1000L)    // initial ping interval
                .setWebSocket(OkWebSocket.create()) // websocket client
                .setRetryStrategy(retryStrategy)    // retry count and delay time strategy
                .setEventListener(eventListener)    // event listener
                .build();

WsGo.init(config);
```


### 2. Go

```Java
// connect
WsGo.getInstance().connect();

// send text
WsGo.getInstance().send("hello from WsGo");

// disconnect
WsGo.getInstance().disconnect(1000, "close");
WsGo.getInstance().disconnectNormal("close");

// change the ping interval
WsGo.getInstance().changePingInterval(10, TimeUnit.SECONDS);

// destory WsGo instance
WsGo.getInstance().destroyInstance();
```

### 3. More about WsConfig

#### 3.1 setWebSocket(WebSocket socket)

WsGo has already support OkHttp and Java WebSocket.

* for OkHttp

```java
setWebSocket(OkWebSocket.create());
```

* for Java WebSocket

```java
setWebSocket(JWebSocket.create());
```

If you want to use any other WebSocket client. Implementation the `WebSocket` interface and pass the result to `ChannelCallback`, then WsGo will help you manage the channel.

```java
public interface WebSocket {
    void connect(WsConfig config, ChannelCallback callback);

    void reconnect(WsConfig config, ChannelCallback callback);

    boolean disconnect(int code, String reason);

    void changePingInterval(long interval, TimeUnit unit);

    boolean send(String msg);
}
```

#### 3.2 setRetryStrategy(RetryStrategy retryStrategy)

WsGo will auto reconnect if the channel disconnect abnormally. The RetryStrategy means the relationship of retry count and retry delay time.

WsGo has a DefaultRetryStrategy inner, if you want to control it by yourself, you can implementation the `RetryStrategy` interface.

```java
public interface RetryStrategy {

    /**
     * The relationship of retry count and delay time,
     * WsGo will call the method for every reconnect.
     *
     * @param retryCount The retry time that WsGo has retied.
     * @return The delay time in milliseconds.
     */
    long onRetry(long retryCount);

}
```

#### 3.3 setEventListener(EventListener eventListener)

Add an EventListener of WsGo, the callback runs in a different thread to the calling thread. You need to switch thread if needed.

```java
public interface EventListener {

    void onConnect();

    void onDisConnect(Throwable throwable);

    void onClose(int code, String reason);

    void onMessage(String text);

    void onReconnect(long retryCount, long delayMillSec);

    void onSend(String text, boolean success);

}
```

### 4. Data Flow

WsGo use two Message Queue for sending Command & Event.



