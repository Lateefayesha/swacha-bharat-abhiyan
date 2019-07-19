package com.appynitty.swachbharatabhiyanlibrary.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.adapters.UI.InflateHistoryDetailsAdapter;
import com.appynitty.swachbharatabhiyanlibrary.adapters.connection.HistoryDetailsAdapterClass;
import com.appynitty.swachbharatabhiyanlibrary.pojos.WorkHistoryDetailPojo;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.utils.LocaleHelper;
import java.util.List;
import java.util.Objects;

public class HistoryDetailsPageActivity extends AppCompatActivity {

    private static final String TAG = "HistoryDetailsPageActivity";
    private Toolbar toolbar;
    private Context mContext;
    private GridView detailsGrid;
    private List<WorkHistoryDetailPojo> workHistoryDetailPojoList;
    private String historyDate;
    private View lineView;
    private LinearLayout noDataErrorLayout;

    private HistoryDetailsAdapterClass mAdapter;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initComponents() {
        generateId();
        registerEvents();
        initData();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(AUtils.isNetWorkAvailable(this))
        {
            AUtils.hideSnackBar();
        }
        else {
            AUtils.showSnackBar(this);
        }
    }

    private void generateId() {
        setContentView(R.layout.activity_history_details_page);

        toolbar = findViewById(R.id.toolbar);
        
        mContext = HistoryDetailsPageActivity.this;
        AUtils.mCurrentContext = mContext;

        mAdapter = new HistoryDetailsAdapterClass();

        detailsGrid = findViewById(R.id.history_detail_grid);
        lineView = findViewById(R.id.line_view);
        noDataErrorLayout = findViewById(R.id.show_error_data);

        initToolbar();
    }

    private void registerEvents() {
        mAdapter.setHistoryDetailsListener(new HistoryDetailsAdapterClass.HistoryDetailsListener() {
            @Override
            public void onSuccessCallBack() {
                setHistoryDetails();
            }

            @Override
            public void onFailureCallBack() {

            }
        });
    }

    private void initToolbar() {

        Intent intent = getIntent();

        if(intent.hasExtra(AUtils.HISTORY_DETAILS_DATE)){
            String date = intent.getStringExtra(AUtils.HISTORY_DETAILS_DATE);
            toolbar.setTitle(AUtils.getTitleDateFormat(date));
            historyDate = intent.getStringExtra(AUtils.HISTORY_DETAILS_DATE);
        }

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void initData() {
        mAdapter.fetchHistoryDetails(historyDate);
    }

    private void setHistoryDetails() {
        workHistoryDetailPojoList = mAdapter.getWorkHistoryDetailTypePojoList();

        if(!AUtils.isNull(workHistoryDetailPojoList)){
            noDataErrorLayout.setVisibility(View.GONE);
            lineView.setVisibility(View.VISIBLE);
            InflateHistoryDetailsAdapter adapter = new InflateHistoryDetailsAdapter(mContext, workHistoryDetailPojoList);
            detailsGrid.setAdapter(adapter);
        }else{
            noDataErrorLayout.setVisibility(View.VISIBLE);
        }
    }
}
