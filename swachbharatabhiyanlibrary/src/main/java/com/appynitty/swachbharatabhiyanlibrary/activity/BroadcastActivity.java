package com.appynitty.swachbharatabhiyanlibrary.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.connection.SyncServer;
import com.appynitty.swachbharatabhiyanlibrary.pojos.CollectionAreaHousePojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.CollectionAreaPointPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.CollectionAreaPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.GarbageCollectionPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.GcResultPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.ImagePojo;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.utils.LocaleHelper;
import com.appynitty.swachbharatabhiyanlibrary.utils.MyAsyncTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mithsoft.lib.components.Toasty;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import io.github.kobakei.materialfabspeeddial.FabSpeedDial;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;
import quickutils.core.QuickUtils;
import quickutils.core.categories.view;

public class BroadcastActivity extends AppCompatActivity {

    private final static String TAG = "BroadcastActivity";
    private Context mContext;
    private Toolbar toolbar;
    private AutoCompleteTextView areaAutoComplete;
    private Button submitBtn;
    private Snackbar mSnackbar;

    private HashMap<String, String> areaHash;

    private List<CollectionAreaPojo> areaPojoList;

    @Override
    protected void attachBaseContext(Context newBase) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            super.attachBaseContext(LocaleHelper.onAttach(newBase));
        } else {
            super.attachBaseContext(newBase);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        registerEvents();
        initData();
    }

    protected void generateId() {
        setContentView(R.layout.activity_broadcast);

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

        mContext = BroadcastActivity.this;
        AUtils.mCurrentContext = mContext;

        areaAutoComplete = findViewById(R.id.txt_area_auto);
        areaAutoComplete.setThreshold(0);
        areaAutoComplete.setDropDownBackgroundResource(R.color.white);
        areaAutoComplete.setSingleLine();

        submitBtn = findViewById(R.id.submit_button);

        areaPojoList = null;

        initToolbar();
    }

    protected void initToolbar(){
        toolbar.setTitle(getResources().getString(R.string.title_activity_broadcast_page));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    protected void registerEvents() {

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isAreaValid())
                {
                    broadcastMessageAsyncTask(areaHash.get(areaAutoComplete.getText().toString().toLowerCase()));
                    Toasty.success(mContext, mContext.getResources().getString(R.string.sending_msg), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        areaAutoComplete.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocused) {
                if(isFocused){
                    areaAutoComplete.showDropDown();
                    AUtils.showKeyboard((Activity)mContext);
                }
            }
        });

        areaAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String area = areaAutoComplete.getText().toString();
            }
        });

    }

    protected void initData() {

        fetchAreaList();
    }

    private void fetchAreaList() {
        new MyAsyncTask(mContext, true, new MyAsyncTask.AsynTaskListener() {

            @Override
            public void doInBackgroundOpration(SyncServer syncServer) {

                areaPojoList = syncServer.fetchCollectionArea(AUtils.HP_AREA_TYPE_ID);
            }

            @Override
            public void onFinished() {
                if(!AUtils.isNull(areaPojoList)){
                    inflateAreaAutoComplete(areaPojoList);
                }else{
                    Toasty.error(mContext, getResources().getString(R.string.serverError)).show();
                }
            }
        }).execute();
    }

    private void broadcastMessageAsyncTask(final String areaID){

        new MyAsyncTask(mContext, false, new MyAsyncTask.AsynTaskListener() {
            @Override
            public void doInBackgroundOpration(SyncServer syncServer) {
                syncServer.pullAreaBroadcastFromServer(areaID);
            }

            @Override
            public void onFinished() {

            }
        }).execute();
    }

    private void inflateAreaAutoComplete(List<CollectionAreaPojo> pojoList){

        areaHash = new HashMap<>();
        ArrayList<String> keyList = new ArrayList<>();
        for(CollectionAreaPojo pojo : pojoList){
            areaHash.put(pojo.getArea().toLowerCase(), pojo.getId());
            keyList.add(pojo.getArea().trim());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_dropdown_item_1line, keyList);
        areaAutoComplete.setThreshold(0);
        areaAutoComplete.setAdapter(adapter);
        if(!areaAutoComplete.isFocused()){
            areaAutoComplete.requestFocus();
        }

    }

    private boolean isAreaValid() {
        String area = areaAutoComplete.getText().toString().toLowerCase();
        if(areaHash.containsKey(area)) {
            return true;
        }
        else {
            Toasty.error(mContext, mContext.getResources().getString(R.string.area_validation)).show();
            return false;
        }
    }
}
