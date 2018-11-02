package com.appynitty.swachbharatabhiyanlibrary.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.GridView;

import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.adapters.UI.InflateHistoryAdapter;
import com.appynitty.swachbharatabhiyanlibrary.adapters.UI.InflateHistoryDetailsAdapter;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;

import java.util.Objects;

public class HistoryDetailsPageActivity extends AppCompatActivity {

    private static final String TAG = "HistoryDetailsPageActivity";
    private Toolbar toolbar;
    private Context mContext;
    private GridView detailsGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initComponents();
    }

    private void initComponents() {
        generateId();
        registerEvents();
        initData();
    }

    private void generateId() {
        setContentView(R.layout.activity_history_details_page);
        toolbar = findViewById(R.id.toolbar);
        
        mContext = HistoryDetailsPageActivity.this;
        detailsGrid = findViewById(R.id.history_detail_grid);

        initToolbar();
    }

    private void initToolbar() {

        Intent intent = getIntent();
        if(intent.hasExtra(AUtils.HISTORY_DETAILS)){
            toolbar.setTitle(intent.getStringExtra(AUtils.HISTORY_DETAILS));
        }

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

    private void registerEvents() {
    }

    private void initData() {
        setHistoryDetails();
    }

    private void setHistoryDetails() {
        InflateHistoryDetailsAdapter adapter = new InflateHistoryDetailsAdapter(mContext, AUtils.getMonthSpinnerList());
        detailsGrid.setAdapter(adapter);
        fetchHistoryDetsils();
    }

    private void fetchHistoryDetsils() {
    }
}
