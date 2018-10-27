package com.appynitty.swachbharatabhiyanlibrary.utils;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Build;

import com.mithsoft.lib.utils.MsUtils;

import java.util.Locale;

import quickutils.core.QuickUtils;

public class AUtils extends MsUtils {

    //    Local URL
    public static final String SERVER_URL = "http://192.168.200.3:6077/";

    //    Staging URL
//    public static final String SERVER_URL = "http://115.115.153.117:6088/";

    //    Relese URL
//    public static final String SERVER_URL = "http://115.115.153.117:7055/";


    //    Genral Constant
    public static final String STATUS_SUCCESS = "success";

    public static final String STATUS_ERROR = "error";
    public static final String STATUS_EXPIRED = "expired";

    public static final String LANGUAGE_NAME = "LanguageName";

    public static final String DEFAULT_LANGUAGE_NAME = "mi";

    public static final String CONTENT_TYPE = "application/json";

    public static final String APP_ID = "AppId";


    public static final String DIALOG_TYPE_VEHICLE = "DialogTypeVehicle";
    public static final String DIALOG_TYPE_LANGUAGE = "Dialog_Type_Language";

    public static final String DEFAULT_LANGUAGE_ID = "2";
    public static final String LANGUAGE_ID = "LanguageId";

    public static final int SPLASH_SCREEN_TIME = 3000;


    // Language Change of an application
    public static void changeLanguage(Activity context, int languageId) {

        String languageStr = "";
        switch (languageId) {
            case 1:
                languageStr = "en";
                QuickUtils.prefs.save(AUtils.LANGUAGE_NAME,languageStr);
                break;
            case 2:
                languageStr = "mi";
                QuickUtils.prefs.save(AUtils.LANGUAGE_NAME,languageStr);
                break;
            case 3:
                languageStr = "hi";
                QuickUtils.prefs.save(AUtils.LANGUAGE_NAME,languageStr);
                break;
            case 4:
                languageStr = "gu";
                QuickUtils.prefs.save(AUtils.LANGUAGE_NAME,languageStr);
                break;
            case 5:
                languageStr = "pa";
                QuickUtils.prefs.save(AUtils.LANGUAGE_NAME,languageStr);
                break;
        }

        Locale locale = new Locale(languageStr);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Locale.setDefault(Locale.Category.DISPLAY, locale);
        } else {
            Locale.setDefault(locale);
        }
        Configuration config = new Configuration();
        config.locale = locale;
        context.getApplicationContext().getResources().updateConfiguration(config, null);
        context.onConfigurationChanged(config);
    }

    public interface PREFS {

        //    Save Data Constant
        String IS_USER_LOGIN = "UserLoginStatus";
        String USER_ID = "UserId";
        String USER_TYPE = "UserType";
    }

}
