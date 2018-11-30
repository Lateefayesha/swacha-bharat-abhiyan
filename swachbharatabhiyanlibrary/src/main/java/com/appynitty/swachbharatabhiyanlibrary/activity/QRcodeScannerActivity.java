package com.appynitty.swachbharatabhiyanlibrary.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
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

public class QRcodeScannerActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler {

    private final static String TAG = "QRcodeScannerActivity";
    private Context mContext;
    private Toolbar toolbar;
    private ZBarScannerView scannerView;
    private FabSpeedDial fabSpeedDial;
    private AutoCompleteTextView areaAutoComplete;
    private TextInputLayout idIpLayout;
    private AutoCompleteTextView idAutoComplete;
    private RadioGroup collectionRadioGroup;
    private String radioSelection;
    private Button submitBtn, permissionBtn;
    private View contentView;
    private boolean isActivityData;
    private ImagePojo imagePojo;
    private Boolean isScanQr;
    private HashMap<String, String> areaHash;
    private HashMap<String, String> idHash;
    private List<CollectionAreaPojo> areaPojoList;
    private List<CollectionAreaHousePojo> hpPojoList;
    private List<CollectionAreaPointPojo> gpPojoList;

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

    private void initComponents() {
        generateId();
        registerEvents();
        initData();
    }

    protected void generateId() {
        setContentView(R.layout.activity_qrcode_scanner);
        toolbar = findViewById(R.id.toolbar);

        mContext = QRcodeScannerActivity.this;
        AUtils.mCurrentContext = mContext;

        fabSpeedDial = findViewById(R.id.flash_toggle);

        areaAutoComplete = findViewById(R.id.txt_area_auto);
        areaAutoComplete.setThreshold(0);
        areaAutoComplete.setDropDownBackgroundResource(R.color.white);
        areaAutoComplete.setSingleLine();

        idAutoComplete = findViewById(R.id.txt_id_auto);
        idAutoComplete.setThreshold(0);
        idAutoComplete.setDropDownBackgroundResource(R.color.white);
        idAutoComplete.setSingleLine();

        idIpLayout = findViewById(R.id.txt_id_layout);
        collectionRadioGroup = findViewById(R.id.collection_radio_group);
        radioSelection = AUtils.RADIO_SELECTED_HP;

        submitBtn = findViewById(R.id.submit_button);
        permissionBtn = findViewById(R.id.grant_permission);
        contentView = findViewById(R.id.scanner_view);

        imagePojo = null;
        areaPojoList = null;
        isActivityData = false;
        isScanQr = true;

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.qr_scanner);
        scannerView = new ZBarScannerView(mContext);
        scannerView.setLaserColor(getResources().getColor(R.color.colorPrimary));
        scannerView.setBorderColor(getResources().getColor(R.color.colorPrimary));
        contentFrame.addView(scannerView);

