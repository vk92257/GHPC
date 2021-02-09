package com.lynhill.ghpc.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Line;
import com.google.android.material.textfield.TextInputLayout;
import com.lynhill.ghpc.BuildConfig;
import com.lynhill.ghpc.R;
import com.lynhill.ghpc.adapter.AddMoreAdapter;
import com.lynhill.ghpc.adapter.SampleImageAdapter;
import com.lynhill.ghpc.pojo.Representatives;
import com.lynhill.ghpc.util.Constants;
import com.lynhill.ghpc.util.StorageManager;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.paperdb.Paper;

import static android.provider.CalendarContract.CalendarCache.URI;

public class FillForm extends AppCompatActivity {
    private static final int REQUEST_CAMERA_CODE = 214;
    private TextInputLayout userName, address, dob, email, phoneNumber;
    String stringName, stringAddress, stringDob, stringEmail, stringPhoneNumber;
    ImageView signature, signatureClikc, addPhoneNumber, addEmail;
    private String currentPhotoPath;
    private boolean haveSignature = false;
    ArrayList<String> phoneArrayList = new ArrayList<>();
    ArrayList<String> emailArrayList = new ArrayList<>();
    AddMoreAdapter phoneAdapter;
    AddMoreAdapter emailAdapter;
    private RecyclerView emailList, phoneList;

    private String TAG = FillForm.class.getSimpleName();
    private int requestPermissionID = 769;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_form);
        findViews();
    }

    private void findViews() {
        addPhoneNumber = findViewById(R.id.add_phone);
        addEmail = findViewById(R.id.add_email);
        signature = findViewById(R.id.signature);
        signatureClikc = findViewById(R.id.signature_click);
        userName = findViewById(R.id.username_til);
        address = findViewById(R.id.user_address_layout);
        dob = findViewById(R.id.user_dob_layout);
        email = findViewById(R.id.email_til);
        phoneNumber = findViewById(R.id.user_phone_number_layout);
        emailList = findViewById(R.id.email_list);
        phoneList = findViewById(R.id.phone_list);
        clickListenre();
        autoFillform();
        if (phoneArrayList.isEmpty()) {
        }
        if (emailArrayList.isEmpty()) {
        }
        //        showDialog();
    }

    private void addingListView() {
        phoneList.setLayoutManager(new LinearLayoutManager(this));
        emailList.setLayoutManager(new LinearLayoutManager(this));
//    phoneAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, phoneArrayList);
        phoneAdapter = new AddMoreAdapter(phoneArrayList, this);
//    emailAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, emailArrayList);
        emailAdapter = new AddMoreAdapter(emailArrayList, this);
        phoneList.setAdapter(phoneAdapter);
        emailList.setAdapter(emailAdapter);
    }

    private void clickListenre() {
        addPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneArrayList.add(phoneNumber.getEditText().getText().toString());
                phoneNumber.getEditText().setText("");
                phoneAdapter.notifyDataSetChanged();
            }
        });
        addEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEmailValid(email.getEditText().getText().toString())) {
                    emailArrayList.add(email.getEditText().getText().toString());
                    email.getEditText().setText("");
                    emailAdapter.notifyDataSetChanged();
                } else {
                    email.setError("Email is invalid");
                }
            }
        });
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
        imageFileName = "."+StorageManager.getInstance(this).getUserName()+ timeStamp + "_";
        filename = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = filename.getAbsolutePath();
        uri = Uri.fromFile(filename);

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
//            signature.setImageURI(Uri.parse(currentPhotoPath));
            Log.e(TAG, "onActivityResult: uri " + uri + " \n path" + currentPhotoPath);

            CropImage.activity(uri)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                signature.setImageURI(resultUri);
                haveSignature = true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
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
//            if (!TextUtils.isEmpty(storageManager.getUserEmail()))
//                email.getEditText().setText(storageManager.getUserEmail());
//            if (!TextUtils.isEmpty(storageManager.getUserPhoneNumber()))
//                phoneNumber.getEditText().setText(storageManager.getUserPhoneNumber());
            emailArrayList = Paper.book().read(Constants.PAPER_EMAIL);
            phoneArrayList = Paper.book().read(Constants.PAPER_CONTACT);
            if (!emailArrayList.isEmpty() && !phoneArrayList.isEmpty()) {
                Log.e(TAG, "autoFillform: " + emailArrayList.size());
                Log.e(TAG, "autoFillform: " + phoneArrayList.size());
                addingListView();
            }
        }
    }


    public void Signup_click(View view) {
        ArrayList<Representatives> representatives = new ArrayList<>();
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
        if (!TextUtils.isEmpty(email.getEditText().getText().toString()) || !emailArrayList.isEmpty()) {
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
                email.setErrorEnabled(false);
            }


        } else {
            email.setError("Enter your e-mail.");
            return;
        }

        if (!TextUtils.isEmpty(phoneNumber.getEditText().getText().toString()) || !phoneArrayList.isEmpty()) {
            if (!TextUtils.isEmpty(phoneNumber.getEditText().getText().toString())) {
                if (phoneNumber.getEditText().getText().length() < 10) {
                    phoneNumber.setError("enter a valid phone number");
                } else {
                    stringPhoneNumber = phoneNumber.getEditText().getText().toString();
                    phoneNumber.setErrorEnabled(false);
                    storageManager.setUserPhoneNumber(stringPhoneNumber);
                }
            } else {

            }
        } else {
            phoneNumber.setError("Enter your phone number.");
            return;
        }
        if (!haveSignature) {
            Toast.makeText(this, "Please upload your signature.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            storageManager.setUserSignature(currentPhotoPath);
            representatives1.setSignature(currentPhotoPath);
        }

        if (!emailArrayList.isEmpty() && !phoneArrayList.isEmpty()) {
            Paper.book().write(Constants.PAPER_EMAIL, emailArrayList);
            Paper.book().write(Constants.PAPER_CONTACT, phoneArrayList);
        }
        uploadProcess();
    }

    private void uploadProcess() {
//        Toast.makeText(this, "toast", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, SampleImagesActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
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