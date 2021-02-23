package com.lynhill.ghpc.activities;


import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lynhill.ghpc.R;
import com.lynhill.ghpc.adapter.RepAdapter;
import com.lynhill.ghpc.pojo.Representatives;
import com.lynhill.ghpc.util.APIConstans;
import com.lynhill.ghpc.util.Constants;
import com.lynhill.ghpc.util.StorageManager;
import com.lynhill.ghpc.util.Utils;
import com.lynhill.ghpc.util.VolleySingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

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
        Log.e(TAG, "onCreate: "+repList );
//        repList = Paper.book().read("rep");
//        Log.e(TAG, "onCreate: "+ repList.size());
      if (repList!=null){
          if (repList != null && !repList.isEmpty()) {
              Representatives repo = repList.get(0);
            Log.e(TAG, "name " + repo.getProject() + " address " + repo.getAddress() + " dob " + repo.getAddress() + " email" + repo.getEmali() + " phone" + repo.getPhoneNumber() + " sample Image list" + repo.getSampleImages().size());
              recyclerview();
          }
      }

        getContactsVolleyRequest();

    }

    private void getContactsVolleyRequest() {

        getContactsRequest = new StringRequest(Request.Method.GET, APIConstans.JOB,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("HttpClient", "success! response: " + response.toString());
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

    private void recyclerview() {
        rv.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
//        rv.setLayoutManager(new LinearLayoutManager(this));
        repAdapter = new RepAdapter(repList, this);
        rv.setAdapter(repAdapter);
        rv.setHasFixedSize(true);
        if (repList.size() > 0) {
            background.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getContactsRequest != null) {
            getContactsRequest.cancel();
        }
    }

    /**
     * open new activity for adding new user
     *
     * @param view
     */
    public void addEmployee(View view) {
        Intent intent = new Intent(this, AddNewClient.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}