        initToolbar();
    }

    protected void initToolbar(){
        toolbar.setTitle(getResources().getString(R.string.title_activity_qrcode_scanner));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    protected void registerEvents() {

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    Boolean areaValid = isAutoCompleteValid(areaAutoComplete, areaHash);
                    Boolean idValid = isAutoCompleteValid(idAutoComplete, idHash);

                    if(areaValid && idValid){
                        submitQRcode(idHash.get(idAutoComplete.getText().toString().toLowerCase()));
                    }else{
                        if(getAreaType().equals(AUtils.HP_AREA_TYPE_ID))
                            Toasty.error(mContext, mContext.getResources().getString(R.string.hp_area_validation)).show();
                        else
                            Toasty.error(mContext, mContext.getResources().getString(R.string.gp_area_validation)).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        collectionRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioGroupId = radioGroup.getCheckedRadioButtonId();

                areaAutoComplete.setText("");
                idAutoComplete.setText("");
                AUtils.showKeyboard((Activity) mContext);

                if (radioGroupId == R.id.house_collection_radio) {
                    idIpLayout.setHint(getResources().getString(R.string.house_number_hint));
                    radioSelection = AUtils.RADIO_SELECTED_HP;
                    fetchAreaList(true);
                }

                if (radioGroupId == R.id.point_collection_radio){
                    idIpLayout.setHint(getResources().getString(R.string.gp_id_hint));
                    radioSelection = AUtils.RADIO_SELECTED_GP;
                    fetchAreaList(true);
                }
            }
        });

        idAutoComplete.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocused) {
                if(isFocused)
                    AUtils.showKeyboard((Activity) mContext);

                if (isFocused && isScanQr) {
                    hideQR();
                    AUtils.showKeyboard((Activity) mContext);
                }else {
                    idAutoComplete.clearListSelection();
                }
            }
        });

        idAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(isAutoCompleteValid(idAutoComplete, idHash))
                    AUtils.hideKeyboard((Activity) mContext);
                else
                    Toasty.error(mContext, mContext.getResources().getString(R.string.area_validation)).show();
            }
        });

        areaAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(isAutoCompleteValid(areaAutoComplete, areaHash))
                    inflateAutoComplete(areaHash.get(areaAutoComplete.getText().toString().toLowerCase()));
                else
                    Toasty.error(mContext, mContext.getResources().getString(R.string.area_validation)).show();
            }
        });

        areaAutoComplete.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_NEXT){
                    if(isAutoCompleteValid(areaAutoComplete, areaHash)){
                        inflateAutoComplete(areaHash.get(areaAutoComplete.getText().toString().toLowerCase()));
                        return false;
                    }else {
                        areaAutoComplete.requestFocus();
                        Toasty.error(mContext, mContext.getResources().getString(R.string.area_validation)).show();
                        return true;
                    }
                }
                return false;
            }
        });

        idAutoComplete.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if(actionId == EditorInfo.IME_ACTION_DONE){
                    if(isAutoCompleteValid(idAutoComplete, idHash)){
                        AUtils.hideKeyboard((Activity) mContext);
                        return false;
                    }else {
                        idAutoComplete.requestFocus();
                        if(getAreaType().equals(AUtils.HP_AREA_TYPE_ID))
                            Toasty.error(mContext, mContext.getResources().getString(R.string.hp_validation)).show();
                        else
                            Toasty.error(mContext, mContext.getResources().getString(R.string.gp_validation)).show();

                        return true;
                    }
                }
                return false;
            }
        });

        permissionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCameraPermission();
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

    protected void initData() {

        checkCameraPermission();
        fetchAreaList(false);

        Intent intent = getIntent();
        if(intent.hasExtra(AUtils.REQUEST_CODE)){
            Type type = new TypeToken<ImagePojo>(){}.getType();
            imagePojo = new Gson().fromJson(QuickUtils.prefs.getString(AUtils.PREFS.IMAGE_POJO, null), type);

            if(!AUtils.isNull(imagePojo)){
                isActivityData = true;
            }
        }
    }

    private void submitQRcode(String houseid) {
        startSubmitQRAsyncTask(houseid);
    }

    private void showPopup(String id, GcResultPojo pojo){

        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        View view = View.inflate(mContext, R.layout.layout_qr_result, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        if(!dialog.isShowing()){
            dialog.show();
        }

        final String responseStatus = pojo.getStatus();

        TextView ownerName = view.findViewById(R.id.house_owner_name);
        TextView houseId = view.findViewById(R.id.house_id);
        TextView collectionStatus = view.findViewById(R.id.collection_status);
        ImageView statusImage = view.findViewById(R.id.response_image);
        Button doneBtn = view.findViewById(R.id.done_btn);

        if(responseStatus.equals(AUtils.STATUS_ERROR)){
            statusImage.setImageDrawable(getDrawable(R.drawable.ic_cancel_red));
            doneBtn.setText(getString(R.string.retry_txt));
            houseId.setText(null);
            if(QuickUtils.prefs.getString(AUtils.LANGUAGE_ID, AUtils.DEFAULT_LANGUAGE_ID).equals("2")){
                collectionStatus.setText(pojo.getMessageMar());
            }else{
                collectionStatus.setText(pojo.getMessage());
            }
            ownerName.setText(id.toUpperCase());
        }else if(responseStatus.equals(AUtils.STATUS_SUCCESS)){
            ownerName.setText(pojo.getName());
            houseId.setText(id);

            Type type = new TypeToken<ImagePojo>() {
            }.getType();

            imagePojo = new Gson().fromJson(
                    QuickUtils.prefs.getString(AUtils.PREFS.IMAGE_POJO , null), type);
        }

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                restartPreview();
                if(responseStatus.equals(AUtils.STATUS_SUCCESS)){
                    imagePojo = null;
                    QuickUtils.prefs.save(AUtils.PREFS.IMAGE_POJO, null);
                    finish();
                }
            }
        });

    }

    private void checkCameraPermission() {
        if(AUtils.isCameraPermissionGiven(mContext)){
            startPreview();
            contentView.setVisibility(View.VISIBLE);
            permissionBtn.setVisibility(View.GONE);
            checkLocationPermission();
        }else{
            contentView.setVisibility(View.GONE);
            permissionBtn.setVisibility(View.VISIBLE);
        }
    }

    private void scanQR(){
        isScanQr = true;
        startCamera();
        contentView.setVisibility(View.VISIBLE);
        submitBtn.setVisibility(View.GONE);
        collectionRadioGroup.setVisibility(View.GONE);
        areaAutoComplete.setVisibility(View.GONE);
        areaAutoComplete.setText("");
        idAutoComplete.clearFocus();
        idAutoComplete.setText("");
        idIpLayout.setHint(getResources().getString(R.string.hp_gp_id_hint));
    }

    private void hideQR(){
        isScanQr= false;
        stopCamera();
        contentView.setVisibility(View.GONE);
        submitBtn.setVisibility(View.VISIBLE);
        collectionRadioGroup.setVisibility(View.VISIBLE);
        areaAutoComplete.setVisibility(View.VISIBLE);
        areaAutoComplete.requestFocusFromTouch();
        areaAutoComplete.setSelected(true);

        if(radioSelection.equals(AUtils.RADIO_SELECTED_HP)) {
            idIpLayout.setHint(getResources().getString(R.string.house_number_hint));
        }
        else{
            idIpLayout.setHint(getResources().getString(R.string.gp_id_hint));
        }
    }

    @SuppressLint("MissingPermission")
    private void checkLocationPermission() {

        if (AUtils.isLocationPermissionGiven(mContext)) {
            //You already have the permission, just go ahead.
            LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

            boolean GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (!GpsStatus) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }else{
                AUtils.saveLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AUtils.MY_PERMISSIONS_REQUEST_CAMERA) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                checkCameraPermission();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.CAMERA)) {

                AUtils.showPermissionDialog(mContext, "CAMERA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, AUtils.MY_PERMISSIONS_REQUEST_CAMERA);
                        }
                    }
                });
            }
        } else if (requestCode == AUtils.MY_PERMISSIONS_REQUEST_LOCATION) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                checkLocationPermission();
            }else if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.ACCESS_FINE_LOCATION)) {

                AUtils.showPermissionDialog(mContext, "Location Service", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, AUtils.MY_PERMISSIONS_REQUEST_LOCATION);
                        }
                    }
                });
            }

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
        submitQRcode(result.getContents());
