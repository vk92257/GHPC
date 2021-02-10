package com.lynhill.ghpc.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lynhill.ghpc.R;

import java.net.URI;
import java.util.ArrayList;

public class ImageFullScreen_Adapter extends RecyclerView.Adapter<ImageFullScreen_Adapter.ImageHolder> {
    private Context context;
    private ArrayList<String> data;

    public ImageFullScreen_Adapter(Context context, ArrayList<String> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fullimage_view_rv, parent, false);
        return new ImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
        holder.fullimage_iv.setImageURI(Uri.parse(data.get(position)));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ImageHolder extends RecyclerView.ViewHolder{
        ImageView fullimage_iv;
        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            fullimage_iv = itemView.findViewById(R.id.fullimage_iv);
        }
    }


}
