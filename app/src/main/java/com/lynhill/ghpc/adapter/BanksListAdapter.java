package com.lynhill.ghpc.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lynhill.ghpc.R;
import com.lynhill.ghpc.activities.MyWebView;
import com.lynhill.ghpc.listener.UserInfoListener;

import java.util.ArrayList;

public class BanksListAdapter extends RecyclerView.Adapter<BanksListAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<String> data;
    private ArrayList<Integer> banks;
    private ArrayList<String> hyperLink;
    private UserInfoListener userInfoListener;

    public BanksListAdapter(ArrayList<String> hyperlink, ArrayList<String> data, ArrayList<Integer> bank, Context context) {
        this.context = context;
        this.data = data;
        this.hyperLink = hyperlink;
        this.banks = bank;
    }

    public void onPress(UserInfoListener userInfoListener) {
        this.userInfoListener = userInfoListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bankrv_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String s = data.get(position);
        int image = banks.get(position);

        String text = "<a href='" + hyperLink.get(position) + "'> Apply for loan now ! </a>";
        holder.hyperLink.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT));
//        holder.hyperLink.setMovementMethod(LinkMovementMethod.getInstance());
        holder.hyperLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MyWebView.class);
                intent.putExtra("url", hyperLink.get(position));
                context.startActivity(intent);
            }
        });
        holder.textView.setText(s);

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "" + s, Toast.LENGTH_SHORT).show();

            }
        });
        holder.bankImage.setImageResource(image);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView, hyperLink;
        private ImageView bankImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.user_text);
            bankImage = itemView.findViewById(R.id.profile_image);
            hyperLink = itemView.findViewById(R.id.hyper_link);
        }
    }
}
