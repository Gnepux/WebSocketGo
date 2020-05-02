package com.gnepux.ws;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.gnepux.wsgo.EventListener;
import com.gnepux.wsgo.WsConfig;
import com.gnepux.wsgo.WsGo;
import com.gnepux.wsgo.okwebsocket.OkWebSocket;
import com.gnepux.wsgo.retry.RetryStrategy;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {

    private String pushUrl = "wss://push.myiot360.com:48000/push";

    private HashMap<String, String> headerMap = new HashMap<String, String>() {
        {
            put("x-zypush-id", "e15cdd187f300af13d05e4034afc7e83c6e5bd5f");
            put("x-zhsq-app", "user");
            put("x-zhsq-platform", "android");
        }
    };

    private TextView logTextView;

    private ScrollView scrollView;

    private Handler printHandler = new Handler();

    private ViewSwitcher switcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switcher = findViewById(R.id.view_switcher);
        switcher.setDisplayedChild(0);

        scrollView = findViewById(R.id.scroll_view);
        logTextView = findViewById(R.id.tv_log);

        addBtn(0, "WsGo init", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();

                printLog("WsGo init");
                switcher.setDisplayedChild(1);
            }
        });

        addBtn(1, "connect", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WsGo.getInstance().connect();
            }
        });

        addBtn(1, "send text", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WsGo.getInstance().send("hello from WsGo");
            }
        });

        addBtn(1, "disconnect", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WsGo.getInstance().disconnectNormal("close");
            }
        });

        addBtn(1, "change ping interval", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WsGo.getInstance().changePingInterval(10, TimeUnit.SECONDS);
            }
        });

        addBtn(1, "destroy", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WsGo.getInstance().destroyInstance();

                printLog("WsGo destroy");
                switcher.setDisplayedChild(0);
            }
        });
    }

    private void init() {
        WsConfig config = new WsConfig.Builder()
                .debugMode(true)
                .setUrl(pushUrl)
                .setHttpHeaders(headerMap)
                .setConnectTimeout(10 * 1000L)
                .setPingInterval(10 * 1000L)
                .setWebSocket(OkWebSocket.create())
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
                        printLog("[connect] success");
                    }

                    @Override
                    public void onDisConnect(Throwable throwable) {
                        printLog("[disconnect] " + throwable.getMessage());
                    }

                    @Override
                    public void onClose(int code, String reason) {
                        printLog("[close] code = " + code + ", reason = " + reason);
                    }

                    @Override
                    public void onMessage(String text) {
                        printLog("[receive] " + text);
                    }

                    @Override
                    public void onReconnect(long retryCount, long delayMillSec) {
                        printLog("[reconnect] " + retryCount + " times retry after " + delayMillSec + "ms");
                    }

                    @Override
                    public void onSend(String text, boolean success) {
                        printLog("[send] text = " + text + " , success = " + success);
                    }
                })
                .build();

        WsGo.init(config);
    }

    private void addBtn(int id, String text, View.OnClickListener listener) {
        LinearLayout linearLayout = findViewById(id == 0 ? R.id.view0 : R.id.view1);
        Button connect = new Button(this);
        connect.setText(text);
        connect.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        connect.setOnClickListener(listener);
        linearLayout.addView(connect);
    }

    private void printLog(final String log) {
        printHandler.post(new Runnable() {
            @Override
            public void run() {
                logTextView.append(log + "\n");
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }
}
