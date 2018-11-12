package com.appynitty.swachbharatabhiyanlibrary.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.pojos.ImagePojo;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.utils.LocaleHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mithsoft.lib.activity.BaseActivity;
import com.mithsoft.lib.components.Toasty;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.Objects;

import quickutils.core.QuickUtils;

public class TakePhotoActivity extends BaseActivity {

    private final static String TAG = "TakePhotoActivity";
    private static final int REQUEST_CAMERA = 22;
    private static final int SELECT_FILE = 33;


    private Context mContext;
    private Toolbar toolbar;

    private EditText comments;
    private ImageView beforeImage;
    private ImageView afterImage;
    private CardView openQR;

    private int imageViewNo = 0;

    private String resumeFilePath = "";
    private String beforeImageFilePath = "";
    private String afterImageFilePath = "";

    private ImagePojo imagePojo;

    @Override
    protected void attachBaseContext(Context newBase) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            super.attachBaseContext(LocaleHelper.onAttach(newBase));
        } else {
            super.attachBaseContext(newBase);
        }
    }

    @Override
    protected void generateId() {

        setContentView(R.layout.activity_take_photo);
        toolbar = findViewById(R.id.toolbar);

        mContext = TakePhotoActivity.this;
        AUtils.mCurrentContext = mContext;

        beforeImage = findViewById(R.id.img_before_photo);
        afterImage = findViewById(R.id.img_after_photo);

        comments = findViewById(R.id.txt_comments);

        openQR = findViewById(R.id.open_qr);

        initToolbar();
    }

    @Override
    protected void registerEvents() {
        beforeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageViewNo = 1;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // Do something for marshmallow and above versions
                    isCameraPermissionGiven();
                } else {
                    // do something for phones running an SDK before marshmallow
                    checkGpsStatus();
                }
            }
        });

        afterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageViewNo = 2;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // Do something for marshmallow and above versions
                    isCameraPermissionGiven();
                } else {
                    // do something for phones running an SDK before marshmallow
                    checkGpsStatus();
                }
            }
        });

        openQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQRClicked();
            }
        });

        comments.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (comments.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });

    }

    @Override
    protected void initData() {

        Type type = new TypeToken<ImagePojo>() {
        }.getType();

        imagePojo = new Gson().fromJson(
                QuickUtils.prefs.getString(AUtils.PREFS.IMAGE_POJO , null), type);


        if (!AUtils.isNull(imagePojo)) {

            if(!AUtils.isNullString(imagePojo.getImage1())) {
                beforeImage.setImageURI(Uri.parse(imagePojo.getImage1()));
                beforeImageFilePath = imagePojo.getImage1();
            } else if(!AUtils.isNullString(imagePojo.getImage2())) {
                afterImage.setImageURI(Uri.parse(imagePojo.getImage2()));
                afterImageFilePath = imagePojo.getImage2();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {

//                onSelectFromGalleryResult(data);

            } else if (requestCode == REQUEST_CAMERA) {

                onCaptureImageResult(data);
            }
        }
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
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(TakePhotoActivity.this, Manifest.permission.CAMERA)) {

                AUtils.showPermissionDialog(mContext, "CAMERA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, AUtils.MY_PERMISSIONS_REQUEST_CAMERA);
                        }
                    }
                });
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(TakePhotoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

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
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(TakePhotoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

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
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(TakePhotoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void openQRClicked() {

        if(validateForm()) {

           if(getFormData()) {

               startActivity(new Intent(TakePhotoActivity.this,
                       QRcodeScannerActivity.class).putExtra(AUtils.REQUEST_CODE, AUtils.MY_RESULT_REQUEST_QR));
               TakePhotoActivity.this.finish();
           }
        }
    }

    private void initToolbar() {
        toolbar.setTitle(getResources().getString(R.string.title_activity_take_photo));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
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

        switch (imageViewNo) {

            case 1:
                beforeImage.setImageBitmap(thumbnail);
                beforeImageFilePath = destination.getAbsolutePath();
                break;
            case 2:
                afterImage.setImageBitmap(thumbnail);
                afterImageFilePath = destination.getAbsolutePath();
                break;
        }

    }

    private boolean validateForm() {

        if (AUtils.isNullString(beforeImageFilePath) && AUtils.isNullString(afterImageFilePath)) {
            Toasty.warning(mContext, mContext.getString(R.string.plz_capture_img), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean getFormData() {

        imagePojo = new ImagePojo();

        if (!AUtils.isNullString(beforeImageFilePath)) {
            imagePojo.setImage1(beforeImageFilePath);
            imagePojo.setBeforeImage("Before");
        }
        if (!AUtils.isNullString(afterImageFilePath)) {
            imagePojo.setImage2(afterImageFilePath);
            imagePojo.setAfterImage("After");
        }

        if (!AUtils.isNullString(comments.getText().toString())) {
            imagePojo.setComment(comments.getText().toString());
        }

        if (!AUtils.isNull(imagePojo)) {

            Type type = new TypeToken<ImagePojo>() {
            }.getType();
            QuickUtils.prefs.save(AUtils.PREFS.IMAGE_POJO, new Gson().toJson(imagePojo, type));

            return true;
        }
        else {
            return false;
        }
    }
}
