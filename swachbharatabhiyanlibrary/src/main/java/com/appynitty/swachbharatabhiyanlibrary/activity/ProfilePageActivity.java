package com.appynitty.swachbharatabhiyanlibrary.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.appynitty.swachbharatabhiyanlibrary.R;

import java.util.Objects;

public class ProfilePageActivity extends AppCompatActivity {

    Toolbar toolbar;

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
        toolbar = findViewById(R.id.toolbar);

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
    }

    private void initData() {
    }
}