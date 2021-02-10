package com.lynhill.ghpc.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lynhill.ghpc.R;
import com.lynhill.ghpc.activities.ImageView_ViewPager;
import com.lynhill.ghpc.listener.UserInfoListener;
import com.lynhill.ghpc.util.Constans;

import java.util.ArrayList;

public class SampleImageAdapter extends RecyclerView.Adapter<SampleImageAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<String> data;
    private UserInfoListener userInfoListener;


    public SampleImageAdapter(ArrayList<String> data, Context context) {
        this.context = context;
        this.data = data;
    }

    public void onPress(UserInfoListener userInfoListener) {
        this.userInfoListener = userInfoListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_image_rv, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String s = data.get(position);
        Glide.with(context).load(s).into(holder.textView);
        holder.root_camera_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageView_ViewPager.class);
                intent.putExtra(Constans.IMAGE_ARRAY,data);
                intent.putExtra(Constans.IMAGE_POSITION,position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView textView;
        private LinearLayout root_camera_ll;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.user_image);
            root_camera_ll = itemView.findViewById(R.id.root_camera_ll);
        }
    }
}
