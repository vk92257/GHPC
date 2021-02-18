package com.lynhill.ghpc.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.lynhill.ghpc.BuildConfig;
import com.lynhill.ghpc.R;
import com.lynhill.ghpc.adapter.SampleImageAdapter;
import com.lynhill.ghpc.pojo.Representatives;
import com.lynhill.ghpc.util.APIConstans;
import com.lynhill.ghpc.util.ApiInterface;
import com.lynhill.ghpc.util.Constants;
import com.lynhill.ghpc.util.StorageManager;
import com.lynhill.ghpc.util.Utils;
import com.lynhill.ghpc.util.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;
import kotlin.io.TextStreamsKt;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SampleImagesActivity extends BaseActivity {
    private static final String TAG = SampleImagesActivity.class.getSimpleName();
    private static final int REQUEST_CAMERA_CODE = 250;
    private RecyclerView sampleImages;
    private String currentPhotoPath;
    private SampleImageAdapter sampleImageAdapter;
    private ArrayList<String> sampleImagesList = new ArrayList<>();
    private int numberOfColumns = 2;
    private StringRequest jobPostRequest;
    private Uri uri;
    private FrameLayout progressBarLayout;
    private TextView progressText;
    private static int currentPosition;
    private FirebaseStorage firebaseStorage;
    private ArrayList<String> tempImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_images);
        sampleImages = findViewById(R.id.sample_image_rv);
        progressBarLayout = findViewById(R.id.sample_Image_progress_layout);
        progressText = findViewById(R.id.sample_image_progress_text);
        firebaseStorage = FirebaseStorage.getInstance();
        setUpRv();
        storedSampleImages();
    }

    private void storedSampleImages() {

//        int position = StorageManager.getInstance(this).getCurrentUser();
        int position = getIntent().getIntExtra("position", -1);
        Log.e(TAG, "storedSampleImages: " + position);
        if (position != -1) {
            Representatives representatives = Utils.getArrayList(this, Constants.REPRESENTATIVE_LIST).get(position);
            Log.e(TAG, "storedSampleImages: " + sampleImagesList.size());
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

//    public void showCustomDialog() {
//        final String[] status = {""};
//        Dialog dialog1 = new Dialog(this);
//        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog1.setCancelable(false);
//        dialog1.setContentView(R.layout.alert_dialog);
//        TextView success = dialog1.findViewById(R.id.success);
//        TextView pending = dialog1.findViewById(R.id.pending);
//        TextView fail = dialog1.findViewById(R.id.fail);
//        TextView next = dialog1.findViewById(R.id.next);
//
//        success.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                pending.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_black)));
//                fail.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_black)));
//                success.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primary)));
//                status[0] = success.getText().toString();
//                next.setEnabled(true);
//                next.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primary)));
//
//            }
//        });
//        pending.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                pending.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange)));
//                success.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_black)));
//                fail.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_black)));
//                status[0] = pending.getText().toString();
//                next.setEnabled(true);
//                next.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primary)));
//            }
//        });
//        fail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                pending.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_black)));
//                success.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_black)));
//                fail.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
//                status[0] = fail.getText().toString();
//                next.setEnabled(true);
//                next.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primary)));
//            }
//        });
//        next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                StorageManager.getInstance(AddNewClient.this).setLoanStatus(status[0]);
//                Log.e(TAG, "onClick: " + StorageManager.getInstance(AddNewClient.this).getLoanStatus());
//                dialog1.dismiss();
//                startBankActivity2();
//            }
//        });
//
//        dialog1.show();
//    }

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
            sampleImagesList.add(uri.toString());
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
//            Log.e(TAG, "upload: error in username ");
        }
        if (!TextUtils.isEmpty(st.getUserAddress())) {
            rep.setAddress(st.getUserAddress());
//            Log.e(TAG, "upload: " + st.getUserAddress());
        } else {
//            Log.e(TAG, "upload: error in address ");
        }
        if (!TextUtils.isEmpty(st.getUserDOB())) {
            rep.setDob(st.getUserDOB());
        } else {
//            Log.e(TAG, "upload: error in dob ");
        }
        if (Utils.getStringArrayList(this, Constants.PAPER_EMAIL) != null) {
            rep.setEmali(Utils.getStringArrayList(this, Constants.PAPER_EMAIL));
        } else {
//            Log.e(TAG, "upload: error in email ");
        }
        if (Utils.getStringArrayList(this, Constants.PAPER_EMAIL) != null) {
            rep.setPhoneNumber(Utils.getStringArrayList(this, Constants.PAPER_CONTACT));
        } else {
//            Log.e(TAG, "upload: error in phone number ");
        }
        if (!TextUtils.isEmpty(st.getUserSignature())) {
            rep.setSignature(st.getUserSignature());
        } else {
//            Log.e(TAG, "upload: error in signature ");
        }
        if (sampleImagesList != null) {
            if (sampleImagesList.size() == 0) {
                Toast.makeText(this, "Please add site images.", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.e(TAG, "Compare " + sampleImagesList.size());
            currentPosition = sampleImagesList.size();
            rep.setSampleImages(sampleImagesList);
        } else {
            return;
        }

        if (Utils.getStringArrayList(this, "rep") != null) {
            list = Utils.getArrayList(SampleImagesActivity.this, "rep");
//            Log.e(TAG, "upload: before list is there  " + list.size());
        } else {
            list = new ArrayList<>();
        }
        rep.setProject(StorageManager.getInstance(this).getUserProject());
        if (getIntent().getIntExtra("position", -1) == -1) {
//            list.add(rep);
            for (int i = 0; i < rep.getSampleImages().size(); i++) {
                firebaseUrlConverter(Uri.parse(rep.getSampleImages().get(i)), list, rep);
            }
        } else {
            list.set(getIntent().getIntExtra("position", -1), rep);
            Utils.saveArrayList(SampleImagesActivity.this, list, "rep");
            Intent intent = new Intent(SampleImagesActivity.this, MainDashBoard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }


    }

    public static final Bitmap getBitmap(ContentResolver cr, Uri url)
            throws FileNotFoundException, IOException {
        InputStream input = cr.openInputStream(url);
        Bitmap bitmap = BitmapFactory.decodeStream(input);
        input.close();
        return bitmap;
    }

    private void firebaseUrlConverter(Uri imageuri, ArrayList<Representatives> list, Representatives rep) {
        progressBarLayout.setVisibility(View.VISIBLE);
//        final int[] comparePostition = {1};
        tempImages = new ArrayList<>();

        StorageReference storageReference = firebaseStorage.getReference();
        ContentResolver contentResolver = this.getContentResolver();
        Bitmap originalImage = null;
//        try {
//            originalImage = getBitmap(contentResolver, imageuri);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try {
            originalImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageuri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        assert originalImage != null;
        originalImage.compress(Bitmap.CompressFormat.JPEG, 10, outputStream);
        byte[] data = outputStream.toByteArray();
        Calendar calendar = Calendar.getInstance();
        //Returns current time in millis
        long timeMilli2 = calendar.getTimeInMillis();
        StorageReference reference = storageReference.child("profile")
                .child("image_" + String.valueOf(timeMilli2) + ".jpg");
        reference.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "onSuccess: second");
                Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        tempImages.add(uri.toString());
                        Log.e(TAG, "compare  --> " + currentPosition + " <> " + tempImages.size());
//                        comparePostition[0]++;
                        progressText.setText("Please wait while uploading your images(" + tempImages.size() + " of " + sampleImageAdapter.getItemCount() + ")");
                        if (tempImages.size() == sampleImageAdapter.getItemCount())
                            fastAndroidNtworking(list, rep);
                        Log.e(TAG, "onSuccess: " + uri.toString());
//                        storedata(uri.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBarLayout.setVisibility(View.INVISIBLE);
                        Log.d(TAG, "onFailure: " + e.getMessage());
                    }
                })
                ;
