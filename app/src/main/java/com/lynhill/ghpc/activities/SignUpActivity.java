package com.lynhill.ghpc.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.lynhill.ghpc.R;

public class SignUpActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void Signup_click(View view) {
        Toast.makeText(this, "click here", Toast.LENGTH_SHORT).show();
    }
}