package com.appynitty.swachbharatabhiyanlibrary.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.appynitty.retrofitconnectionlibrary.pojos.ResultPojo;
import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.adapters.connection.EmpQrLocationAdapterClass;
import com.appynitty.swachbharatabhiyanlibrary.adapters.connection.EmpRegistrationDataAdapterClass;
import com.appynitty.swachbharatabhiyanlibrary.adapters.connection.EmpWardZoneAreaAdapterClass;
import com.appynitty.swachbharatabhiyanlibrary.entity.EmpSyncServerEntity;
import com.appynitty.swachbharatabhiyanlibrary.pojos.EmpRegistrationPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.QrLocationPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.ZoneWardAreaMasterPojo;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.utils.LocaleHelper;
import com.appynitty.swachbharatabhiyanlibrary.view_model.EmpSyncServerViewModel;
import com.google.android.material.textfield.TextInputLayout;
import com.mithsoft.lib.components.MyProgressDialog;
import com.mithsoft.lib.components.Toasty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import quickutils.core.QuickUtils;

public class EmpAddLocationDetailsActivity extends AppCompatActivity {

    private Context mContext;

    private EditText txtName, txtNameMar, txtAddress, txtHouseNo, txtContactNo;
    private Spinner spinnerZone, spinnerWard, spinnerArea;
    private Button btnSubmit;
    private String referenceId, submitType, zoneId, wardId, areaId;

    private HashMap<String, String> zoneMap, zoneWardMap, areaMap;
    ArrayList<String> nameListZone, nameListWardZone, nameListArea;

    private EmpWardZoneAreaAdapterClass mAdapterClass;
    private EmpQrLocationAdapterClass empQrLocationAdapterClass;
    private EmpRegistrationDataAdapterClass empRegistrationDataAdapterClass;

    private MyProgressDialog myProgressDialog;

