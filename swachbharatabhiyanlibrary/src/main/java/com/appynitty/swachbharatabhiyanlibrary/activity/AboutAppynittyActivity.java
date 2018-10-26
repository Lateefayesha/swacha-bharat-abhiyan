package com.appynitty.swachbharatabhiyanlibrary.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

import com.appynitty.swachbharatabhiyanlibrary.R;
import com.mithsoft.lib.activity.BaseActivity;

public class AboutAppynittyActivity extends BaseActivity {

    Toolbar toolbar;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void generateId() {

        setContentView(R.layout.activity_about_appynitty);
        toolbar = findViewById(R.id.toolbar);
        webView = findViewById(R.id.webview);

        initToolBar();
    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
    }

    @Override
    protected void registerEvents() {

    }

    @Override
    protected void initData() {

    }

}
