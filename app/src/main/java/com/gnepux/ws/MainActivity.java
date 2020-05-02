package com.gnepux.ws;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
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

import static com.gnepux.ws.UiHelper.addBtn;
import static com.gnepux.ws.UiHelper.addEditText;

/**
 * @author gnepux
 */
public class MainActivity extends Activity {

    private static final String DEFAULT_URL = "wss://";

    private static final HashMap<String, String> DEFAULT_HEADER = new HashMap<String, String>() {
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

        final TextView urlTv = addEditText(this, 0, DEFAULT_URL);

        addBtn(this, 0, "WsGo init", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init(urlTv.getText().toString());

                printLog("WsGo init");
                switcher.setDisplayedChild(1);
            }
        });

        addBtn(this, 1, "connect", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WsGo.getInstance().connect();
            }
        });

        final TextView sendTextTv = addEditText(this, 1, "hello from WsGo");

        addBtn(this, 1, "send text", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WsGo.getInstance().send(sendTextTv.getText().toString());
            }
        });

        addBtn(this, 1, "disconnect", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WsGo.getInstance().disconnectNormal("close");
            }
        });

        addBtn(this, 1, "change ping interval", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WsGo.getInstance().changePingInterval(10, TimeUnit.SECONDS);
            }
        });

        addBtn(this, 1, "destroy", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WsGo.getInstance().destroyInstance();

                printLog("WsGo destroy");
                switcher.setDisplayedChild(0);
            }
        });
    }

    private void init(String url) {
        WsConfig config = new WsConfig.Builder()
                .debugMode(true)
                .setUrl(url)
                .setHttpHeaders(DEFAULT_HEADER)
                .setWebSocket(OkWebSocket.create())
                .setConnectTimeout(10 * 1000L)
                .setReadTimeout(10 * 1000L)
                .setWriteTimeout(10 * 1000L)
                .setPingInterval(10 * 1000L)
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
