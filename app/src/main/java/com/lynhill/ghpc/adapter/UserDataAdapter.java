package com.lynhill.ghpc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lynhill.ghpc.R;
import com.lynhill.ghpc.listener.UserInfoListener;

import java.util.ArrayList;

public class UserDataAdapter extends RecyclerView.Adapter<UserDataAdapter.MyViewHolder> {
    private int id;
    private Context context;
    private ArrayList<String> data;
    private UserInfoListener userInfoListener;

    public UserDataAdapter(ArrayList<String> data, Context context, int id) {
        this.id = id;
        this.context = context;
        this.data = data;
    }

    public void onPress(UserInfoListener userInfoListener) {
        this.userInfoListener = userInfoListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_data_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String s = data.get(position);
        holder.textView.setText(s);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "" + s, Toast.LENGTH_SHORT).show();
                userInfoListener.onClick(id,s);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.user_text);
        }
    }
}
