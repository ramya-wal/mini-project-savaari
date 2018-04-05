package com.example.ramya.savaari.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ramya.savaari.R;
import com.example.ramya.savaari.interfaces.OnItemClickListener;
import com.example.ramya.savaari.models.Vehicle;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.ViewHolder> {
    private List<Vehicle> vehiclesArrayList;
    private Context context;
    boolean isOwnVehicle;
    private OnItemClickListener listener;

    /**
     * A ViewHolder class that describes an item's view and related infrmation
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvVehicleModel;
        TextView tvVehicleID;
        ImageView ivVehicleImage;
        TextView tvMenuOptions;
        View itemView;

        private ViewHolder(View view) {
            super(view);
            this.itemView = view;
            this.tvVehicleModel = view.findViewById(R.id.list_item_tvVehicleModel);
            this.tvVehicleID = view.findViewById(R.id.list_item_tvVehicleId);
            this.tvMenuOptions = view.findViewById(R.id.list_item_menuOption);
            this.ivVehicleImage = view.findViewById(R.id.list_item_iv_vehicleImage);
        }
    }

    public CustomRecyclerViewAdapter(List<Vehicle> vehiclesArrayList, Context context, boolean isOwnVehicle) {
        this.vehiclesArrayList = vehiclesArrayList;
        this.context = context;
        this.isOwnVehicle = isOwnVehicle;
        this.listener = (OnItemClickListener) context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new CustomRecyclerViewAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (holder != null) {

            //Using picasso to load the image into ImageView
            if(vehiclesArrayList.get(position).getImage() != null) {
                Picasso.with(context)
                        .load(vehiclesArrayList.get(position).getImage())
                        .resize(350, 350)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .into(holder.ivVehicleImage);
            }

            //Setting vehicle information intolist items TextViews
            String vehicleModel = vehiclesArrayList.get(position).getBrand() + " " + vehiclesArrayList.get(position).getModel();
            holder.tvVehicleModel.setText(vehicleModel);
            holder.tvVehicleID.setText(context.getResources().getString(R.string.year, vehiclesArrayList.get(position).getYear()));


            holder.tvMenuOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(context, holder.tvMenuOptions);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.recyclerview_menu_options);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.view:
                                    listener.onViewClicked(position);
                                    break;
                                case R.id.update:
                                    listener.onUpdateClicked(position);
                                    break;
                                case R.id.delete:
                                    listener.onDeleteClicked(position);
                                    break;
                            }
                            return false;
                        }
                    });
                    popup.show();


                }
            });
           // if (!isOwnVehicle){
                holder.tvMenuOptions.setVisibility(View.GONE);
           // }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return vehiclesArrayList.size();
    }
}
