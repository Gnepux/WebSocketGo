package com.gnepux.ws;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.gnepux.wsgo.EventListener;
import com.gnepux.wsgo.retry.RetryStrategy;
import com.gnepux.wsgo.WsConfig;
import com.gnepux.wsgo.WsGo;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    String pushUrl = "";
    String zypushId = "e15cdd187f300af13d05e4034afc7e83c6e5bd5f";
    HashMap<String, String> headerMap = new HashMap<String, String>() {
        {
            put("x-zypush-id", zypushId);
            put("x-zhsq-app", "user");
            put("x-zhsq-platform", "android");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WsConfig config = new WsConfig.Builder()
                .setUrl(pushUrl)
                .setHeaders(headerMap)
                .setPingInterval(10 * 1000L)
                .setRetryStrategy(new RetryStrategy() {
                    @Override
                    public long onRetry(int retryCount) {
                        if (retryCount < 2) {
                            return 0;
                        } else if (retryCount < 5) {
                            return 500;
                        } else {
                            return 1000;
                        }
                    }
                })
                .setEventListener(new EventListener() {
                    @Override
                    public void onConnect() {
                        super.onConnect();
                        Log.e("xupeng", "listener onConnect");
                    }

                    @Override
                    public void onDisConnect() {
                        super.onDisConnect();
                        Log.e("xupeng", "listener onDisConnect");
                    }

                    @Override
                    public void onClose() {
                        super.onClose();
                        Log.e("xupeng", "listener onClose");
                    }

                    @Override
                    public void onMessage(String text) {
                        super.onMessage(text);
                        Log.e("xupeng", "listener onMessage:" + text);
                        WsGo.getInstance().send("ok");
                    }

                    @Override
                    public void onRetry(int retryCount, long delayMillSec) {
                        super.onRetry(retryCount, delayMillSec);
                        Log.e("xupeng", "listener onRetry:" + retryCount + " after " + delayMillSec + "ms");
                    }

                    @Override
                    public void onSend(boolean success) {
                        super.onSend(success);
                        Log.e("xupeng", "listener onSend:" + success);
                    }
                })
                .build();
        WsGo.init(config);


        addBtn("前台连接", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WsGo.getInstance().connect();
            }
        });

        addBtn("断开", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WsGo.getInstance().disconnectNormal("关闭");
            }
        });

        addBtn("调整心跳", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WsGo.getInstance().changePingInterval(3, TimeUnit.SECONDS);
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
}
