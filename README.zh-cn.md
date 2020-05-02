## 简介

[English Version](https://github.com/Gnepux/WsGo/blob/master/README.md)

WsGo是一个Java库，可以用来帮助管理WebSocket的连接。

1. 可以用来进行连接、断连、自动重连、调整心跳。
2. 支持OkHttp、Java WebSocket和其他自定义的WebSocket库。
3. 支持Android和纯Java环境。
4. 线程安全。

## 下载

### Gradle
```groovy
implementation 'com.gnepux:wsgo:1.0.2'
// use okhttp
implementation 'com.gnepux:wsgo-okwebsocket:1.0.1'
// use java websocket
implementation 'com.gnepux:wsgo-jwebsocket:1.0.1'
```

### Maven
```xml
<dependency>
    <groupId>com.gnepux</groupId>
    <artifactId>wsgo</artifactId>
    <version>1.0.2</version>
    <type>pom</type>
</dependency>

<!-- use okhttp -->
<dependency>
    <groupId>com.gnepux</groupId>
    <artifactId>wsgo-okwebsocket</artifactId>
    <version>1.0.1</version>
    <type>pom</type>
</dependency>

<!-- use java websocket -->
<dependency>
    <groupId>com.gnepux</groupId>
    <artifactId>wsgo-jwebsocket</artifactId>
    <version>1.0.1</version>
    <type>pom</type>
</dependency>
```

## 使用方法

### 1. 初始化

```java
WsConfig config = new WsConfig.Builder()
                .debugMode(true)    // debug模式则会打印日志
                .setUrl(pushUrl)    // ws url
                .setHttpHeaders(headerMap)  // http headers
                .setConnectTimeout(10 * 1000L)  // connect timeout
                .setReadTimeout(10 * 1000L)     // read timeout
                .setWriteTimeout(10 * 1000L)    // write timeout
                .setPingInterval(10 * 1000L)    // 初始心跳间隔
                .setWebSocket(OkWebSocket.create()) // websocket客户端
                .setRetryStrategy(retryStrategy)    // 重试策略
                .setEventListener(eventListener)    // 事件监听
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

### 3. WsConfig的更多配置

#### 3.1 setWebSocket(WebSocket socket)

WsGo 已经支持OkHttp and Java WebSocket

* for OkHttp (wsgo-okwebsocket)

```java
setWebSocket(OkWebSocket.create());
```

* for Java WebSocket (wsgo-jwebsocket)

```java
setWebSocket(JWebSocket.create());
```

如果你需要使用其他的WebSocket库或自定义客户端，只需要实现一个`WebSocket`接口，将对应结果传递给`ChannelCallback`即可。剩下的连接管理，WsGo会帮你完成。

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

对于非正常断开，WsGo会自动重连。RetryStrategy指的是重连次数和延时的关系。

WsGo默认有一个DefaultRetryStrategy，如果你需要自己调整，实现`RetryStrategy`接口里的`onRetry`方法即可。

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

添加事件回调。需要注意，回调在WsGo自己创建的一个线程中运行，不在调用线程中。如有必要，需要在会调用手动切换线程。

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

### 4. 数据流

![dataflow](https://github.com/Gnepux/WsGo/raw/master/dataflow.png)

### 5. License

```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
