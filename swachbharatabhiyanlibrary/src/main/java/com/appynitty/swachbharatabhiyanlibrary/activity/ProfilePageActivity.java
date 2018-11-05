package com.appynitty.swachbharatabhiyanlibrary.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.custom_component.GlideCircleTransformation;
import com.appynitty.swachbharatabhiyanlibrary.adapters.connection.UserDetailAdapterClass;
import com.appynitty.swachbharatabhiyanlibrary.pojos.UserDetailPojo;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.appynitty.swachbharatabhiyanlibrary.utils.LocaleHelper;
import com.bumptech.glide.Glide;

import java.util.Objects;

public class ProfilePageActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView userName, empId, contactNo, bloodGroup, address;
    private ImageView profilePic;

    private UserDetailAdapterClass mAdapter;

    private UserDetailPojo userDetailPojo;

    private Context mContext;

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

    private void initComponents() {
        generateId();
        registerEvents();
        initData();
    }

    private void generateId() {
        setContentView(R.layout.activity_profile_page);

        mContext = ProfilePageActivity.this;
        AUtils.mCurrentContext = mContext;

        mAdapter = new UserDetailAdapterClass();

        toolbar = findViewById(R.id.toolbar);

        profilePic = findViewById(R.id.profile_profile_pic);

        userName = findViewById(R.id.profile_user_name);
        empId = findViewById(R.id.profile_emp_id);
        contactNo = findViewById(R.id.profile_contact_no);
        bloodGroup = findViewById(R.id.profile_blood_group);
        address = findViewById(R.id.profile_address);

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

        mAdapter.setUserDetailListener(new UserDetailAdapterClass.UserDetailListener() {
            @Override
            public void onSuccessCallBack() {

                userDetailPojo = mAdapter.getUserDetailPojo();

                loadData();
            }

            @Override
            public void onFailureCallBack() {

            }
        });
    }

    private void initData() {

        userDetailPojo = mAdapter.getUserDetailPojo();

        if(!AUtils.isNull(userDetailPojo))
        {
            loadData();

            mAdapter.getUserDetail();
        } else {
            mAdapter.getUserDetail();
        }
    }

    private void loadData()
    {

        if(!AUtils.isNullString(userDetailPojo.getProfileImage()))
        {
            try{
                Glide.with(mContext).load(userDetailPojo.getProfileImage())
                        .placeholder(R.drawable.ic_user_white)
                        .error(R.drawable.ic_user_white)
                        .bitmapTransform(new GlideCircleTransformation(getApplicationContext()))
                        .into(profilePic);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        userName.setText(userDetailPojo.getName());
        empId.setText(userDetailPojo.getUserId());
        contactNo.setText(userDetailPojo.getMobileNumber());
        address.setText(userDetailPojo.getAddress());
    }
}
