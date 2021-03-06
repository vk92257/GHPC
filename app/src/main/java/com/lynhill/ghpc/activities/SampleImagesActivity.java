package com.lynhill.ghpc.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.lynhill.ghpc.BuildConfig;
import com.lynhill.ghpc.R;
import com.lynhill.ghpc.adapter.SampleImageAdapter;
import com.lynhill.ghpc.pojo.Representatives;
import com.lynhill.ghpc.util.Constants;
import com.lynhill.ghpc.util.StorageManager;
import com.lynhill.ghpc.util.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import io.paperdb.Paper;

public class SampleImagesActivity extends BaseActivity {
    private static final String TAG = SampleImagesActivity.class.getSimpleName();
    private static final int REQUEST_CAMERA_CODE = 250;
    private RecyclerView sampleImages;
    private String currentPhotoPath;
    private SampleImageAdapter sampleImageAdapter;
    private ArrayList<String> sampleImagesList = new ArrayList<>();
    private int numberOfColumns = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_images);
        sampleImages = findViewById(R.id.sample_image_rv);
        setUpRv();
        storedSampleImages();
    }

    private void storedSampleImages() {

//        int position = StorageManager.getInstance(this).getCurrentUser();
        int position = getIntent().getIntExtra("position",-1);
        Log.e(TAG, "storedSampleImages: "+position );
        if (position != -1) {
            Representatives representatives = Utils.getArrayList(this, Constants.REPRESENTATIVE_LIST).get(position);
            Log.e(TAG, "storedSampleImages: "+sampleImagesList.size() );
            sampleImagesList.addAll(representatives.getSampleImages());
            sampleImageAdapter.notifyDataSetChanged();

        }
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


    //    onclick
    public void backpress(View view) {
        finish();
    }


    private File createImagevideoFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File filename;
        imageFileName = "." + StorageManager.getInstance(this).getUserName() + timeStamp + "_";
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
            sampleImagesList.add(currentPhotoPath);
            sampleImageAdapter.notifyDataSetChanged();
        }
    }

    private void setUpRv() {

//        sampleImages.setLayoutManager(new LinearLayoutManager(this));
        sampleImages.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
//        sampleImages.setLayoutManager(new GridLayoutManager(this,2));
        sampleImageAdapter = new SampleImageAdapter(sampleImagesList, this);
        sampleImages.setAdapter(sampleImageAdapter);
    }

    public void addEmployee(View view) {
        dispatchTakePictureIntent();
    }


    public void upload(View view) {
        ArrayList<Representatives> list = null;
        Representatives rep = new Representatives();
        StorageManager st = StorageManager.getInstance(this);
        if (!TextUtils.isEmpty(st.getUserName())) {
            rep.setName(st.getUserName());

        } else {
            Log.e(TAG, "upload: error in username ");
        }
        if (!TextUtils.isEmpty(st.getUserAddress())) {
            rep.setAddress(st.getUserAddress());
            Log.e(TAG, "upload: " + st.getUserAddress());
        } else {
            Log.e(TAG, "upload: error in address ");
        }
        if (!TextUtils.isEmpty(st.getUserDOB())) {
            rep.setDob(st.getUserDOB());
        } else {
            Log.e(TAG, "upload: error in dob ");
        }
        if (Utils.getStringArrayList(this, Constants.PAPER_EMAIL) != null) {
            rep.setEmali(Utils.getStringArrayList(this, Constants.PAPER_EMAIL));
        } else {
            Log.e(TAG, "upload: error in email ");
        }
        if (Utils.getStringArrayList(this, Constants.PAPER_EMAIL) != null) {
            rep.setPhoneNumber(Utils.getStringArrayList(this, Constants.PAPER_CONTACT));
        } else {
            Log.e(TAG, "upload: error in phone number ");
        }
        if (!TextUtils.isEmpty(st.getUserSignature())) {
            rep.setSignature(st.getUserSignature());
        } else {
            Log.e(TAG, "upload: error in signature ");
        }
        if (sampleImagesList != null) {
            rep.setSampleImages(sampleImagesList);
        } else {
            Log.e(TAG, "upload: error in sample image  " + list.size());
        }

        if (Utils.getStringArrayList(this, "rep") != null) {
            list = Utils.getArrayList(SampleImagesActivity.this, "rep");
            Log.e(TAG, "upload: before list is there  " + list.size());
        } else {
            list = new ArrayList<>();
        }

        rep.setProject(StorageManager.getInstance(this).getUserProject());
        list.add(rep);
        Utils.saveArrayList(SampleImagesActivity.this, list, "rep");
        Log.e(TAG, "upload: after list is there  " + Utils.getArrayList(SampleImagesActivity.this, "rep").size());
        Intent intent = new Intent(this, MainDashBoard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

//    public void upload(View view) {
//        ArrayList<Representatives> list = null;
//        Representatives rep = new Representatives();
//        StorageManager st = StorageManager.getInstance(this);
//        if (!TextUtils.isEmpty(st.getUserName())) {
//            rep.setName(st.getUserName());
//            Log.e(TAG, "upload: " + st.getUserName());
//        } else {
//            Log.e(TAG, "upload: error in username ");
//        }
//        if (!TextUtils.isEmpty(st.getUserAddress())) {
//            rep.setAddress(st.getUserAddress());
//        } else {
//            Log.e(TAG, "upload: error in address ");
//        }
//        if (!TextUtils.isEmpty(st.getUserDOB())) {
//            rep.setDob(st.getUserDOB());
//        } else {
//            Log.e(TAG, "upload: error in dob ");
//        }
////        if (!TextUtils.isEmpty(st.getUserEmail())) {
////            rep.setEmali(st.getUserEmail());
////        } else {
////            Log.e(TAG, "upload: error in email ");
////        }
////        if (!TextUtils.isEmpty(st.getUserPhoneNumber())) {
////            rep.setPhoneNumber(st.getUserPhoneNumber());
////        } else {
////            Log.e(TAG, "upload: error in phone number ");
////        }
//        if (!TextUtils.isEmpty(st.getUserSignature())) {
//            rep.setSignature(st.getUserSignature());
//        } else {
//            Log.e(TAG, "upload: error in signature ");
//        }
//        if (sampleImagesList != null) {
//            rep.setSampleImages(sampleImagesList);
//        } else {
//            Log.e(TAG, "upload: error in sample image  ");
//        }
////        if (Paper.book().read("rep") != null) {
////            list = Paper.book().read("rep");
////            list.add(rep);
////            Log.e(TAG, "upload: list is there  ");
////        } else {
////            list = new ArrayList<>();
////            list.add(rep);
////            Log.e(TAG, "upload: list is not there");
////
////        }
//            rep.setPhoneNumber(Paper.book().read(Constants.PAPER_CONTACT));
//            rep.setEmali(Paper.book().read(Constants.PAPER_EMAIL));
////        if ()
//
//        Paper.book().write("rep", list);
//        Intent intent = new Intent(this, MainDashBoard.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        finish();
//        list = Utils.getArrayList(SampleImagesActivity.this, "rep");
//        list.add(rep);
//        Log.e(TAG, "upload: list is there  ");
//        Utils.saveArrayList(SampleImagesActivity.this, list, "rep");
//    }
}