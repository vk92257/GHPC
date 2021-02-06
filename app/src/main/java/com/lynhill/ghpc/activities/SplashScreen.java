package com.lynhill.ghpc.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.lynhill.ghpc.R;
import com.lynhill.ghpc.util.App;

public class SplashScreen extends BaseActivity {
    private static final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    private ImageView splash_logo;
    private static final int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        initview();
        splashloading();
    }

    //initview
    private void initview() {
        splash_logo = findViewById(R.id.splash_logo);

        startAnimation();
    }


    //Splash screen animation
    private void startAnimation() {

        Animation splashAnim = AnimationUtils.loadAnimation(App.getInstance(), R.anim.fadein);
        splash_logo.setAnimation(splashAnim);
    }

    //checeking for login
    private void splashloading() {
        mainThreadHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                startActivity(new Intent(SplashScreen.this,MainDashBoard.class));
                finish();
            }
        },SPLASH_TIME_OUT);

    }


}