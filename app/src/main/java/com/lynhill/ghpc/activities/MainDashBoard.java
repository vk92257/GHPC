package com.lynhill.ghpc.activities;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lynhill.ghpc.R;
import com.lynhill.ghpc.adapter.RepAdapter;
import com.lynhill.ghpc.pojo.Representatives;

import java.util.ArrayList;

import io.paperdb.Paper;

public class MainDashBoard extends BaseActivity {
    private static final String TAG = MainDashBoard.class.getSimpleName();
    private ArrayList<Representatives> repList;
    private ImageView background;
    private RecyclerView rv;
    private RepAdapter repAdapter;

    private int numberOfColumns = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dash_board);
        rv = findViewById(R.id.rv);
        background = findViewById(R.id.backgroundImage);
        repList = Paper.book().read("rep");
//        Log.e(TAG, "onCreate: "+ repList.size());
        if (repList != null && !repList.isEmpty()) {
            Log.e(TAG, "onCreate: " + repList.size());
            Representatives repo = repList.get(0);
            Log.e(TAG, "name " + repo.getName() + " address " + repo.getAddress() + " dob " + repo.getAddress() + " email" + repo.getEmali() + " phone" + repo.getPhoneNumber() + " sample Image list" + repo.getSampleImages().size());
            recyclerview();
        }

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

    /**
     * open new activity for adding new user
     *
     * @param view
     */
    public void addEmployee(View view) {
        Intent intent = new Intent(this, FindEmployee.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}