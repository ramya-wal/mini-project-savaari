package com.example.ramya.savaari.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.ramya.savaari.R;
import com.example.ramya.savaari.adapters.CustomRecyclerViewAdapter;
import com.example.ramya.savaari.interfaces.OnItemClickListener;
import com.example.ramya.savaari.models.Vehicle;
import com.example.ramya.savaari.models.VehicleImageList;
import com.example.ramya.savaari.models.VehicleImages;
import com.example.ramya.savaari.models.VehicleList;
import com.example.ramya.savaari.network.APIClient;
import com.example.ramya.savaari.network.APIInterface;
import com.example.ramya.savaari.util.Constants;
import com.example.ramya.savaari.util.ProgressDialogClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewAllVehiclesActivity extends AppCompatActivity implements OnItemClickListener {

    RecyclerView vehicleRecyclerView;
    CustomRecyclerViewAdapter adapter;

    List<Vehicle> vehiclesArrayList;

    APIInterface apiInterface;
    Map<String, String> headers = new HashMap<>();
    Map<String, String> query = new HashMap<>();

    TextView tvNoVehicle;

    SharedPreferences sharedPreferences;

    ProgressDialogClass progressDialogClass;

    List<VehicleImages> vehicleImagesList = new ArrayList<>();
    List<String> picArray  = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_vehicles);

        tvNoVehicle = findViewById(R.id.activity_view_all_vehicles_tvNoVehicleText);

        sharedPreferences = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        headers.put("X-Parse-Application-Id", Constants.APPLICATION_ID);
        headers.put("X-Parse-REST-API-Key", Constants.API_KEY);

        progressDialogClass = new ProgressDialogClass(this);

        vehiclesArrayList = new ArrayList<>();

        final String currentUser = sharedPreferences.getString(Constants.CURRENT_USER, null);

        Call<VehicleList> call = apiInterface.getAllVehicles(headers);
        progressDialogClass.showProgressDialog();
        call.enqueue(new Callback<VehicleList>() {
            @Override
            public void onResponse(Call<VehicleList> call, Response<VehicleList> response) {
                vehiclesArrayList = response.body().getResults();

                for (int i = 0; i < vehiclesArrayList.size(); i++) {
                    if (vehiclesArrayList.get(i).getOwner().getObjectId().equals(currentUser)) {
                        vehiclesArrayList.remove(i);
                        i--;
                    }
                }
                if (vehiclesArrayList != null) {
                    tvNoVehicle.setVisibility(View.GONE);
                    showVehicles();
                    getVehicleImages();
                } else {
                    tvNoVehicle.setVisibility(View.VISIBLE);
                }
                progressDialogClass.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<VehicleList> call, Throwable t) {
                progressDialogClass.dismissProgressDialog();
            }
        });

    }

    public void getVehicleImages() {
        for (int i = 0;  i < vehiclesArrayList.size() ; i++) {
            final int index = i;
            query.put("where", "{\"vehicleId\":\"" + vehiclesArrayList.get(i).getVehicleId() + "\"}");
            Call<VehicleImageList> call = apiInterface.getVehicleImageForVehicle(headers,query);
            call.enqueue(new Callback<VehicleImageList>() {
                @Override
                public void onResponse(Call<VehicleImageList> call, Response<VehicleImageList> response) {
                    vehicleImagesList = response.body().getResults();
                    picArray.add(vehicleImagesList.get(0).getImage());
                    vehiclesArrayList.get(index).setImage(vehicleImagesList.get(0).getImage());
                    adapter.notifyItemChanged(index);
                }

                @Override
                public void onFailure(Call<VehicleImageList> call, Throwable t) {
                    progressDialogClass.dismissProgressDialog();
                }
            });
        }

    }

    public void showVehicles () {
        vehicleRecyclerView = findViewById(R.id.activity_view_all_vehicles_rcvVehicleList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ViewAllVehiclesActivity.this);
        vehicleRecyclerView.setLayoutManager(layoutManager);
        adapter = new CustomRecyclerViewAdapter(vehiclesArrayList, ViewAllVehiclesActivity.this, false);
        vehicleRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(ViewAllVehiclesActivity.this, ViewUserVehicleActivity.class);
        //Passing vehicle object to VehicleDetailsActivity to view details
        intent.putExtra(Constants.KEY_VEHICLE_OBJECT, vehiclesArrayList.get(position));

        startActivity(intent);
    }

    @Override
    public void onViewClicked(int position) {

    }

    @Override
    public void onUpdateClicked(int position) {
        // No action
    }

    @Override
    public void onDeleteClicked(int position) {
        // No action
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
