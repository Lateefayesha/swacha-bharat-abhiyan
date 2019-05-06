package com.appynitty.swachbharatabhiyanlibrary.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.utils.LocaleHelper;
import com.mithsoft.lib.components.Toasty;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Objects;

public class DumpYardWeightActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 22;

    private Context mContext;
    private Toolbar toolbar;
    private String dumpYardId;
    private Intent intent;
    private Button btnSubmitDumpDetails;

    private TextView textDumpYardId;
    private EditText editDryTotal, editWetTotal, editTotal;
    private ImageButton btnTakeDryPhoto, btnTakeWetPhoto;
    RadioButton radioButtonDryKg, radioButtonWetKg, radioButtonDryTon, radioButtonWetTon;
    RadioGroup radioGroupDry, radioGroupWet;

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

        textDumpYardId = findViewById(R.id.txt_house_id);
        editTotal = findViewById(R.id.txt_total_weight);
        editDryTotal = findViewById(R.id.txt_dry_weight);
        editWetTotal = findViewById(R.id.txt_wet_weight);

        radioGroupDry = findViewById(R.id.radio_dry_weight);
        radioGroupWet = findViewById(R.id.radio_wet_weight);

        radioButtonDryKg = findViewById(R.id.radio_dry_kg);
        radioButtonDryTon = findViewById(R.id.radio_dry_ton);
        radioButtonWetKg = findViewById(R.id.radio_wet_kg);
        radioButtonWetTon = findViewById(R.id.radio_wet_ton);

        btnSubmitDumpDetails = findViewById(R.id.btn_submit_dump);
        btnTakeDryPhoto = findViewById(R.id.btn_take_dry_photo);
        btnTakeWetPhoto = findViewById(R.id.btn_take_wet_photo);
    }

    private void registerEvents(){

        editDryTotal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculateTotalWeight();
            }
        });

        editWetTotal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculateTotalWeight();
            }
        });

        radioGroupDry.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                calculateTotalWeight();
            }
        });

        radioGroupWet.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                calculateTotalWeight();
            }
        });

        btnSubmitDumpDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateForm()){
                    submitDumpDetailData();
                }
            }
        });

        btnTakeDryPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // Do something for marshmallow and above versions
                    isCameraPermissionGiven();
                } else {
                    // do something for phones running an SDK before marshmallow
                    checkGpsStatus();
                }
            }
        });

        btnTakeWetPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // Do something for marshmallow and above versions
                    isCameraPermissionGiven();
                } else {
                    // do something for phones running an SDK before marshmallow
                    checkGpsStatus();
                }
            }
        });
    }

    private void isCameraPermissionGiven() {
        if (AUtils.isCameraPermissionGiven(mContext)) {
            //You already have the permission, just go ahead.
            isStoragePermissionGiven();
        }
    }

    private void isStoragePermissionGiven() {
        if(AUtils.isStoragePermissionGiven(mContext)) {
            //You already have the permission, just go ahead.
            isLocationPermissionGiven();
        }
    }

    private void isLocationPermissionGiven() {
        if (AUtils.isLocationPermissionGiven(mContext)) {
            //You already have the permission, just go ahead.
            checkGpsStatus();
        }
    }

    private void checkGpsStatus() {

        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        boolean GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (GpsStatus) {

            takePhotoImageViewOnClick();
        } else {

            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }

    private void takePhotoImageViewOnClick() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == AUtils.MY_PERMISSIONS_REQUEST_CAMERA) {
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
                isStoragePermissionGiven();
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
            } else if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                AUtils.showPermissionDialog(mContext, "EXTERNAL STORAGE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, AUtils.MY_PERMISSIONS_REQUEST_CAMERA);
                        }
                    }
                });
            } else {
//                Toast.makeText(getActivity(), "Unable to get Permission", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == AUtils.MY_PERMISSIONS_REQUEST_STORAGE) {
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
                isLocationPermissionGiven();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                AUtils.showPermissionDialog(mContext, "EXTERNAL STORAGE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, AUtils.MY_PERMISSIONS_REQUEST_STORAGE);
                        }
                    }
                });
            } else {
//                Toast.makeText(getActivity(), "Unable to get Permission", Toast.LENGTH_LONG).show();
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
                checkGpsStatus();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                AUtils.showPermissionDialog(mContext, "Location Service", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, AUtils.MY_PERMISSIONS_REQUEST_LOCATION);
                        }
                    }
                });
            } else {
//                Toast.makeText(getActivity(), "Unable to get Permission", Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }
        }
    }

    private void onCaptureImageResult(Intent data) {

        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        File destination = null;
        try {


            File dir = new File(Environment.getExternalStorageDirectory()
                    .toString() + "/Gram Panchayat");

            if (!dir.exists()) {
                dir.mkdirs();
            }
            destination = new File(dir, System.currentTimeMillis() + ".jpg");

            FileOutputStream fOut = new FileOutputStream(destination);
            thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fOut);

        } catch (Exception e) {

            e.printStackTrace();
            Toasty.error(mContext, "Unable to add image", Toast.LENGTH_SHORT).show();
        }

//        switch (imageViewNo) {
//
//            case 1:
//                beforeImage.setImageBitmap(thumbnail);
//                beforeImageFilePath = destination.getAbsolutePath();
//                break;
//            case 2:
//                afterImage.setImageBitmap(thumbnail);
//                afterImageFilePath = destination.getAbsolutePath();
//                break;
//        }

    }

    private void initData(){
        if(intent.hasExtra(AUtils.dumpYardId)){
            dumpYardId = intent.getStringExtra(AUtils.dumpYardId);
            textDumpYardId.setText(dumpYardId);
        }
    }

    private void calculateTotalWeight(){
        float total = 0f;

        float dryInTons = getDryWeightInTons();
        float wetInTons = getWetWeightInTons();

        total = dryInTons + wetInTons;

        editTotal.setText(String.valueOf(total));
    }

    private float getDryWeightInTons(){
        float returnValue = 0f;
        String dryWt = editDryTotal.getText().toString();
        if(!AUtils.isNull(dryWt)){
            if(radioButtonDryKg.isChecked()){
                returnValue = Float.parseFloat(dryWt) / 1000;
            }else if(radioButtonDryTon.isChecked()){
                returnValue = Float.parseFloat(dryWt);
            }
        }

        return returnValue;
    }

    private float getWetWeightInTons(){
        float returnValue = 0f;
        String wetWt = editWetTotal.getText().toString();
        if(!AUtils.isNull(wetWt)){
            if(radioButtonWetKg.isChecked()){
                returnValue = Float.parseFloat(wetWt) / 1000;
            }else if(radioButtonWetTon.isChecked()){
                returnValue = Float.parseFloat(wetWt);
            }
        }

        return returnValue;
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
        map.put(AUtils.DUMPDATA.weightTotalDry, String.valueOf(getDryWeightInTons()));
        map.put(AUtils.DUMPDATA.weightTotalWet, String.valueOf(getWetWeightInTons()));

        return map;
    }



}
