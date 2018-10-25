package com.appynitty.swachbharatabhiyanlibrary.activity;

import android.os.Bundle;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.adapters.InflateMenuAdapter;
import com.appynitty.swachbharatabhiyanlibrary.pojos.MenuListPojo;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;

import java.util.ArrayList;
import java.util.List;

import io.github.yavski.fabspeeddial.FabSpeedDial;

public class DashboardActivity extends AppCompatActivity {

    private FabSpeedDial fab;
    private GridView menuGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initComponents();
    }

    private void initComponents(){
        generateId();
        registerEvents();
        initData();
    }

    private void generateId() {

        fab = findViewById(R.id.fab_setting);
        menuGridView = findViewById(R.id.menu_grid);
    }

    private void registerEvents() {

        fab.setMenuListener(new FabSpeedDial.MenuListener() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return false;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                return false;
            }

            @Override
            public void onMenuClosed() {

            }
        });
    }

    private void initData() {
        List<MenuListPojo> menuPojoList = new ArrayList<MenuListPojo>();

        menuPojoList.add(new MenuListPojo("Test", R.drawable.ic_user));

        InflateMenuAdapter mainMenuAdaptor = new InflateMenuAdapter(DashboardActivity.this, menuPojoList);
        menuGridView.setAdapter(mainMenuAdaptor);
    }

}
