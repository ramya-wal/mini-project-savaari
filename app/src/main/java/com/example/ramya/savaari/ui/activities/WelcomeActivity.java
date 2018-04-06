package com.example.ramya.savaari.ui.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ramya.savaari.R;
import com.example.ramya.savaari.adapters.CustomRecyclerViewAdapter;
import com.example.ramya.savaari.database.ExampleDao;
import com.example.ramya.savaari.interfaces.OnItemClickListener;
import com.example.ramya.savaari.models.User;
import com.example.ramya.savaari.models.Vehicle;
import com.example.ramya.savaari.models.VehicleImageList;
import com.example.ramya.savaari.models.VehicleImages;
import com.example.ramya.savaari.models.VehicleList;
import com.example.ramya.savaari.network.APIClient;
import com.example.ramya.savaari.network.APIInterface;
import com.example.ramya.savaari.util.Constants;
import com.example.ramya.savaari.util.ProgressDialogClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WelcomeActivity extends AppCompatActivity implements OnItemClickListener {

    ImageView ivProfileImage;
    TextView tvWelcomeText;
    TextView tvNoVehiclesText;
    RecyclerView rcvVehicleList;
    FloatingActionButton fabAddVehicle;
    CustomRecyclerViewAdapter adapter;

    ProgressDialogClass progressDialogClass;

    Toolbar toolbar;

    ExampleDao exampleDao;

    String currentUser;

    List<Vehicle> vehiclesArrayList = new ArrayList<>();
    List<VehicleImages> vehicleImagesList = new ArrayList<>();
    List<String> vehicleImageThumbnail = new ArrayList<>();
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    APIInterface apiInterface;

    Map<String, String> headers = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        headers.put("X-Parse-Application-Id", Constants.APPLICATION_ID);
        headers.put("X-Parse-REST-API-Key", Constants.API_KEY);

        fabAddVehicle = findViewById(R.id.aactivity_welcome_fabAddVehicle);

        ivProfileImage = findViewById(R.id.activity_welcome_ivProfileImage);
        tvWelcomeText = findViewById(R.id.activity_welcome_tvWelcomeText);
        tvNoVehiclesText = findViewById(R.id.activity_welcome_tvNoVehicleText);
        rcvVehicleList = findViewById(R.id.activity_welcome_rcvVehicleList);

        progressDialogClass = new ProgressDialogClass(WelcomeActivity.this);
        toolbar = findViewById(R.id.activity_welcome_toolBar);
        setSupportActionBar(toolbar);

        exampleDao = new ExampleDao(getApplicationContext());

        sharedpreferences = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        currentUser = sharedpreferences.getString(Constants.CURRENT_USER, null);

        User user = exampleDao.getUser(currentUser);

        Picasso.with(this).load(user.getProfileImageStream()).into(ivProfileImage);
        tvWelcomeText.setText(getResources().getString(R.string.welcome, user.getFirstName(), user.getLastName()));

        ArrayList<Vehicle> vehicles = exampleDao.getVehiclesOfUser(currentUser);
        if (vehicles == null || vehicles.size() == 0) {
            //API call for getting vehicles and inserting them into database
            progressDialogClass.showProgressDialog();
            Call<VehicleList> call = apiInterface.getAllVehicles(headers);
            // TODO Make pope API call here for current user
            call.enqueue(new Callback<VehicleList>() {
                @Override
                public void onResponse(Call<VehicleList> call, Response<VehicleList> response) {
                    vehiclesArrayList = response.body().getResults();

                    for (int i = 0; i < vehiclesArrayList.size(); i++) {
                        if (!(vehiclesArrayList.get(i).getOwner().getObjectId().equals(currentUser))) {
                            vehiclesArrayList.remove(i);
                            i--;
                        }
                    }
                    for (int i = 0; i < vehiclesArrayList.size(); i++) {
                        exampleDao.insertVehicle(vehiclesArrayList.get(i));
                    }
                    showVehicles();
                    getVehicleImages();
                }

                @Override
                public void onFailure(Call<VehicleList> call, Throwable t) {
                    progressDialogClass.dismissProgressDialog();
                }
            });
        } else {
            getVehicleDetailsFromDatabase();
        }


        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });

        tvWelcomeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });

        fabAddVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAddVehicle = new Intent(WelcomeActivity.this, AddVehicleActivity.class);
                startActivity(intentAddVehicle);
            }
        });

    }

    public void getVehicleImages() {
        Call<VehicleImageList> call = apiInterface.getAllVehicleImages(headers);
        call.enqueue(new Callback<VehicleImageList>() {
            @Override
            public void onResponse(Call<VehicleImageList> call, Response<VehicleImageList> response) {
                vehicleImagesList = response.body().getResults();
                for (int i = 0; i < vehicleImagesList.size(); i++) {
                    for (int j = 0; j < vehiclesArrayList.size(); j++) {
                        final int index = j;
                        if (vehicleImagesList.get(i).getVehicleId().equals(vehiclesArrayList.get(j).getVehicleId())) {
                            exampleDao.insertVehicleImages(vehicleImagesList.get(i));
                            vehiclesArrayList.get(index).setImage(vehicleImagesList.get(0).getImage());
                            adapter.notifyItemChanged(index);
                        }
                    }
                }
                progressDialogClass.dismissProgressDialog();
                getVehicleDetailsFromDatabase();
            }

            @Override
            public void onFailure(Call<VehicleImageList> call, Throwable t) {
                progressDialogClass.dismissProgressDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_logout:
                showLogOutDialog();
                break;
            case R.id.action_viewVehicles:
                Intent intent = new Intent(WelcomeActivity.this, ViewAllVehiclesActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showVehicles() {

        rcvVehicleList.setVisibility(View.VISIBLE);
        tvNoVehiclesText.setVisibility(View.GONE);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(WelcomeActivity.this);
        rcvVehicleList.setLayoutManager(layoutManager);

        adapter = new CustomRecyclerViewAdapter(vehiclesArrayList, WelcomeActivity.this, true);
        rcvVehicleList.setAdapter(adapter);
    }

    public void getVehicleDetailsFromDatabase() {
        vehiclesArrayList = exampleDao.getVehiclesOfUser(currentUser);

        if (vehiclesArrayList != null) {
            for (int i = 0; i < vehiclesArrayList.size(); i++) {
                vehicleImageThumbnail.add(exampleDao.getVehicleImageThumbnail(vehiclesArrayList.get(i).getVehicleId()));
                vehiclesArrayList.get(i).setImage((exampleDao.getVehicleImageThumbnail(vehiclesArrayList.get(i).getVehicleId())));
            }
            showVehicles();
        }
    }

    @Override
    protected void onResume() {
        vehiclesArrayList = exampleDao.getVehiclesOfUser(currentUser);
        super.onResume();
        if (vehiclesArrayList != null && vehicleImageThumbnail != null) {
            getVehicleDetailsFromDatabase();
        } else {
            tvNoVehiclesText.setVisibility(View.VISIBLE);
        }
    }

    void showLogOutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);
        builder.setMessage("Do you want to log out?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);

                        editor = sharedpreferences.edit();
                        editor.putString(Constants.CURRENT_USER, null);
                        editor.apply();
                        //Delete all details from database
                        exampleDao.deleteUser(currentUser);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Logout");
        alert.show();
    }

    @Override
    public void onItemClick(int position) {
        //no action
    }

    @Override
    public void onViewClicked(int position) {
        Intent intent = new Intent(WelcomeActivity.this, ViewVehicleActivity.class);
        intent.putExtra(Constants.KEY_VEHICLE_ID, vehiclesArrayList.get(position).getVehicleId());
        startActivity(intent);
    }

    @Override
    public void onUpdateClicked(int position) {
        Intent intent = new Intent(WelcomeActivity.this, UpdateVehicleActivity.class);
        intent.putExtra(Constants.KEY_VEHICLE_OBJECT, vehiclesArrayList.get(position));
        startActivity(intent);
    }

    @Override
    public void onDeleteClicked(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);
        builder.setMessage("Do you want to delete this vehicle?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final String vehicleId = vehiclesArrayList.get(position).getVehicleId();
                        progressDialogClass.showProgressDialog();
                        Call<ResponseBody> call = apiInterface.deleteVehicle(headers, vehicleId);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                deleteVehicleImages(position);
                                exampleDao.deleteVehicle(vehicleId);
                                vehiclesArrayList.remove(position);
                                vehicleImageThumbnail.remove(position);
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                progressDialogClass.dismissProgressDialog();
                            }
                        });
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Delete");
        alert.show();

    }

    void deleteVehicleImages(final int position) {
        final String vehicleId = vehiclesArrayList.get(position).getVehicleId();
        List<VehicleImages> vehicleImagesList = exampleDao.getVehicleImages(vehicleId);
        for (int i = 0; i < vehicleImagesList.size(); i++) {
            Call<ResponseBody> call = apiInterface.deleteVehicleImages(headers, vehicleImagesList.get(i).getObjectId());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    progressDialogClass.dismissProgressDialog();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressDialogClass.dismissProgressDialog();
                }
            });
        }

    }


}