package com.lynhill.ghpc.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.lynhill.ghpc.R;

public class Representative_detail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_representative_detail);
    }

//    onclick's
    public void backpress(View view){
        finish();
    }
}