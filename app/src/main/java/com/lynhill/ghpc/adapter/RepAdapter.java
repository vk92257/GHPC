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
import com.lynhill.ghpc.pojo.Representatives;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RepAdapter extends RecyclerView.Adapter<RepAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Representatives> data;
    private UserInfoListener userInfoListener;

    public RepAdapter(ArrayList<Representatives> data, Context context) {
        this.context = context;
        this.data = data;
    }

    public void onPress(UserInfoListener userInfoListener) {
        this.userInfoListener = userInfoListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rep_rv, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       Representatives rep = data.get(position);
        holder.username.setText(rep.getName());
        holder.email.setText(rep.getEmali());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
       TextView  username , email ;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.userName);
            email = itemView.findViewById(R.id.email);
        }
    }
}
