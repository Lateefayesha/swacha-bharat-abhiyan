package com.appynitty.swachbharatabhiyan;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;


import android.view.WindowManager;

import com.appynitty.swachbharatabhiyanlibrary.activity.SplashScreenActivity;
import com.appynitty.swachbharatabhiyanlibrary.pojos.LanguagePojo;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Prefs.putString(AUtils.APP_ID, "1");
//        Prefs.putInt(AUtils.VERSION_CODE, BuildConfig.VERSION_CODE);
        Prefs.putInt(AUtils.VERSION_CODE, 23);
//        initLanguageList();

        startActivity(new Intent(MainActivity.this, SplashScreenActivity.class));
    }

    private void initLanguageList() {

        ArrayList<LanguagePojo> languagePojos = new ArrayList<>();

        LanguagePojo eng = new LanguagePojo();
        eng.setLanguage(AUtils.LanguageNameConstants.ENGLISH);
        eng.setLanguageId(AUtils.LanguageIDConstants.ENGLISH);
        languagePojos.add(eng);

        LanguagePojo mar = new LanguagePojo();
        mar.setLanguageId(AUtils.LanguageIDConstants.MARATHI);
        mar.setLanguage(AUtils.LanguageNameConstants.MARATHI);
        languagePojos.add(mar);

        LanguagePojo hi = new LanguagePojo();
        hi.setLanguageId(AUtils.LanguageIDConstants.HINDI);
        hi.setLanguage(AUtils.LanguageNameConstants.HINDI);
        languagePojos.add(hi);

        AUtils.setLanguagePojoList(languagePojos);

    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
