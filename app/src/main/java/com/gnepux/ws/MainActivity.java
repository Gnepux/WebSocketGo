package com.gnepux.ws;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.gnepux.wsgo.EventListener;
import com.gnepux.wsgo.WsConfig;
import com.gnepux.wsgo.WsGo;
import com.gnepux.wsgo.protocol.okhttp.OkWebSocket;
import com.gnepux.wsgo.retry.RetryStrategy;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {

    private String pushUrl = "";

    private HashMap<String, String> headerMap = new HashMap<String, String>() {
        {
            put("x-zypush-id", "e15cdd187f300af13d05e4034afc7e83c6e5bd5f");
            put("x-zhsq-app", "user");
            put("x-zhsq-platform", "android");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addBtn("WsGo init", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
            }
        });

        addBtn("connnect", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WsGo.getInstance().connect();
            }
        });

        addBtn("disconnect", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WsGo.getInstance().disconnectNormal("close");
            }
        });

        addBtn("change ping interval", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WsGo.getInstance().changePingInterval(10, TimeUnit.SECONDS);
            }
        });

        addBtn("destroy", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WsGo.getInstance().destroyInstance();
            }
        });
    }

    private void addBtn(String text, View.OnClickListener listener) {
        LinearLayout linearLayout = findViewById(R.id.ll);
        Button connect = new Button(this);
        connect.setText(text);
        connect.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        connect.setOnClickListener(listener);
        linearLayout.addView(connect);
    }

    private void init() {
        WsConfig config = new WsConfig.Builder()
                .debugMode(false)
                .setUrl(pushUrl)
                .setHttpHeaders(headerMap)
                .setConnectTimeout(10 * 1000L)
                .setPingInterval(10 * 1000L)
                .setWebSocket(new OkWebSocket.Factory().create())
                .setRetryStrategy(new RetryStrategy() {
                    @Override
                    public long onRetry(long retryCount) {
                        if (retryCount < 2) {
                            return 0;
                        } else if (retryCount < 5) {
                            return 5 * 1000;
                        } else {
                            return 10 * 1000;
                        }
                    }
                })
                .setEventListener(new EventListener() {
                    @Override
                    public void onConnect() {
                        Log.e("xupeng", "listener onConnect");
                    }

                    @Override
                    public void onDisConnect() {
                        Log.e("xupeng", "listener onDisConnect");
                    }

                    @Override
                    public void onClose() {
                        Log.e("xupeng", "listener onClose");
                    }

                    @Override
                    public void onMessage(String text) {
                        Log.e("xupeng", "listener onMessage:" + text);
                        WsGo.getInstance().send("ok");
                    }

                    @Override
                    public void onRetry(long retryCount, long delayMillSec) {
                        Log.e("xupeng", "listener onRetry:" + retryCount + " after " + delayMillSec + "ms");
                    }

                    @Override
                    public void onSend(boolean success) {
                        Log.e("xupeng", "listener onSend:" + success);
                    }
                })
                .build();

        WsGo.init(config);
    }
}
