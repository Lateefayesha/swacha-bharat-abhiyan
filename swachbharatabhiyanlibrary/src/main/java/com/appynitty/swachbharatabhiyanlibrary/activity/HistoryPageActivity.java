package com.appynitty.swachbharatabhiyanlibrary.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;

import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.adapters.UI.InflateHistoryAdapter;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;

import java.util.Objects;

public class HistoryPageActivity extends AppCompatActivity {

    private static final String TAG = "HistoryPageActivity";
    private Context mContext;
    private Toolbar toolbar;
    private Spinner yearSpinner, monthSpinner;
    private GridView historyGrid;

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
        setContentView(R.layout.activity_history_page);
        toolbar = findViewById(R.id.toolbar);
        mContext = HistoryPageActivity.this;
        monthSpinner = findViewById(R.id.spinner_month);
        yearSpinner = findViewById(R.id.spinner_year);
        historyGrid = findViewById(R.id.grid_history);

        initToolbar();
    }

    private void initToolbar() {
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
        initSpinner();
        setHistoryData();
    }

    private void initSpinner() {
        setMonthSpinner(monthSpinner);
        setYearSpinner(yearSpinner);
    }

    public void setMonthSpinner(Spinner monthSpinner) {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(mContext,
                R.layout.layout_simple_white_textview, AUtils.getMonthSpinnerList());
        monthSpinner.setAdapter(spinnerAdapter);
    }

    public void setYearSpinner(Spinner yearSpinner) {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(mContext,
                R.layout.layout_simple_white_textview, AUtils.getYearSpinnerList());
        yearSpinner.setAdapter(spinnerAdapter);
    }

    private void setHistoryData() {
        InflateHistoryAdapter adapter = new InflateHistoryAdapter(mContext, AUtils.getMonthSpinnerList());
        historyGrid.setAdapter(adapter);
        fetchHistory();
    }

    private void fetchHistory() {
    }
}