    private EmpSyncServerViewModel syncServerViewModel;

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
        initData();
        registerEvents();
    }

    private void generateId(){

        setContentView(R.layout.activity_emp_add_location_details);
        mContext = EmpAddLocationDetailsActivity.this;
        AUtils.mCurrentContext = mContext;

        myProgressDialog = new MyProgressDialog(mContext, R.drawable.progress_bar, false);

        zoneMap = new HashMap<>();
        zoneWardMap = new HashMap<>();
        areaMap = new HashMap<>();

        nameListZone = new ArrayList<>();
        nameListWardZone = new ArrayList<>();
        nameListArea = new ArrayList<>();

        TextInputLayout mobileLayout = findViewById(R.id.contact_hint);
        TextInputLayout houseIdLayout = findViewById(R.id.house_id_hint);

        txtName = findViewById(R.id.name);
        txtNameMar = findViewById(R.id.name_mar);
        txtAddress = findViewById(R.id.address);
        txtHouseNo = findViewById(R.id.house_id);
        txtContactNo = findViewById(R.id.mobile_no);
        spinnerZone = findViewById(R.id.select_zone);
        spinnerWard = findViewById(R.id.select_ward);
        spinnerArea = findViewById(R.id.select_area);
        btnSubmit = findViewById(R.id.save_button);


        Intent intent = getIntent();
        if(intent.hasExtra(AUtils.ADD_DETAILS_TYPE_KEY)){
            referenceId = intent.getStringExtra(AUtils.NondaniLocation.REFERENCE_ID);
            submitType = intent.getStringExtra(AUtils.NondaniLocation.SUBMIT_TYPE);

            if(!submitType.equals("1")){
                mobileLayout.setVisibility(View.GONE);
                houseIdLayout.setVisibility(View.GONE);
            }
        }

        initToolbar();

        mAdapterClass = new EmpWardZoneAreaAdapterClass();
        empQrLocationAdapterClass = new EmpQrLocationAdapterClass();
        empRegistrationDataAdapterClass = new EmpRegistrationDataAdapterClass();

        syncServerViewModel = ViewModelProviders.of((AppCompatActivity) AUtils.mCurrentContext).get(EmpSyncServerViewModel.class);
    }

    private void initToolbar(){

        String additionalParam = getResources().getString(R.string.title_activity_emp_add_location_details);

        if(!AUtils.isNull(referenceId) && !referenceId.matches("")){
//            additionalParam = referenceId+"-"+additionalParam;
            additionalParam = referenceId;
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(additionalParam);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void registerEvents(){
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitDetails();
            }
        });

        mAdapterClass.setEmpZoneWardAreaListner(new EmpWardZoneAreaAdapterClass.EmpZoneWardAreaListner() {
            @Override
            public void onSuccessCallback(List<ZoneWardAreaMasterPojo> data, int CallRequestCode) {
                try{
                    if(!AUtils.isNull(data)){
                        switch (CallRequestCode){
                            case EmpWardZoneAreaAdapterClass.CALL_REQUEST_ZONE:
                                initZone(data);
                                break;
                            case EmpWardZoneAreaAdapterClass.CALL_REQUEST_WARD_ZONE:
                                initWardZone(data);
                                break;
                            case EmpWardZoneAreaAdapterClass.CALL_REQUEST_AREA:
                                initArea(data);
                                break;
                        }
                    }else{
                        Toasty.error(mContext, getResources().getString(R.string.something_error), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailureCallback() {
                Toasty.error(mContext, getResources().getString(R.string.something_error), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onErrorCallback() {
                Toasty.error(mContext, getResources().getString(R.string.serverError), Toast.LENGTH_LONG).show();
            }
        });

        empQrLocationAdapterClass.setEmpQrLocationListner(new EmpQrLocationAdapterClass.EmpQrLocationListner() {
            @Override
            public void onSuccessCallback(ResultPojo resultPojo) {
                myProgressDialog.dismiss();
                String message = "";

                if(QuickUtils.prefs.getString(AUtils.LANGUAGE_ID, AUtils.DEFAULT_LANGUAGE_ID).equals("2")){
                    message = resultPojo.getMessageMar();
                }else{
                    message = resultPojo.getMessage();
                }

                if(resultPojo.getStatus().equals(AUtils.STATUS_SUCCESS)){
                    Toasty.success(mContext, message, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }else{
                    Toasty.error(mContext, message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailureCallback() {
                myProgressDialog.dismiss();
                Toasty.error(mContext,getResources().getString(R.string.something_error), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onErrorCallback() {
                myProgressDialog.dismiss();
                Toasty.error(mContext,getResources().getString(R.string.serverError), Toast.LENGTH_LONG).show();
            }
        });

        empRegistrationDataAdapterClass.setEmpRegistrationDetailsListner(new EmpRegistrationDataAdapterClass.EmpRegistrationDetailsListner() {
            @Override
            public void onSuccessCallback(EmpRegistrationPojo empRegistrationPojo) {
                myProgressDialog.dismiss();
                if(!AUtils.isNull(empRegistrationPojo))
                    initFieldData(empRegistrationPojo);
                else
                    Toasty.error(mContext,getResources().getString(R.string.something_error), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailureCallback() {
                myProgressDialog.dismiss();
                Toasty.error(mContext,getResources().getString(R.string.something_error), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onErrorCallback() {
                myProgressDialog.dismiss();
                Toasty.error(mContext,getResources().getString(R.string.serverError), Toast.LENGTH_LONG).show();
            }
        });

        syncServerViewModel.getEmpSysncServerEntityList().observeForever(new Observer<List<EmpSyncServerEntity>>() {
            @Override
            public void onChanged(@Nullable final List<EmpSyncServerEntity> userLocationEntities) {
                // Update list
                AUtils.qrLocationPojoList.clear();

                for(EmpSyncServerEntity entity : userLocationEntities) {
                    QrLocationPojo qrLocationPojo = new QrLocationPojo();
                    qrLocationPojo.setOfflineID(String.valueOf(entity.getIndex_id()));
                    qrLocationPojo.setUserId(QuickUtils.prefs.getString(AUtils.PREFS.USER_ID, ""));
                    qrLocationPojo.setLat(entity.getLat());
                    qrLocationPojo.setLong(entity.getLong());
                    qrLocationPojo.setReferanceId(entity.getRef_id());
                    qrLocationPojo.setGcType(String.valueOf(entity.getGcType()));
                    qrLocationPojo.setDate(String.valueOf(entity.getDate()));
                    qrLocationPojo.setAddress(entity.getAddress());
                    qrLocationPojo.setAreaId(String.valueOf(entity.getAreaId()));
                    qrLocationPojo.setHouseNumber(entity.getHouseNumber());
                    qrLocationPojo.setWardId(String.valueOf(entity.getWardId()));
                    qrLocationPojo.setZoneId(String.valueOf(entity.getZoneId()));
                    qrLocationPojo.setZoneId(String.valueOf(entity.getZoneId()));
                    qrLocationPojo.setMobileno(entity.getMobileno());
                    qrLocationPojo.setName(entity.getName());
                    qrLocationPojo.setNameMar(entity.getNameMar());

                    AUtils.qrLocationPojoList.add(qrLocationPojo);
                }
            }
        });
    }

    private void initData(){
        myProgressDialog.show();

        QrLocationPojo pojo = new QrLocationPojo();
        pojo.setReferanceId(referenceId);
        pojo.setGcType(submitType);
        empRegistrationDataAdapterClass.fetchRegistrationDetails(pojo);

        mAdapterClass.fetchZone();
        mAdapterClass.fetchWardZone();
        mAdapterClass.fetchArea();
    }

    private void initZone(List<ZoneWardAreaMasterPojo> list){

        nameListZone.add("--Select Zone--");
        for(ZoneWardAreaMasterPojo pojo: list){
            nameListZone.add(pojo.getName());
            zoneMap.put(pojo.getName(), pojo.getzoneId());
            zoneMap.put(pojo.getzoneId(), pojo.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, nameListZone);
        spinnerZone.setAdapter(adapter);

        if(!AUtils.isNull(zoneId) && !zoneId.isEmpty()){
            if(zoneMap.containsValue(zoneId) && nameListZone.indexOf(zoneMap.get(zoneId)) > 0){
                spinnerZone.setSelection(nameListZone.indexOf(zoneMap.get(zoneId)));
            }
        }
    }

    private void initWardZone(List<ZoneWardAreaMasterPojo> list){

        nameListWardZone.add("--Select Ward No.--");
        for(ZoneWardAreaMasterPojo pojo: list){
            String name = pojo.getWardNo()+"("+pojo.getZone()+")";
            nameListWardZone.add(name);
            zoneWardMap.put(name, pojo.getWardID());
            zoneWardMap.put(pojo.getWardID(), name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, nameListWardZone);
        spinnerWard.setAdapter(adapter);

        if(!AUtils.isNull(wardId) && !wardId.isEmpty()){
            if(zoneWardMap.containsValue(wardId) && nameListWardZone.indexOf(zoneWardMap.get(wardId)) > 0){
                spinnerWard.setSelection(nameListWardZone.indexOf(zoneWardMap.get(wardId)));
            }
        }
    }

    private void initArea(List<ZoneWardAreaMasterPojo> list){

        nameListArea.add("--Select Area--");
        for(ZoneWardAreaMasterPojo pojo: list){
            String name = pojo.getArea()+"("+pojo.getAreaMar()+")";
            nameListArea.add(name);
            areaMap.put(name, pojo.getId());
            areaMap.put(pojo.getId(), name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, nameListArea);
        spinnerArea.setAdapter(adapter);

        if(!AUtils.isNull(areaId) && !areaId.isEmpty()){
            if(areaMap.containsValue(areaId) && nameListArea.indexOf(areaMap.get(areaId)) > 0){
                spinnerArea.setSelection(nameListArea.indexOf(areaMap.get(areaId)));
            }
        }
    }

    private void initFieldData(EmpRegistrationPojo pojo){
        if(pojo.getStatus().toLowerCase().equals(AUtils.STATUS_SUCCESS)){
            txtName.setText(pojo.getName());
            txtNameMar.setText(pojo.getNamemar());
            txtAddress.setText(pojo.getAddress());

            wardId = pojo.getWardId();
            zoneId = pojo.getZoneId();
            areaId = pojo.getAreaId();

            if(submitType.equals("1") && !AUtils.isNull(pojo.getHouseNumber()) && pojo.getHouseNumber().length() > 0){
                txtHouseNo.setText(pojo.getHouseNumber());
                txtContactNo.setText(pojo.getMobileno());
            }

            if(zoneMap.size() > 0 && zoneMap.containsValue(zoneId) && nameListZone.indexOf(zoneMap.get(zoneId)) > 0){
                spinnerZone.setSelection(nameListZone.indexOf(zoneMap.get(zoneId)));
            }

            if(zoneWardMap.size() > 0 && zoneWardMap.containsValue(wardId) && nameListWardZone.indexOf(zoneWardMap.get(wardId)) > 0){
                spinnerWard.setSelection(nameListWardZone.indexOf(zoneWardMap.get(wardId)));
            }

            if(areaMap.size() > 0 && areaMap.containsValue(areaId) && nameListArea.indexOf(areaMap.get(areaId)) > 0){
                spinnerArea.setSelection(nameListArea.indexOf(areaMap.get(areaId)));
            }

        }else{
            Toast.makeText(mContext, pojo.getMessage(), Toast.LENGTH_LONG).show();
            ((Activity)mContext).finish();
        }
    }

    private void submitDetails(){

        myProgressDialog.show();
        QrLocationPojo qrLocationPojo = new QrLocationPojo();
        qrLocationPojo.setReferanceId(referenceId);
        qrLocationPojo.setGcType(submitType);
        qrLocationPojo.setLat(QuickUtils.prefs.getString(AUtils.LAT, ""));
        qrLocationPojo.setLong(QuickUtils.prefs.getString(AUtils.LONG, ""));

        qrLocationPojo.setName(txtName.getText().toString());
        qrLocationPojo.setNameMar(txtNameMar.getText().toString());
        qrLocationPojo.setAddress(txtAddress.getText().toString());
        if(spinnerZone.getSelectedItemPosition() == 0)
            qrLocationPojo.setZoneId("0");
        else
            qrLocationPojo.setZoneId(zoneMap.get(spinnerZone.getSelectedItem().toString()));

        if (spinnerWard.getSelectedItemPosition() == 0)
            qrLocationPojo.setWardId("0");
        else
            qrLocationPojo.setWardId(zoneWardMap.get(spinnerWard.getSelectedItem().toString()));

        if (spinnerArea.getSelectedItemPosition() == 0)
            qrLocationPojo.setAreaId("0");
        else
            qrLocationPojo.setAreaId(areaMap.get(spinnerArea.getSelectedItem().toString()));

        qrLocationPojo.setHouseNumber(txtHouseNo.getText().toString());
        qrLocationPojo.setMobileno(txtContactNo.getText().toString());
        qrLocationPojo.setUserId(QuickUtils.prefs.getString(AUtils.PREFS.USER_ID, ""));

        if(AUtils.isInternetAvailable()) {
            empQrLocationAdapterClass.saveQrLocation(qrLocationPojo);
        } else {
            insertToDB(qrLocationPojo);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private void insertToDB(QrLocationPojo pojo) {

        EmpSyncServerEntity entity = new EmpSyncServerEntity();

        entity.setRef_id(pojo.getReferanceId());
        entity.setGcType(Integer.parseInt(pojo.getGcType()));
        entity.setLong(QuickUtils.prefs.getString(AUtils.LONG,""));
        entity.setLat(QuickUtils.prefs.getString(AUtils.LAT,""));
        entity.setDate(AUtils.getSeverDateTime());
        entity.setName(pojo.getName());
        entity.setNameMar(pojo.getNameMar());
        entity.setAddress(pojo.getAddress());
        entity.setZoneId(Integer.parseInt(pojo.getZoneId()));
        entity.setWardId(Integer.parseInt(pojo.getWardId()));
        entity.setAreaId(Integer.parseInt(pojo.getAreaId()));
        entity.setHouseNumber(pojo.getHouseNumber());
        entity.setMobileno(pojo.getMobileno());

        syncServerViewModel.insert(entity);

        Toasty.success(mContext, "Uploaded successfully", Toast.LENGTH_LONG).show();
        finish();
    }
}
