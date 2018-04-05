package com.example.ramya.savaari.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.ramya.savaari.R;
import com.example.ramya.savaari.models.Owner;
import com.example.ramya.savaari.models.User;
import com.example.ramya.savaari.models.UserList;
import com.example.ramya.savaari.models.Vehicle;
import com.example.ramya.savaari.models.VehicleImageList;
import com.example.ramya.savaari.models.VehicleImages;
import com.example.ramya.savaari.network.APIClient;
import com.example.ramya.savaari.network.APIInterface;
import com.example.ramya.savaari.ui.fragments.ViewPagerFragment;
import com.example.ramya.savaari.util.Constants;
import com.example.ramya.savaari.util.ProgressDialogClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewUserVehicleActivity extends AppCompatActivity {

    TextView tvBrandModel, tvColour, tvYear, tvPrice;
    TextView tvName, tvLivesIn, tvAddress, tvPhoneNo, tvEmail;

    private List<VehicleImages> imagesArrayList = new ArrayList<>();
    ArrayList<String> imagesArray = new ArrayList<>();

    ProgressDialogClass progressDialogClass;

    APIInterface apiInterface;
    Map<String, String> headers = new HashMap<>();
    Map<String, String> query = new HashMap<>();

    Vehicle vehicle;
    String vehicleId;

    User owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_vehicle);

        progressDialogClass = new ProgressDialogClass(this);

        owner = new User();

        apiInterface = APIClient.getClient().create(APIInterface.class);
        headers.put("X-Parse-Application-Id", Constants.APPLICATION_ID);
        headers.put("X-Parse-REST-API-Key", Constants.API_KEY);

        vehicle = getIntent().getParcelableExtra(Constants.KEY_VEHICLE_OBJECT);
        vehicleId = vehicle.getVehicleId();

        query.put("where", "{\"vehicleId\":\"" + vehicleId + "\"}");
        Call<VehicleImageList> call = apiInterface.getVehicleImageForVehicle(headers, query);
        progressDialogClass.showProgressDialog();
        call.enqueue(new Callback<VehicleImageList>() {
            @Override
            public void onResponse(Call<VehicleImageList> call, Response<VehicleImageList> response) {
                imagesArrayList = response.body().getResults();
                progressDialogClass.dismissProgressDialog();

                setVehicleDetails();
                getUserDetails();
            }

            @Override
            public void onFailure(Call<VehicleImageList> call, Throwable t) {
                progressDialogClass.dismissProgressDialog();
            }
        });


    }

    void getUserDetails() {
        final String userId = vehicle.getOwner().getObjectId();

//        query.put("where", "{\"objectId\":\"" + userId + "\"}");
        Call<User> call = apiInterface.getUserDetails(headers, userId);
        progressDialogClass.showProgressDialog();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                owner = response.body();
                progressDialogClass.dismissProgressDialog();

                setUserDetails();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressDialogClass.dismissProgressDialog();
            }
        });

    }

    void setUserDetails() {
        tvAddress = findViewById(R.id.acitvity_view_user_vehicle_tvAddress);
        tvPhoneNo = findViewById(R.id.acitvity_view_user_vehicle_tvPhoneNo);
        tvEmail = findViewById(R.id.acitvity_view_user_vehicle_tvEmail);
        tvName = findViewById(R.id.activity_view_user_vehicle_tvName);
        tvLivesIn = findViewById(R.id.acitvity_view_user_vehicle_tvLiveIn);

        String name, city;
        name = owner.getFirstName() + " " + owner.getLastName();
        city = owner.getCity() + " " + owner.getCountry();
        tvAddress.setText(owner.getAddress());
        tvPhoneNo.setText(owner.getPhoneNo());
        tvEmail.setText(owner.getEmail());
        tvLivesIn.setText(city);
        tvName.setText(name);
    }

    void setVehicleDetails() {
        for (int i = 0; i < imagesArrayList.size(); i++)
            imagesArray.add(i, imagesArrayList.get(i).getImage());

        tvBrandModel = findViewById(R.id.activity_view_user_vehicle_tvBrandModel);
        tvColour = findViewById(R.id.activity_view_user_vehicle_tvColour);
        tvYear = findViewById(R.id.activity_view_user_vehicle_tvYear);
        tvPrice = findViewById(R.id.activity_view_user_vehicle_tvPrice);

        String brandModel = vehicle.getBrand() + " " + vehicle.getModel();
        tvBrandModel.setText(brandModel);

        String priceText = getResources().getString(R.string.price, vehicle.getPrice());
        String yearText = getResources().getString(R.string.year, vehicle.getYear());
        String colourText = getResources().getString(R.string.colour, vehicle.getColor());
        tvPrice.setText(priceText);
        tvYear.setText(yearText);
        tvColour.setText(colourText);

        // ArrayList<VehicleImages> vehicleImagesArrayList = exampleDao.getVehicleImages(vehicleId);

        Bundle bundle = new Bundle();
        bundle.putStringArrayList(Constants.KEY_VEHICLE_OBJECT, imagesArray);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ViewPagerFragment fragment = new ViewPagerFragment();
        fragment.setArguments(bundle);
        fragmentTransaction.add(R.id.activity_view_user_vehicle_fragViewPager, fragment);
        fragmentTransaction.commit();
    }
}
