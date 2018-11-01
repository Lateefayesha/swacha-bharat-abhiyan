package com.appynitty.swachbharatabhiyanlibrary.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.appynitty.swachbharatabhiyanlibrary.R;

import java.util.Objects;

public class HistoryPageActivity extends AppCompatActivity {

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

    private void registerEvents() {
    }

    private void initData() {
    }
}
