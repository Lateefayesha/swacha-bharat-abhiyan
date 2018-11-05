package com.appynitty.swachbharatabhiyanlibrary.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;

import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.utils.LocaleHelper;
import com.appynitty.swachbharatabhiyanlibrary.utils.WebviewInitialize;
import com.mithsoft.lib.activity.BaseActivity;

import java.util.Objects;

public class AboutAppynittyActivity extends BaseActivity {

    private static final String TAG = "AboutAppynittyActivity";
    private Context mContext;
    private Toolbar toolbar;
    private WebView webView;
    private WebviewInitialize webviewInit;

    @Override
    protected void attachBaseContext(Context newBase) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            super.attachBaseContext(LocaleHelper.onAttach(newBase));
        } else {
            super.attachBaseContext(newBase);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void generateId() {

        setContentView(R.layout.activity_about_appynitty);
        mContext = AboutAppynittyActivity.this;
        AUtils.mCurrentContext = mContext;

        toolbar = findViewById(R.id.toolbar);
        webView = findViewById(R.id.webview);
        webviewInit = new WebviewInitialize(mContext, webView);
        initToolBar();
    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void registerEvents() {}

    @Override
    protected void initData() {
        webviewInit.InitiateDefaultWebview("http://www.appynitty.com/");
    }

    @Override
    public void onBackPressed() {
        if(!webviewInit.webviewGoBack()){
            super.onBackPressed();
        }
    }
}
