package com.lynhill.ghpc.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lynhill.ghpc.R;

public class MyWebView extends AppCompatActivity {
    WebView webView;


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

    @SuppressLint("SetJavaScriptEnabled")
    private void setUpViews() {
        // WebView settings
        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setUseWideViewPort(true);
//        webView.addJavascriptInterface(new JSInterface(), "HTMLOUT");
        webView.loadUrl(getIntent().getStringExtra("url"));
//        webView.loadUrl("https://stackoverflow.com/questions/42759449/android-open-url-in-webview");


    }
    
}