//                imageuri = downloadUrl.toString();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBarLayout.setVisibility(View.INVISIBLE);
                Log.d(TAG, "onFailure: " + e.getMessage());
            }
        });
    }

    private void fastAndroidNtworking(ArrayList<Representatives> list, Representatives rep) {
        if (tempImages != null) {
            rep.setSampleImages(tempImages);
        }
        progressBarLayout.setVisibility(View.VISIBLE);
        OkHttpClient okClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public okhttp3.Response intercept(Chain chain) throws IOException {
                                okhttp3.Request original = chain.request();

                                // Request customization: add request headers
                                okhttp3.Request.Builder requestBuilder = original.newBuilder()
                                        .header("Authorization", "bearer" + getResources().getString(R.string.jobnimbus_token))
                                        .header("Content-Type", "application/json")
                                        .method(original.method(), original.body());

                                okhttp3.Request request = requestBuilder.build();
                                return chain.proceed(request);
                            }
                        })
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIConstans.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okClient)
                .build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("name", rep.getProject());
            requestBody.put("status_name", "Lead");
            requestBody.put("country_name", "India");
            requestBody.put("sales_rep_name", rep.getName());
            requestBody.put("jnid", rep.getName());
            requestBody.put("created_by_name", rep.getName());
//            requestBody.put("address_line1", rep.getAddress());
            requestBody.put("address_line2", rep.getAddress());