//        restartPreview();
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

    private void startPreview(){
        scannerView.startCamera();
        scannerView.resumeCameraPreview(this);
    }

    private void stopPreview(){
        scannerView.stopCameraPreview();
        scannerView.stopCamera();
    }

    private void startCamera(){
        scannerView.startCamera();
    }

    private void stopCamera(){
        scannerView.stopCamera();
    }

    private void restartPreview() {
        stopPreview();
        startPreview();
    }

    private void fetchAreaList(Boolean isProgress) {
        new MyAsyncTask(mContext, isProgress, new MyAsyncTask.AsynTaskListener() {

            @Override
            public void doInBackgroundOpration(SyncServer syncServer) {

                areaPojoList = syncServer.fetchCollectionArea(getAreaType());
            }

            @Override
            public void onFinished() {
                if(!AUtils.isNull(areaPojoList)){
                    areaAutoComplete.clearListSelection();
                    areaAutoComplete.requestFocus();
                    idAutoComplete.clearListSelection();
                    inflateAreaAutoComplete(areaPojoList);
                }else{
                    Toasty.error(mContext, getResources().getString(R.string.serverError)).show();
                }
            }
        }).execute();
    }

    private void fetchHpList(final String areaId) {
        new MyAsyncTask(mContext, true, new MyAsyncTask.AsynTaskListener() {

            @Override
            public void doInBackgroundOpration(SyncServer syncServer) {

                hpPojoList = syncServer.fetchCollectionAreaHouse(getAreaType(), areaId);
            }

            @Override
            public void onFinished() {
                if(!AUtils.isNull(hpPojoList)){
                    inflateHpAutoComplete(hpPojoList);
                }else{
                    Toasty.error(mContext, getResources().getString(R.string.serverError)).show();
                }
            }
        }).execute();
    }

    private void fetchGpList(final String areaId) {
        new MyAsyncTask(mContext, true, new MyAsyncTask.AsynTaskListener() {

            @Override
            public void doInBackgroundOpration(SyncServer syncServer) {
                String areaType = "2";
                if(radioSelection.equals(AUtils.RADIO_SELECTED_HP)){
                    areaType = "1";
                }

                gpPojoList = syncServer.fetchCollectionAreaPoint(areaType, areaId);
            }

            @Override
            public void onFinished() {
                if(!AUtils.isNull(gpPojoList)){
                    inflateGpAutoComplete(gpPojoList);
                }else{
                    Toasty.error(mContext, getResources().getString(R.string.serverError)).show();
                }
            }
        }).execute();
    }

    private void startSubmitQRAsyncTask(final String houseNo){

        stopCamera();
        new MyAsyncTask(mContext, true, new MyAsyncTask.AsynTaskListener() {
            GcResultPojo resultPojo = null;
            @Override
            public void doInBackgroundOpration(SyncServer syncServer) {

                GarbageCollectionPojo garbageCollectionPojo = new GarbageCollectionPojo();
                garbageCollectionPojo.setId(houseNo);
                if(isActivityData){
                    garbageCollectionPojo.setAfterImage(imagePojo.getAfterImage());
                    garbageCollectionPojo.setBeforeImage(imagePojo.getBeforeImage());
                    garbageCollectionPojo.setComment(imagePojo.getComment());
                    garbageCollectionPojo.setImage1(imagePojo.getImage1());
                    garbageCollectionPojo.setImage2(imagePojo.getImage2());
                }

                resultPojo = syncServer.saveGarbageCollection(garbageCollectionPojo);
            }

            @Override
            public void onFinished() {
                if(!AUtils.isNull(resultPojo)){
                    showPopup(houseNo, resultPojo);
                }else{
                    restartPreview();
                    Toasty.error(mContext, mContext.getString(R.string.serverError), Toast.LENGTH_SHORT).show();
                }
            }
        }).execute();
    }

    private void inflateAreaAutoComplete(List<CollectionAreaPojo> pojoList){

        areaHash = new HashMap<>();
        ArrayList<String> keyList = new ArrayList<>();
        for(CollectionAreaPojo pojo : pojoList){
            areaHash.put(pojo.getArea().toLowerCase()/**/, pojo.getId());
            keyList.add(pojo.getArea().trim());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_dropdown_item_1line, keyList);
        areaAutoComplete.setThreshold(0);
        areaAutoComplete.setAdapter(adapter);
        if(!areaAutoComplete.isFocused()){
            areaAutoComplete.requestFocus();
        }
//        areaAutoComplete.showDropDown();

    }

    private void inflateHpAutoComplete(List<CollectionAreaHousePojo> pojoList){

        idHash = new HashMap<>();
        ArrayList<String> keyList = new ArrayList<>();
        for(CollectionAreaHousePojo pojo : pojoList){
            idHash.put(pojo.getHouseNumber().toLowerCase(), pojo.getHouseid());
            keyList.add(pojo.getHouseNumber().trim());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_dropdown_item_1line, keyList);
        idAutoComplete.setThreshold(0);
        idAutoComplete.setAdapter(adapter);
        idAutoComplete.requestFocus();
    }

    private void inflateGpAutoComplete(List<CollectionAreaPointPojo> pojoList){

        idHash = new HashMap<>();
        ArrayList<String> keyList = new ArrayList<>();
        for(CollectionAreaPointPojo pojo : pojoList){
            idHash.put(pojo.getGpName().toLowerCase(), pojo.getGpId());
            keyList.add(pojo.getGpName().trim());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_dropdown_item_1line, keyList);
        idAutoComplete.setThreshold(0);
        idAutoComplete.setAdapter(adapter);
        idAutoComplete.requestFocus();
    }

    private void inflateAutoComplete(String areaId){
        String areaType = getAreaType();
        if(areaType.equals(AUtils.HP_AREA_TYPE_ID)){
            fetchHpList(areaId);
        }

        if(areaType.equals(AUtils.GP_AREA_TYPE_ID)){
            fetchGpList(areaId);
        }
    }

    private String getAreaType(){
        String areaType = AUtils.GP_AREA_TYPE_ID;
        if(radioSelection.equals(AUtils.RADIO_SELECTED_HP)){
            areaType = AUtils.HP_AREA_TYPE_ID;
        }
        return areaType;
    }

    private Boolean isAutoCompleteValid(AutoCompleteTextView autoCompleteTextView, HashMap<String, String> hashMap){
        try {
            return hashMap.containsKey(autoCompleteTextView.getText().toString().toLowerCase());
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if(!isScanQr){
            scanQR();
        }else {
            super.onBackPressed();
        }
    }
}
