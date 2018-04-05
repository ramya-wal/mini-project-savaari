package com.example.ramya.savaari.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.ramya.savaari.R;
import com.example.ramya.savaari.interfaces.OnItemClickListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class HorizontalRecyclerViewAdapter extends RecyclerView.Adapter<HorizontalRecyclerViewAdapter.ViewHolder>{
    private List<String> vehicleImageList;
    private Context context;
    private OnItemClickListener listener;

    /**
     * A ViewHolder class that describes an item's view and related infrmation
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivVehicleImage;
        View itemView;

        private ViewHolder(View view) {
            super(view);
            this.itemView = view;
            this.ivVehicleImage = view.findViewById(R.id.horizontal_list_item_ivVehicleImage);
        }

        private void setOnClickListener(final int position, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(position);
                }
            });
        }
    }

    public HorizontalRecyclerViewAdapter(List<String> vehicleImageList, Context context) {
        this.vehicleImageList = vehicleImageList;
        this.context = context;
    }

    public void setOnItemClickListener (OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public HorizontalRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_list_item, parent, false);
        return new HorizontalRecyclerViewAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HorizontalRecyclerViewAdapter.ViewHolder holder, int position) {
        if(holder != null) {

            //Using picasso to load the image into ImageView
            Picasso.with(context)
                    .load(new File(vehicleImageList.get(position)))
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(holder.ivVehicleImage);
//holder.ivVehicleImage.setImageBitmap(BitmapFactory.decodeFile(vehicleImageList.get(position)));
            //Setting vehicle information intolist items TextViews
            holder.setOnClickListener(position, listener);
        }
    }

    @Override
    public int getItemCount() {
        return vehicleImageList.size();
    }
}