//            Log.e(TAG, "fastAndroidNtworking: " + rep.getSampleImages());
            requestBody.put("image_id", rep.getSampleImages());
//            requestBody.put("tags",rep.getSampleImages());
            requestBody.put("email", rep.getEmali().get(0));
            Log.e(TAG, "fastAndroidNtworking: " + requestBody);

            Call<Object> call = apiInterface.getUser(requestBody);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                    progressBarLayout.setVisibility(View.VISIBLE);
                    if (response.code() == 200) {

                        if (response.isSuccessful()) {

                            if (response.body().toString().contains("errorDuplicate job exists")) {
                                Toast.makeText(SampleImagesActivity.this, "" + response.body(), Toast.LENGTH_SHORT).show();
                            } else {
                                list.add(rep);
                                Utils.saveArrayList(SampleImagesActivity.this, list, "rep");
                                Intent intent = new Intent(SampleImagesActivity.this, MainDashBoard.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }

                        } else {
                            Log.e(TAG, "onResponse: error --->" + TextStreamsKt.readText(response.errorBody().charStream()));
                            if (response.body().toString().contains("Duplicate job exists"))
                                Toast.makeText(SampleImagesActivity.this, "" + response.body(), Toast.LENGTH_SHORT).show();
//                            Log.e("TAG", "onResponse: " + response.body());

                        }
                    } else {
                        if (response.errorBody() != null) {
                            progressBarLayout.setVisibility(View.GONE);
                            showCustomDialog(list, rep);
                            Toast.makeText(SampleImagesActivity.this, "" + TextStreamsKt.readText(response.errorBody().charStream()), Toast.LENGTH_SHORT).show();
                            Log.e(TAG, " onResponse: error  pata nahi  " + TextStreamsKt.readText(response.errorBody().charStream()));


//                            JSONObject jsonObj;
//                            try {
//                                jsonObj = new JSONObject(TextStreamsKt.readText(response.errorBody().charStream()));
//                                Toast.makeText(SampleImagesActivity.this, "" + jsonObj.getString("msg"), Toast.LENGTH_SHORT).show();
//                                Log.e(TAG, "onResponse:  error  ky aho rahra hai " + jsonObj.getString("msg"));
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }

                        }

                    }
//                    try {
//                        JSONObject object=new JSONObject(new Gson().toJson(response.body()));
//                        Log.e("TAG", "onResponse: "+response );
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + call.toString());
                    progressBarLayout.setVisibility(View.INVISIBLE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showCustomDialog(ArrayList<Representatives> list, Representatives rep) {
        final String[] status = {""};
        Dialog dialog1 = new Dialog(this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCancelable(false);
        dialog1.setContentView(R.layout.loading_dialgo);
        TextView next = dialog1.findViewById(R.id.next);
        EditText jobName = dialog1.findViewById(R.id.show_image_alert_enter_enter_job);
        jobName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (jobName.getText().equals("")) {
                    next.setEnabled(false);
                    next.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.darkGray)));
                } else {
                    next.setEnabled(true);
                    next.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primary)));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

                next.setEnabled(true);
                next.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primary)));
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(jobName.getText().toString())) {
                    rep.setProject(jobName.getText().toString());
                    fastAndroidNtworking(list, rep);
                    dialog1.dismiss();
                } else {
                    jobName.setError("Enter uniques name");
                }
            }

        });

        dialog1.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (jobPostRequest != null) {
            jobPostRequest.cancel();
        }
    }

}