package com.lynhill.ghpc.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lynhill.ghpc.R;
import com.lynhill.ghpc.activities.Representative_detail;
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Representatives rep = data.get(position);
        holder.username.setText(rep.getName());
        holder.email.setText(rep.getEmali());
        holder.address.setText(rep.getAddress());
        holder.phone.setText(" "+rep.getPhoneNumber());


        holder.root_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Representative_detail.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView username, email, phone,address;
        RelativeLayout root_rl;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.userName);
            phone = itemView.findViewById(R.id.phone);
            email = itemView.findViewById(R.id.email);
            address= itemView.findViewById(R.id.address);
            root_rl = itemView.findViewById(R.id.root_rl);
        }
    }
}
