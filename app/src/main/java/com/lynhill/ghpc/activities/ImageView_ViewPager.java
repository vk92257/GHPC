package com.lynhill.ghpc.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.lynhill.ghpc.R;
import com.lynhill.ghpc.adapter.ImageFullScreen_Adapter;
import com.lynhill.ghpc.util.Constans;

import java.util.ArrayList;

public class ImageView_ViewPager extends AppCompatActivity {


    private ViewPager2 viewpager2;
    private ArrayList<String> data;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view__view_pager);
    intiview();
    getdatafromintent();
    setviewpager();
    }


    private void getdatafromintent() {
    data = getIntent().getStringArrayListExtra(Constans.IMAGE_ARRAY);
    position = getIntent().getIntExtra(Constans.IMAGE_POSITION,0);
    }

    private void intiview() {

        viewpager2 = findViewById(R.id.viewpager2);
    }


    private void setviewpager() {

        ImageFullScreen_Adapter imageFullScreen_adapter = new ImageFullScreen_Adapter(this,data);
        viewpager2.setAdapter(imageFullScreen_adapter);
        viewpager2.setCurrentItem(position,true);


    }
}