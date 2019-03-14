package com.jstech.gridregulation.activity;

import android.app.Activity;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jstech.gridregulation.R;
import com.jstech.gridregulation.base.BaseActivity;
import com.jstech.gridregulation.utils.AppManager;
import com.jstech.gridregulation.utils.LogUtils;

import java.util.Stack;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;


/**
 * Created by hesm on 2018/10/23.
 */

public class PdfActivity extends BaseActivity {

    private WebView webView;
    private String docPath;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pdf;
    }

    @Override
    public void initView() {
        setToolBarTitle("现场检查文件");
        setToolSubBarTitle("");
        docPath = getIntent().getStringExtra("url");

        webView = findViewById(R.id.webview);
        WebSettings settings = webView.getSettings();
        settings.setSavePassword(false);
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setBuiltInZoomControls(true);
        settings.setAllowFileAccess(true);


        webView.setWebViewClient(new WebViewClient() {

            @Override

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });

        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl("file:///android_asset/pdfjs/web/viewer.html?file=" + docPath);

    }

}
