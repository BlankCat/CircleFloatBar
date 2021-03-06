package com.hht.floatbar.module.web;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hht.floatbar.R;


public class WebViewActivity extends AppCompatActivity implements WebContract.IWebView {



    TextView mWebTitle;

    Toolbar mWebToolbar;

    ProgressBar mWebProgressBar;

    WebView mWebView;

    private WebContract.IWebPresenter mWebPresenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web_view);

        initView();

    }

    private void initView() {

        mWebToolbar = findViewById(R.id.web_toolbar);
        mWebTitle = findViewById(R.id.web_title);
        mWebProgressBar = findViewById(R.id.web_progressBar);
        mWebView = findViewById(R.id.web_view);

        mWebPresenter = new WebPresenter(this);

        mWebToolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mWebPresenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebPresenter.unSubscribe();
    }

    @Override
    public Activity getWebViewContext() {
        return this;
    }

    @Override
    public void setGankTitle(String title) {
        mWebTitle.setText(title);
    }

    @Override
    public void loadGankUrl(String url) {
        mWebView.loadUrl(url);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(true);

        mWebView.setWebChromeClient(new MyWebChrome());
        mWebView.setWebViewClient(new MyWebClient());
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }

    private class MyWebChrome extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            mWebProgressBar.setVisibility(View.VISIBLE);
            mWebProgressBar.setProgress(newProgress);
        }
    }

    private class MyWebClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            mWebProgressBar.setVisibility(View.GONE);
        }
    }
}
