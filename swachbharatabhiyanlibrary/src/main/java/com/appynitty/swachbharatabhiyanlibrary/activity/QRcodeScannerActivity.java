package com.appynitty.swachbharatabhiyanlibrary.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;

import java.util.Objects;

import io.github.kobakei.materialfabspeeddial.FabSpeedDial;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;
import quickutils.core.categories.view;

public class QRcodeScannerActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler {

    private final static String TAG = "QRcodeScannerActivity";
    private Context mContext;
    private Toolbar toolbar;
    private ZBarScannerView scannerView;
    private FabSpeedDial fabSpeedDial;
    private EditText houseNoEditText;
    private Button submitBtn;
    private View contentView;
    private boolean isResultActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initComponents();
    }

    private void initComponents() {
        generateId();
        registerEvents();
        initData();
    }

    protected void generateId() {
        setContentView(R.layout.activity_qrcode_scanner);
        toolbar = findViewById(R.id.toolbar);
        mContext = QRcodeScannerActivity.this;

        fabSpeedDial = findViewById(R.id.flash_toggle);
        houseNoEditText = findViewById(R.id.txt_house_no);
        submitBtn = findViewById(R.id.submit_button);
        contentView = findViewById(R.id.scanner_view);

        isResultActivity = false;

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.qr_scanner);
        scannerView = new ZBarScannerView(mContext);
        scannerView.setAutoFocus(true);
        scannerView.setLaserColor(getResources().getColor(R.color.colorPrimary));
        scannerView.setBorderColor(getResources().getColor(R.color.colorPrimary));
        contentFrame.addView(scannerView);

        initToolbar();
    }

    protected void initToolbar(){
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    protected void registerEvents() {

        houseNoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().equals("")){
                    scannerView.startCamera();
                    contentView.setVisibility(View.VISIBLE);
                    submitBtn.setVisibility(View.GONE);
                }else{
                    scannerView.stopCamera();
                    contentView.setVisibility(View.GONE);
                    submitBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitQRcode(false);
            }
        });

        fabSpeedDial.getMainFab().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(scannerView.getFlash()){
                    scannerView.setFlash(false);
                    fabSpeedDial.getMainFab().setImageDrawable(getResources().getDrawable(R.drawable.ic_flash_on_indicator));
                }else{
                    scannerView.setFlash(true);
                    fabSpeedDial.getMainFab().setImageDrawable(getResources().getDrawable(R.drawable.ic_flash_off));
                }
            }
        });

    }

    private void submitQRcode(boolean isQrscan) {
        showPopup();
    }

    private void showPopup(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = View.inflate(mContext, R.layout.layout_qr_result, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();

        Button doneBtn = view.findViewById(R.id.done_btn);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

    }

    protected void initData() {
        Intent intent = getIntent();
        if(intent.hasExtra(AUtils.REQUEST_CODE)){
            isResultActivity = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startPreview();
    }

    @Override
    protected void onPause() {
        stopPreview();
        super.onPause();
    }

    public void handleResult(Result result) {
        Toast.makeText(mContext, result.getContents(), Toast.LENGTH_LONG).show();
        restartPreview();
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
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void startPreview(){
        scannerView.startCamera();
        scannerView.resumeCameraPreview(this);
    }

    private void stopPreview(){
        scannerView.stopCameraPreview();
        scannerView.stopCamera();
    }

    private void restartPreview() {
        stopPreview();
        startPreview();
    }
}
