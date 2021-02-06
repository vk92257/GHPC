package com.lynhill.ghpc.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.lynhill.ghpc.R;

public class MainDashBoard extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dash_board);
    }

    /**
     * open new activity for adding new user
     * @param view
     */
    public void addEmployee(View view) {
        Intent  intent = new Intent(this,FindEmployee.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);

    }
}