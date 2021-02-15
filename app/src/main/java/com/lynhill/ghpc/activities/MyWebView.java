package com.lynhill.ghpc.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lynhill.ghpc.R;
import com.lynhill.ghpc.util.StorageManager;

public class MyWebView extends BaseActivity {
    private static final String TAG = MyWebView.class.getSimpleName();
    WebView webView;
    String contentView;
    String PROCESSING1 = "We are processing your Loanpal loan application. You will hear from us soon";
    String PROCESSING2 = "Your application is in process and we will be contacting you soon.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_web_view);
        findViews();
    }

    private void findViews() {
        webView = findViewById(R.id.url_open);
        setUpViews();
    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    private void setUpViews() {
        // WebView settings
        webView.addJavascriptInterface(new MyJavaScriptInterface(contentView), "INTERFACE");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.e(TAG, "onConsoleMessage: " + consoleMessage.message());
                return super.onConsoleMessage(consoleMessage);
            }
        });
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl(getIntent().getStringExtra("url"));
        webView.loadUrl(getIntent().getStringExtra("url"));
    }

    /* An instance of this class will be registered as a JavaScript interface */
    class MyJavaScriptInterface {
        public MyJavaScriptInterface(String aContentView) {
            contentView = aContentView;
        }

        @SuppressWarnings("unused")
        public void processContent(String aContent) {
            contentView = aContent;
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
//            Log.e(TAG, "shouldOverrideUrlLoading: " + url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.loadUrl("javascript:window.INTERFACE.processContent(document.getElementsByTagName('body')[0].innerText);");
            webView.evaluateJavascript(
                    "(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();",
                    new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String html) {
//                            Log.e("HTML", html);
                            // code here
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}