package com.lynhill.ghpc.activities;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
//import com.androidnetworking.AndroidNetworking;
//import com.androidnetworking.common.Priority;
//import com.androidnetworking.error.ANError;
//import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParseException;
import com.lynhill.ghpc.R;
import com.lynhill.ghpc.adapter.RepAdapter;
import com.lynhill.ghpc.pojo.Representatives;
import com.lynhill.ghpc.util.APIConstans;
import com.lynhill.ghpc.util.ApiInterface;
import com.lynhill.ghpc.util.Constants;
import com.lynhill.ghpc.util.StorageManager;
import com.lynhill.ghpc.util.Utils;
import com.lynhill.ghpc.util.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import kotlin.io.TextStreamsKt;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainDashBoard extends BaseActivity {
    private static final String TAG = MainDashBoard.class.getSimpleName();
    private ArrayList<Representatives> repList;
    private ImageView background;
    private RecyclerView rv;
    private RepAdapter repAdapter;
    private StringRequest getContactsRequest;
    private int numberOfColumns = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dash_board);
        StorageManager.getInstance(this).setCurrentUser(-1);
        rv = findViewById(R.id.rv);
        background = findViewById(R.id.backgroundImage);
        repList = Utils.getArrayList(this, Constants.REPRESENTATIVE_LIST);
        if (repList != null) {
            if (repList != null && !repList.isEmpty()) {
                Representatives repo = repList.get(0);
                Log.e(TAG, "name " + repo.getProject() + " address " + repo.getAddress() + " dob " + repo.getAddress() + " email" + repo.getEmali() + " phone" + repo.getPhoneNumber() + " sample Image list" + repo.getSampleImages().size());
                recyclerview();


            }
        }
//        getJobsVolleyRequest();
//        uploadImageOnJobnimbus("ueueueh");
    }
    private void uploadImageOnJobnimbus(String jnid) {
//        progressBarLayout.setVisibility(View.VISIBLE);


//        for (int i = 0; i < sampleImages.size(); i++) {
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

//                requestBody.put("related", new ArrayList<String>().add(jnid));
                requestBody.put("related", jnid);
                requestBody.put("type", "photo");
                requestBody.put("url", "https://firebasestorage.googleapis.com/v0/b/ghpc-d43d4.appspot.com/o/profile%2Fimage_1613915333266.jpg?alt=media&token=d952495f-a1b5-4dc8-a910-723a8f2f279f");
                Log.e(TAG, " contact: " + requestBody);
                Call<Object> call = apiInterface.uploadUser(requestBody);

//                int finalI = i;
                call.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
//                        progressBarLayout.setVisibility(View.VISIBLE);
                        if (response.code() == 200) {

                            if (response.isSuccessful()) {

                                Log.e(TAG, "onResponse: "+response.body());
                            } else {
                                Log.e(TAG, "onResponse: error --->" + TextStreamsKt.readText(response.errorBody().charStream()));
                                if (response.body().toString().contains("Duplicate job exists"))
                                    Toast.makeText(MainDashBoard.this, "" + response.body(), Toast.LENGTH_SHORT).show();
//                            Log.e("TAG", "onResponse: " + response.body());

                            }
                        } else {
                            if (response.errorBody() != null) {
//                                progressBarLayout.setVisibility(View.GONE);
                                Toast.makeText(MainDashBoard.this, "" + TextStreamsKt.readText(response.errorBody().charStream()), Toast.LENGTH_SHORT).show();
                                Log.e(TAG, " onResponse: error  pata nahi  " + TextStreamsKt.readText(response.errorBody().charStream()));

                            }

                        }

                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + call.toString());
//                        progressBarLayout.setVisibility(View.INVISIBLE);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
//        }
    }

    private void getJobsVolleyRequest() {
        getContactsRequest = new StringRequest(Request.Method.GET, "https://app.jobnimbus.com/api1/contacts",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("HttpClient", "success! response: " + response.toString());
                        handlingJobsResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("HttpClient", "error: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("user","YOUR USERNAME");
//                params.put("pass","YOUR PASSWORD");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "bearer" + getResources().getString(R.string.jobnimbus_token));
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQue(getContactsRequest);
    }

    private void handlingJobsResponse(String response) {
        if (repList == null)
            repList = new ArrayList<>();
        try {
            JSONObject allJobs = new JSONObject(response);
            int totalJobsCount = allJobs.getInt(Constants.GET_JOBS_TOTAL_COUNT);
            JSONArray listOfJobs = allJobs.getJSONArray(Constants.GET_JOBS_RESULT_LIST);
            for (int i = 0; i < totalJobsCount; i++) {
                Representatives rep = new Representatives();
                JSONObject jobsData = listOfJobs.getJSONObject(i);
                rep.setName(jobsData.getString("first_name"));
                if (jobsData.has("address_line2") && !jobsData.isNull("address_line2"))
                rep.setAddress(jobsData.getString("address_line2"));

                if (jobsData.has("image_id") && !jobsData.isNull("image_id")) {
                    ArrayList<String> strings1 = new ArrayList<>();
                    strings1.add(jobsData.getString("mobile_phone"));
                    rep.setPhoneNumber(strings1);
                }

                ArrayList<String> email = new ArrayList<>();
                email.add(jobsData.getString("email"));
                rep.setPhoneNumber(email);



                if (jobsData.has("image_id") && !jobsData.isNull("image_id")) {
//                if (!jobsData.toString().contains("\"image_id\": null") ) {
                    JSONArray images = jobsData.getJSONArray("image_id");
                   if (images != null){
                       ArrayList<String> strings = new ArrayList<>();
                       for (int j = 0; j < images.length(); j++) {
                           strings.add(images.getString(j));
                       }
                       rep.setSampleImages(strings);
                   }
                }
                repList.add(rep);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        recyclerview();
    }

    private void recyclerview() {
        rv.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
//        rv.setLayoutManager(new LinearLayoutManager(this));
        repAdapter = new RepAdapter(repList, this);
        rv.setAdapter(repAdapter);
        rv.setHasFixedSize(true);
        if (repList.size() > 0) {
            background.setVisibility(View.GONE);
//            for (int i = 0; i < repList.get(0).getSampleImages().size(); i++) {
////                firebaseUrlConverter(Uri.parse(repList.get(0).getSampleImages().get(i)));
//            }
//            ndroidNetworking.initialize(getApplicationContext());
//            jobPostRequestApiCall(repList.get(0));
//            fastAndroidNtworking(repList.get(0));

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getContactsRequest != null) {
            getContactsRequest.cancel();
        }
    }

    public void addEmployee(View view) {
        Intent intent = new Intent(this, AddNewClient.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}