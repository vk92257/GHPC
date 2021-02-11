package com.lynhill.ghpc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lynhill.ghpc.R;
import com.lynhill.ghpc.listener.UserInfoListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AddMoreAdapter extends RecyclerView.Adapter<AddMoreAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<String> data;
    private UserInfoListener userInfoListener;

    public AddMoreAdapter(ArrayList<String> data, Context context) {
        this.context = context;
        this.data = data;
    }

    public void onPress(UserInfoListener userInfoListener) {
        this.userInfoListener = userInfoListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String s = data.get(position);
        holder.textView.setTextColor(context.getResources().getColor(R.color.black));
       holder.textView.setText(s);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}
