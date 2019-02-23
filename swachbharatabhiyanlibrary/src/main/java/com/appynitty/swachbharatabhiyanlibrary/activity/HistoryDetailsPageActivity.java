package com.appynitty.swachbharatabhiyanlibrary.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.adapters.UI.InflateHistoryAdapter;
import com.appynitty.swachbharatabhiyanlibrary.adapters.UI.InflateHistoryDetailsAdapter;
import com.appynitty.swachbharatabhiyanlibrary.connection.SyncServer;
import com.appynitty.swachbharatabhiyanlibrary.pojos.WorkHistoryDetailPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.WorkHistoryPojo;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.utils.LocaleHelper;
import com.appynitty.swachbharatabhiyanlibrary.utils.MyAsyncTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

import quickutils.core.QuickUtils;
import quickutils.core.categories.view;

public class HistoryDetailsPageActivity extends AppCompatActivity {

    private static final String TAG = "HistoryDetailsPageActivity";
    private Toolbar toolbar;
    private Context mContext;
    private GridView detailsGrid;
    private List<WorkHistoryDetailPojo> workHistoryDetailPojoList;
    private String historyDate;
    private View lineView;
    private LinearLayout noDataErrorLayout;
    private Snackbar mSnackbar;

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
    protected void onResume() {
        super.onResume();
        AUtils.mApplication.activityResumed();// On Resume notify the Application
    }

    @Override
    protected void onPause() {
        super.onPause();
        AUtils.mApplication.activityPaused();// On Pause notify the Application
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
        initData();
    }

    private void generateId() {
        setContentView(R.layout.activity_history_details_page);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // Check internet connection and accrding to state change the
        // text of activity by calling method
        if (networkInfo != null && networkInfo.isConnected()) {
            if(mSnackbar.isShown())
            {
                mSnackbar.dismiss();
            }
        } else {
            View view = this.findViewById(R.id.parent);
            mSnackbar = Snackbar.make(view, "\u00A9"+"  "+ getResources().getString(R.string.no_internet_error), Snackbar.LENGTH_INDEFINITE);

            mSnackbar.show();
        }

        toolbar = findViewById(R.id.toolbar);
        
        mContext = HistoryDetailsPageActivity.this;
        AUtils.mCurrentContext = mContext;

        detailsGrid = findViewById(R.id.history_detail_grid);
        lineView = findViewById(R.id.line_view);
        noDataErrorLayout = findViewById(R.id.show_error_data);

        initToolbar();
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
        fetchHistoryDetails();
    }

    private void setHistoryDetails() {
        Type type = new TypeToken<List<WorkHistoryDetailPojo>>(){}.getType();
        workHistoryDetailPojoList = new Gson().fromJson(
                QuickUtils.prefs.getString(AUtils.PREFS.WORK_HISTORY_DETAIL_POJO_LIST, null),
                type);

        if(!AUtils.isNull(workHistoryDetailPojoList)){
            noDataErrorLayout.setVisibility(View.GONE);
            lineView.setVisibility(View.VISIBLE);
            InflateHistoryDetailsAdapter adapter = new InflateHistoryDetailsAdapter(mContext, workHistoryDetailPojoList);
            detailsGrid.setAdapter(adapter);
        }else{
            noDataErrorLayout.setVisibility(View.VISIBLE);
        }
    }

    private void fetchHistoryDetails() {
        new MyAsyncTask(mContext, true, new MyAsyncTask.AsynTaskListener() {
            @Override
            public void doInBackgroundOpration(SyncServer syncServer) {
                Boolean isSuccess = syncServer.pullWorkHistoryDetailListFromServer(historyDate);
            }

            @Override
            public void onFinished() {
                setHistoryDetails();
            }
        }).execute();
    }
}
