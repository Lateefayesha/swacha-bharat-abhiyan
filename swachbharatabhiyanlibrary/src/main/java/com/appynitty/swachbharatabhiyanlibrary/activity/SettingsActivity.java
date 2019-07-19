package com.appynitty.swachbharatabhiyanlibrary.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.widget.Toolbar;

import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.utils.LocaleHelper;
import com.appynitty.swachbharatabhiyanlibrary.utils.WebviewInitialize;
import com.mithsoft.lib.activity.BaseActivity;

import java.util.Objects;

import quickutils.core.QuickUtils;

public class SettingsActivity extends BaseActivity {

    private static final String TAG = "SettingsActivity";
    private Context mContext;
    private Toolbar toolbar;

    private Switch switchBifurcation;

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

        setContentView(R.layout.activity_settings);

        mContext = SettingsActivity.this;
        AUtils.mCurrentContext = mContext;

        toolbar = findViewById(R.id.toolbar);
        switchBifurcation = findViewById(R.id.switch_bifurcation);

        initToolBar();
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
    protected void registerEvents() {
        switchBifurcation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                QuickUtils.prefs.save(AUtils.PREFS.IS_GT_FEATURE, checked);
            }
        });
    }

    @Override
    protected void initData() {
        boolean isGtFearture = QuickUtils.prefs.getBoolean(AUtils.PREFS.IS_GT_FEATURE, false);
        switchBifurcation.setChecked(isGtFearture);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initToolBar() {
        toolbar.setTitle(getResources().getString(R.string.setting));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }
}
