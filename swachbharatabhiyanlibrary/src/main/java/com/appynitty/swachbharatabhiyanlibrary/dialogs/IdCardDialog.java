package com.appynitty.swachbharatabhiyanlibrary.dialogs;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.custom_component.GlideCircleTransformation;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.bumptech.glide.Glide;

import java.util.HashMap;

public class IdCardDialog extends Dialog {

    public Dialog d;

    private Context mContext;
    private String mUserName, mEmpID, mProfileUrl;

    private TextView lblUserName, lblEmpId;

    private ImageView imgProfilePic;

    private ImageView imgClose;

    public IdCardDialog(Context context, String userName, String empId, String profileUrl) {
        super(context);

        mContext = context;
        mUserName = userName;
        mEmpID = empId;
        mProfileUrl = profileUrl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_id_card);
        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.height = 959;


        this.getWindow().setAttributes(params);

        initComponents();
    }

    private void initComponents() {

        generateID();
        registerEvents();
        initData();
    }

    private void generateID() {

        lblUserName = findViewById(R.id.user_name);
        lblEmpId = findViewById(R.id.emp_id);

        imgProfilePic = findViewById(R.id.profile_pic);

        imgClose = findViewById(R.id.btn_close);
    }

    private void initData() {

        if(!AUtils.isNullString(mProfileUrl))
        {
            try{
                Glide.with(mContext).load(mProfileUrl)
                        .placeholder(R.drawable.ic_user)
                        .error(R.drawable.ic_user)
                        .centerCrop()
                        .bitmapTransform(new GlideCircleTransformation(mContext.getApplicationContext()))
                        .into(imgProfilePic);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        lblUserName.setText(mUserName);

        lblEmpId.setText(mEmpID);
    }

    private void registerEvents() {
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClose();
            }
        });

    }

    private void onClose()
    {
        this.dismiss();
    }
}
