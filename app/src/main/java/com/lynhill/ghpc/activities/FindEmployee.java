package com.lynhill.ghpc.activities;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.lynhill.ghpc.R;
import com.lynhill.ghpc.adapter.UserDataAdapter;
import com.lynhill.ghpc.listener.UserInfoListener;
import com.lynhill.ghpc.util.StorageManager;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindEmployee extends BaseActivity implements UserInfoListener {
    private static final String TAG = FindEmployee.class.getSimpleName();
    private ImageView scanCode;
    private TextInputLayout userName, address, dob, email, phoneNumber;
    private int OCR_DATA_REQUEST = 675;
    private UserDataAdapter userDataAdapter;
    private ArrayList<String> strings;
    private ImageView fillName, fillAddress, fillDOB;
    private Dialog dialog;
    private TextView solor, roofing, hvac;
    private String project = "";
    String stringName, stringAddress, stringDob, stringEmail, stringPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_employee);
        findViews();
    }

    private void findViews() {
        scanCode = findViewById(R.id.scan_code);
        userName = findViewById(R.id.username_til);
        address = findViewById(R.id.user_address_layout);
        dob = findViewById(R.id.user_dob_layout);
        email = findViewById(R.id.email_til);
        phoneNumber = findViewById(R.id.user_phone_number_layout);
        fillName = findViewById(R.id.fill_user_name);
        fillAddress = findViewById(R.id.fill_user_address);
        fillDOB = findViewById(R.id.fill_user_dob);
        solor = findViewById(R.id.solar);
        hvac = findViewById(R.id.hvac);
        roofing = findViewById(R.id.roofing);
        clickListener();
//        showDialog();
    }


    private void clickListener() {
        // scan click listener on find views
        scanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FindEmployee.this, OCR.class);
                overridePendingTransition(R.anim.fadeout, R.anim.fadeout);
                startActivityForResult(intent, OCR_DATA_REQUEST);
            }
        });

        fillName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog(1);

            }
        });
        fillDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog(3);
            }
        });
        fillAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog(2);
            }
        });

        solor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                solor.setBackgroundColor(getResources().getColor(R.color.primary));
                roofing.setBackgroundColor(getResources().getColor(R.color.black));
                hvac.setBackgroundColor(getResources().getColor(R.color.black));
                project = solor.getText().toString();
//                roofing.setBackgroundColor(getResources().getColor(R.color.darkGray));
//                hvac.setBackgroundColor(getResources().getColor(R.color.darkGray));
            }
        });
        hvac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                solor.setBackgroundColor(getResources().getColor(R.color.black));
                roofing.setBackgroundColor(getResources().getColor(R.color.black));
                hvac.setBackgroundColor(getResources().getColor(R.color.primary));
                project = hvac.getText().toString();
//                solor.setBackgroundColor(getResources().getColor(R.color.darkGray));
//                roofing.setBackgroundColor(getResources().getColor(R.color.darkGray));
            }
        });
        roofing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                solor.setBackgroundColor(getResources().getColor(R.color.black));
                roofing.setBackgroundColor(getResources().getColor(R.color.primary));
                hvac.setBackgroundColor(getResources().getColor(R.color.black));
                project = roofing.getText().toString();
//                solor.setBackgroundColor(getResources().getColor(R.color.darkGray));
//                hvac .setBackgroundColor(getResources().getColor(R.color.darkGray));
            }
        });

    }

    public void Signup_click(View view) {
        StorageManager storageManager = StorageManager.getInstance(this);
        if (!TextUtils.isEmpty(userName.getEditText().getText().toString())) {
            stringName = userName.getEditText().getText().toString();
            userName.setErrorEnabled(false);
            storageManager.setUserName(stringName);
        } else {
            userName.setError("Enter your user name.");
            return;
        }
        if (!TextUtils.isEmpty(address.getEditText().getText().toString())) {
            stringAddress = address.getEditText().getText().toString();
            address.setErrorEnabled(false);
            storageManager.setUserAddress(stringAddress);
        } else {
            address.setError("Enter your address.");
            return;
        }
        if (!TextUtils.isEmpty(dob.getEditText().getText().toString())) {
            stringDob = dob.getEditText().getText().toString();
            dob.setErrorEnabled(false);
            storageManager.setUserPhoneDOB(stringDob);
        } else {
            dob.setError("Enter your date of birth.");
            return;
        }

        if (!TextUtils.isEmpty(email.getEditText().getText().toString())) {
            if (isEmailValid(email.getEditText().getText().toString())) {
                stringEmail = email.getEditText().getText().toString();
                email.setErrorEnabled(false);
                storageManager.setUserEmail(stringEmail);
            } else {
                email.setError("Enter your valid e-mail.");
                return;
            }

        } else {
            email.setError("Enter your e-mail.");
            return;
        }

        if (!TextUtils.isEmpty(phoneNumber.getEditText().getText().toString())) {
            if (phoneNumber.getEditText().getText().length() < 10) {
                phoneNumber.setError("enter a valid phone number");
            } else {
                stringPhoneNumber = phoneNumber.getEditText().getText().toString();
                phoneNumber.setErrorEnabled(false);
                storageManager.setUserPhoneNumber(stringPhoneNumber);
            }
        } else {
            phoneNumber.setError("Enter your phone number.");
            return;
        }
        if (TextUtils.isEmpty(project.trim())) {
            return;
        }
        startBankActivity();
    }

    private boolean isEmailValid(String email) {
        Pattern pattern;
        Matcher matcher;
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void startBankActivity() {
        Intent intent = new Intent(this, BankActivity.class);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        startActivity(intent);
    }

    public void showCustomDialog(int p) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.bottomsheetformenutl);
        RecyclerView recyclerView = dialog.findViewById(R.id.user_data);
        userDataAdapter = new UserDataAdapter(strings, this, p);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userDataAdapter.onPress(this::onClick);
        ImageView close = dialog.findViewById(R.id.close);
        recyclerView.setAdapter(userDataAdapter);
        recyclerView.setHasFixedSize(true);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.e(TAG, "onActivityResult: --> "+requestCode+"  == ");
        if (resultCode == RESULT_OK) {
//            Log.e(TAG, "onActivityResult: --> "+requestCode+"  == "+OCR_DATA_REQUEST);
            if (requestCode == OCR_DATA_REQUEST) {
                String code = data.getStringExtra("data");
                strings = data.getStringArrayListExtra("user_info");
                if (strings != null) {
                    showFillFormOption();
                    showCustomDialog(1);
                }
            }
        }
    }

    private void showFillFormOption() {
        fillName.setVisibility(View.VISIBLE);
        fillDOB.setVisibility(View.VISIBLE);
        fillAddress.setVisibility(View.VISIBLE);
    }

    private void hideFillFormOption() {
        fillName.setVisibility(View.GONE);
        fillDOB.setVisibility(View.GONE);
        fillAddress.setVisibility(View.GONE);
    }


    @Override
    public void onClick(int i, String info) {
        if (i == 1) {
            userName.getEditText().setText(info);
            Log.e(TAG, "onClick: " + i + " " + info);
            dialog.dismiss();
        } else if (i == 2) {
            address.getEditText().setText(info);
            Log.e(TAG, "onClick: " + i + " " + info);
            dialog.dismiss();
        } else if (i == 3) {
            Log.e(TAG, "onClick: " + i + " " + info);
            dob.getEditText().setText(info);
            dialog.dismiss();
        }

    }
}