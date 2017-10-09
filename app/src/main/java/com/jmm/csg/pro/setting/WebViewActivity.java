package com.jmm.csg.pro.setting;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jmm.csg.R;
import com.jmm.csg.base.activity.BaseActivity;
import com.jmm.csg.widget.TitleView;

import butterknife.Bind;

public class WebViewActivity extends BaseActivity {

    @Bind(R.id.titleView) TitleView titleView;
    @Bind(R.id.webView) WebView webView;
    private String url;
    private String title;

    public static void start(Context context, String title, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @Override
    public void parseIntent(Intent intent) {
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");
        Log.e("webViewUrl", url);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_web_view;
    }

    @Override
    public void initView() {
        titleView.setTitle(title).setOnLeftImgClickListener(v -> {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                onBackPressed();
            }
        });
        webView.setWebViewClient(new WebViewClient());
        WebSettings settings = webView.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        if (TextUtils.isEmpty(url)) {
            throw new NullPointerException("url is null");
        }
        webView.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
