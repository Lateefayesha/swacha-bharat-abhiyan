package com.appynitty.swachbharatabhiyanlibrary.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.adapters.UI.EmpInflateHistoryAdapter;
import com.appynitty.swachbharatabhiyanlibrary.adapters.connection.EmpHistoryAdapterClass;
import com.appynitty.swachbharatabhiyanlibrary.pojos.WorkHistoryPojo;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.utils.LocaleHelper;
import com.mithsoft.lib.components.Toasty;

import java.util.List;
import java.util.Objects;

public class EmpHistoryPageActivity extends AppCompatActivity {

    private static final String TAG = "EmpHistoryPageActivity";
    private Context mContext;
    private Toolbar toolbar;
    private Spinner yearSpinner, monthSpinner;
    private GridView historyGrid;
    private List<WorkHistoryPojo> historyPojoList;
    private LinearLayout noDataErrorLayout, noInternetErrorLayout;

    private EmpHistoryAdapterClass mAdapter;

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
        AUtils.mCurrentContext = mContext;
        if(AUtils.isNetWorkAvailable(this))
        {
            AUtils.hideSnackBar();
        }
        else {
            AUtils.showSnackBar(this);
        }
    }

    private void generateId() {
        setContentView(R.layout.emp_activity_history_page);

        toolbar = findViewById(R.id.toolbar);

        mContext = EmpHistoryPageActivity.this;
        AUtils.mCurrentContext = mContext;

        mAdapter = new EmpHistoryAdapterClass();

        monthSpinner = findViewById(R.id.spinner_month);
        yearSpinner = findViewById(R.id.spinner_year);
        historyGrid = findViewById(R.id.grid_history);
        noDataErrorLayout = findViewById(R.id.show_error_data);
        noInternetErrorLayout = findViewById(R.id.show_error_internet);
        historyPojoList = null;

        initToolbar();
    }

    private void initToolbar() {
        toolbar.setTitle(getResources().getString(R.string.title_activity_history_page));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void registerEvents() {
        historyGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                showHistoryDetails(position);
            }
        });

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position > 0 && yearSpinner.getSelectedItemPosition() > 0){
                    mAdapter.fetchHistory(
                            yearSpinner.getSelectedItem().toString(),
                            String.valueOf(position)
                    );
                }else{
                    AUtils.showWarning(mContext, getResources().getString(R.string.select_month_year_warn));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position > 0 && monthSpinner.getSelectedItemPosition() > 0){
                    mAdapter.fetchHistory(
                            yearSpinner.getSelectedItem().toString(),
                            String.valueOf(monthSpinner.getSelectedItemPosition())
                    );
                }else{
                    Toasty.info(mContext, getResources().getString(R.string.select_month_year_warn)).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mAdapter.setHistoryListener(new EmpHistoryAdapterClass.HistoryListener() {
            @Override
            public void onSuccessCallBack() {
                setHistoryData();
            }

            @Override
            public void onFailureCallBack() {
                setHistoryData();
            }
        });
    }

    private void initData() {
        initSpinner();

        if(AUtils.isNetWorkAvailable(mContext)){
            noInternetErrorLayout.setVisibility(View.GONE);
            mAdapter.fetchHistory(String.valueOf(AUtils.getCurrentYear()),
                    String.valueOf(AUtils.getCurrentMonth()));
        }else{
            noInternetErrorLayout.setVisibility(View.VISIBLE);
        }
    }

    private void initSpinner() {
        setMonthSpinner(monthSpinner);
        setYearSpinner(yearSpinner);
    }

    public void setMonthSpinner(Spinner monthSpinner) {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(mContext,
                R.layout.layout_simple_white_textview, AUtils.getMonthSpinnerList());
        monthSpinner.setAdapter(spinnerAdapter);
        monthSpinner.setSelection((AUtils.getCurrentMonth()+1), true);
    }

    public void setYearSpinner(Spinner yearSpinner) {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(mContext,
                R.layout.layout_simple_white_textview, AUtils.getYearSpinnerList());
        yearSpinner.setAdapter(spinnerAdapter);
        yearSpinner.setSelection(1, true);
    }

    private void setHistoryData() {
        noInternetErrorLayout.setVisibility(View.GONE);
        historyPojoList = mAdapter.getworkHistoryTypePojoList();

        if(!AUtils.isNull(historyPojoList) && !historyPojoList.isEmpty()){
            historyGrid.setVisibility(View.VISIBLE);
            noDataErrorLayout.setVisibility(View.GONE);
            initGrid();
        }else{
            historyGrid.setVisibility(View.GONE);
            noDataErrorLayout.setVisibility(View.VISIBLE);
        }
    }

    private void initGrid(){
        EmpInflateHistoryAdapter adapter = new EmpInflateHistoryAdapter(mContext, historyPojoList);
        historyGrid.setAdapter(adapter);
    }

    private void showHistoryDetails(int position) {
        Intent intent = new Intent(mContext, EmpHistoryDetailsPageActivity.class);
        intent.putExtra(AUtils.HISTORY_DETAILS_DATE, historyPojoList.get(position).getDate());
        startActivity(intent);
    }
}
