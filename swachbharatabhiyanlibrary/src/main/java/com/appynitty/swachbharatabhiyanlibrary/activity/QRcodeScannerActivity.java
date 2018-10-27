package com.appynitty.swachbharatabhiyanlibrary.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.appynitty.swachbharatabhiyanlibrary.R;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.mithsoft.lib.activity.BaseActivity;

import java.util.Objects;

import androidx.annotation.NonNull;

public class QRcodeScannerActivity extends BaseActivity {

    private final static String TAG = "QRcodeScannerActivity";
    private Context mContext;
    private Toolbar toolbar;
    private CodeScanner mCodeScanner;
    private CodeScannerView mCodeScannerView;

    @Override
    protected void generateId() {
        setContentView(R.layout.activity_qrcode_scanner);
        toolbar = findViewById(R.id.toolbar);
        mContext = QRcodeScannerActivity.this;
        mCodeScannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(mContext, mCodeScannerView);
        initToolbar();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void registerEvents() {
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, result.getText(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


        mCodeScannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startScanner();
            }
        });
    }

    private void startScanner() {
        mCodeScanner.startPreview();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}
