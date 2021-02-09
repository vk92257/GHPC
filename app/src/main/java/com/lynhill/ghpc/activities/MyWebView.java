package com.lynhill.ghpc.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lynhill.ghpc.R;

public class MyWebView extends BaseActivity {
    private static final String TAG = MyWebView.class.getSimpleName();
    WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_web_view);

        findViews();

    }

    private void findViews() {
        webView =  findViewById(R.id.url_open);

        setUpViews();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setUpViews() {
        // WebView settings
        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setUseWideViewPort(true);
//        webView.addJavascriptInterface(new JSInterface(), "HTMLOUT");
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.e(TAG, "onConsoleMessage: "+consoleMessage.message() );

                return super.onConsoleMessage(consoleMessage);
            }
        });
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl(getIntent().getStringExtra("url"));
//        webView.loadUrl("https://stackoverflow.com/questions/42759449/android-open-url-in-webview");


    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            Log.e(TAG, "shouldOverrideUrlLoading: "+url );
             return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.e(TAG, "onPageFinished: "+url );
            Intent intent = new Intent(MyWebView.this, BankActivity.class);
            intent.putExtra("status","success");
            setResult(RESULT_OK,intent);
            MyWebView.this.finish();
            view.loadUrl("javascript:console.log(document.body.getElementsByTagName('pre')[0].innerHTML);");


//            if (url.contains("success-url"))
//            {
//                //call intent to navigate to activity
//                setResult(RESULT_OK, bundle);
//                ActivityWebview.this.finish();
//            }
        }

    }

}