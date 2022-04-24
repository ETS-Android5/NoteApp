package com.example.allnotes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.allnotes.ImageModel;
import com.example.allnotes.R;
//import com.example.allnotes.model.ImageModel;

import java.util.ArrayList;

public  class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {
    private ArrayList<ImageModel> mlist;
    private Context context;
    public ImageAdapter(Context context, ArrayList<ImageModel> mlist){
        this.context = context;
        this.mlist = mlist;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image,parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(mlist.get(position).getImageUrl()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.cardImage);
        }
    }
}
