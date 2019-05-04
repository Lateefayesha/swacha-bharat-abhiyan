package com.appynitty.swachbharatabhiyanlibrary.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.utils.LocaleHelper;
import com.mithsoft.lib.components.Toasty;

import java.util.HashMap;
import java.util.Objects;

public class DumpYardWeightActivity extends AppCompatActivity {

    private Context mContext;
    private Toolbar toolbar;
    private String dumpYardId;
    private Intent intent;
    private Button btnSubmitDumpDetails;

    private EditText editDryTotal, editWetTotal, editTotal;

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

        initComponents();

    }

    private void initComponents(){
        generateId();
        initToolbar();
        registerEvents();
        initData();
    }

    private void initToolbar(){
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.title_activity_dump_yard_weight);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void generateId(){
        setContentView(R.layout.activity_dump_yard_weight);
        mContext = DumpYardWeightActivity.this;
        AUtils.mCurrentContext = mContext;

        toolbar = findViewById(R.id.toolbar);

        intent = getIntent();

        editTotal = findViewById(R.id.txt_total_weight);
        editDryTotal = findViewById(R.id.txt_dry_weight);
        editWetTotal = findViewById(R.id.txt_wet_weight);

        btnSubmitDumpDetails = findViewById(R.id.btn_submit_dump);
    }

    private void registerEvents(){
        btnSubmitDumpDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateForm()){
                    submitDumpDetailData();
                }
            }
        });
    }

    private void initData(){
        if(intent.hasExtra(AUtils.dumpYardId)){
            dumpYardId = intent.getStringExtra(AUtils.dumpYardId);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private Boolean validateForm(){

        if(
            AUtils.isNullString(editTotal.getText().toString()) ||
            AUtils.isNullString(editDryTotal.getText().toString()) ||
            AUtils.isNullString(editWetTotal.getText().toString())
        ){
            Toasty.error(mContext, mContext.getString(R.string.plz_ent_all_fields)).show();
            return false;
        }else if(
            !AUtils.isNumeric(editTotal.getText().toString()) ||
            !AUtils.isNumeric(editDryTotal.getText().toString()) ||
            !AUtils.isNumeric(editWetTotal.getText().toString())
        ){
            Toasty.error(mContext, mContext.getString(R.string.plz_ent_only_number)).show();
            return false;
        }

        return true;
    }

    private void submitDumpDetailData(){
        Intent intent = new Intent();
        intent.putExtra(AUtils.DUMPDATA.dumpDataMap, getIntentMap());
        setResult(RESULT_OK, intent);
        finish();
    }

    private HashMap<String, String> getIntentMap(){

        HashMap<String, String> map = new HashMap<>();
        map.put(AUtils.DUMPDATA.dumpYardId, dumpYardId);
        map.put(AUtils.DUMPDATA.weightTotal, editTotal.getText().toString());
        map.put(AUtils.DUMPDATA.weightTotalDry, editDryTotal.getText().toString());
        map.put(AUtils.DUMPDATA.weightTotalWet, editWetTotal.getText().toString());

        return map;
    }

}
