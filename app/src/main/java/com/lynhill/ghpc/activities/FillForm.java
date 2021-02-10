package com.lynhill.ghpc.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.lynhill.ghpc.BuildConfig;
import com.lynhill.ghpc.R;
import com.lynhill.ghpc.adapter.SampleImageAdapter;
import com.lynhill.ghpc.pojo.Representatives;
import com.lynhill.ghpc.util.StorageManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.paperdb.Paper;

import static android.provider.CalendarContract.CalendarCache.URI;

public class FillForm extends BaseActivity {
    private static final int REQUEST_CAMERA_CODE = 214;
    private TextInputLayout userName, address, dob, email, phoneNumber;
    String stringName, stringAddress, stringDob, stringEmail, stringPhoneNumber;
    ImageView signature, signatureClikc;
    private String currentPhotoPath;
    private boolean haveSignature = false;
    private String TAG = FillForm.class.getSimpleName();
    private int requestPermissionID=769;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_form);
        findViews();
    }

    private void findViews() {
        signature = findViewById(R.id.signature);
        signatureClikc = findViewById(R.id.signature_click);
        userName = findViewById(R.id.username_til);
        address = findViewById(R.id.user_address_layout);
        dob = findViewById(R.id.user_dob_layout);
        email = findViewById(R.id.email_til);
        phoneNumber = findViewById(R.id.user_phone_number_layout);
        clickListenre();
        autoFillform();
        //        showDialog();
    }


//    onclick's
    public void backpress(View view){
        finish();
    }


    private void clickListenre() {
        signatureClikc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               checkPermissions();
            }
        });
    }

    public void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            dispatchTakePictureIntent();
        } else {
            requestStoragePermission();
        }
    }

    public void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, requestPermissionID);
    }

    private void dispatchTakePictureIntent() {
        Log.e(TAG, "dispatchTakePictureIntent: ");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImagevideoFile();
                Log.e(TAG, "dispatchTakePictureIntent: createImageFile-> " + photoFile.getAbsolutePath().toString());
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e(TAG, "dispatchTakePictureIntent: " + ex.toString());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA_CODE);
            } else {
                Log.e(TAG, "dispatchTakePictureIntent: phohtofilesave photo ");
            }
        } else {
            Log.e(TAG, "dispatchTakePictureIntent: else part");
        }
    }

    private File createImagevideoFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File filename;
        imageFileName = "JPEG_" + timeStamp + "_";
        filename = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = filename.getAbsolutePath();
//        } else {
//            imageFileName = "VID_" + timeStamp + "_";
//            filename = File.createTempFile(
//                    imageFileName,  /* prefix */
//                    ".mp4",         /* suffix */
//                    storageDir      /* directory */
//            );
//            currentVideoPath = filename.getAbsolutePath();
//        }
        return filename;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CAMERA_CODE) {
            signature.setImageURI(Uri.parse(currentPhotoPath));
            haveSignature = true;
        }
    }

    private void autoFillform() {
        StorageManager storageManager = StorageManager.getInstance(this);
        if (storageManager != null) {
            if (!TextUtils.isEmpty(storageManager.getUserName()))
                userName.getEditText().setText(storageManager.getUserName());
            if (!TextUtils.isEmpty(storageManager.getUserAddress()))
                address.getEditText().setText(storageManager.getUserAddress());
            if (!TextUtils.isEmpty(storageManager.getUserDOB()))
                dob.getEditText().setText(storageManager.getUserDOB());
            if (!TextUtils.isEmpty(storageManager.getUserEmail()))
                email.getEditText().setText(storageManager.getUserEmail());
            if (!TextUtils.isEmpty(storageManager.getUserPhoneNumber()))
                phoneNumber.getEditText().setText(storageManager.getUserPhoneNumber());

        }
    }


    public void Signup_click(View view) {
        ArrayList<Representatives>representatives= new ArrayList<>();
        Representatives representatives1 = new Representatives();
        StorageManager storageManager = StorageManager.getInstance(this);
        if (!TextUtils.isEmpty(userName.getEditText().getText().toString())) {
            stringName = userName.getEditText().getText().toString();
            userName.setErrorEnabled(false);
              representatives1.setName(stringName);
        } else {
            userName.setError("Enter your user name.");
            return;
        }
        if (!TextUtils.isEmpty(address.getEditText().getText().toString())) {
            stringAddress = address.getEditText().getText().toString();
            address.setErrorEnabled(false);
            representatives1.setAddress(stringAddress);
        } else {
            address.setError("Enter your address.");
            return;
        }
        if (!TextUtils.isEmpty(dob.getEditText().getText().toString())) {
            stringDob = dob.getEditText().getText().toString();
            dob.setErrorEnabled(false);
            representatives1.setDob(stringDob);
        } else {
            dob.setError("Enter your date of birth.");
            return;
        }

        if (!TextUtils.isEmpty(email.getEditText().getText().toString())) {
            if (isEmailValid(email.getEditText().getText().toString())) {
                stringEmail = email.getEditText().getText().toString();
                email.setErrorEnabled(false);
                representatives1.setEmali(stringEmail);
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
                representatives1.setPhoneNumber(stringPhoneNumber);
            }
        } else {
            phoneNumber.setError("Enter your phone number.");
            return;
        }
        if (!haveSignature) {
            Toast.makeText(this, "Please upload your signature.", Toast.LENGTH_SHORT).show();
            return;
        }else{
            storageManager.setUserSignature(currentPhotoPath);
            representatives1.setSignature(currentPhotoPath);
        }

        uploadProcess();
    }

    private void uploadProcess() {
//        Toast.makeText(this, "toast", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, SampleImagesActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
    }

    private boolean isEmailValid(String email) {
        Pattern pattern;
        Matcher matcher;
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        Log.e(TAG, "onRequestPermissionsResult: " + requestCode + "  ==   " + requestPermissionID);
        if (grantResults.length > 0 && requestCode == requestPermissionID) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
                // Do_SOme_Operation();
//                Toast.makeText(this, " granted", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            finish();
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }
}