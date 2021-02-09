package com.lynhill.ghpc.util;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import com.lynhill.ghpc.R;

import java.lang.reflect.Method;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.paperdb.Paper;

public class App extends Application {
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();

        Paper.init(this);
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        initApplication();
        set_Calligraphy();
    }

    /**
     * set font calligraphy
     */
    private void set_Calligraphy() {

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/OpenSans-Regular.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());
    }


    private void initApplication() {
        instance = this;
    }

    public static App getInstance() {
        return instance;
    }
}
