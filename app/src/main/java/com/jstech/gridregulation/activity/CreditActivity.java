package com.jstech.gridregulation.activity;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.jstech.gridregulation.R;
import com.jstech.gridregulation.base.BaseActivity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils.SharedPreferencesHelper;

//信用评价
public class CreditActivity extends BaseActivity {

    private WebView webView;
    private ProgressBar progressBar;
    private String orgId;

    @Override
    protected int getLayoutId() {
//        return 0;
        return R.layout.activity_credit;
    }


    @Override
    public void initView() {

        setToolBarTitle("领导驾驶舱");
        setToolSubBarTitle("");
        webView = findViewById(R.id.webview);
        progressBar = findViewById(R.id.progressBar);
        orgId = SharedPreferencesHelper.getInstance(this).getSharedPreference("orgId", "").toString();

        WebSettings webSettings = webView.getSettings();
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        //网页在app内打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    progressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    progressBar.setProgress(newProgress);//设置进度值
                }
                super.onProgressChanged(view, newProgress);
            }
        });


        // TODO: 2018/11/12 打包之前要改
//        String url = "http://192.168.0.56:8280/driverApp.html";//
//        String url = "http://47.104.227.130:8280/driverApp.html";//阿里云
//        String url = "http://218.26.228.85:8085/driverApp.html";//政务云
//        String url = String.format("http://192.168.0.23:8880/driverApp.html?region=%s", orgId);
        String url = String.format("http://trace.jshiinfo.com/driverApp.html?region=%s", orgId);

        webView.loadUrl(url);


    }
}
