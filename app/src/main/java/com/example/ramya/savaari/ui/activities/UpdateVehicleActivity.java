package com.example.ramya.savaari.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ramya.savaari.R;
import com.example.ramya.savaari.adapters.CustomRecyclerViewAdapter;
import com.example.ramya.savaari.adapters.HorizontalRecyclerViewAdapter;
import com.example.ramya.savaari.database.ExampleDao;
import com.example.ramya.savaari.models.UpdateDetails;
import com.example.ramya.savaari.models.Vehicle;
import com.example.ramya.savaari.models.VehicleImages;
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

public class UpdateVehicleActivity extends AppCompatActivity {

    EditText etBrand, etModel, etColour, etYear, etPrice;
    Button btnUpdateVehicle;

    String brand, model, colour;
    int year, price;

    Vehicle vehicle;

    APIInterface apiInterface;
    ProgressDialogClass progressDialogClass;

    Map<String, String> headers = new HashMap<>();

    ExampleDao exampleDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_vehicle);

        headers.put("X-Parse-Application-Id", Constants.APPLICATION_ID);
        headers.put("X-Parse-REST-API-Key", Constants.API_KEY);
        headers.put("Content-Type", "application/json");

        exampleDao = new ExampleDao(this);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        progressDialogClass = new ProgressDialogClass(this);

        Intent intent = getIntent();
        vehicle = intent.getParcelableExtra(Constants.KEY_VEHICLE_OBJECT);

        etBrand = findViewById(R.id.activity_update_vehicle_etBrand);
        etColour = findViewById(R.id.activity_update_vehicle_etColour);
        etModel = findViewById(R.id.activity_update_vehicle_etModel);
        etYear = findViewById(R.id.activity_update_vehicle_etYear);
        etPrice = findViewById(R.id.activity_update_vehicle_etPrice);

        btnUpdateVehicle = findViewById(R.id.activity_update_vehicle_btnUpdateVehicle);

        etBrand.setText(vehicle.getBrand());
        etModel.setText(vehicle.getModel());
        etColour.setText(vehicle.getColor());
        etYear.setText(Integer.toString(vehicle.getYear()));
        etPrice.setText(Integer.toString(vehicle.getPrice()));

        btnUpdateVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                brand = etBrand.getText().toString();
                model = etModel.getText().toString();
                colour = etColour.getText().toString();
                year = Integer.parseInt(etYear.getText().toString());
                price = Integer.parseInt(etPrice.getText().toString());

                vehicle.setBrand(brand);
                vehicle.setModel(model);
                vehicle.setColor(colour);
                vehicle.setYear(year);
                vehicle.setPrice(price);

                progressDialogClass.showProgressDialog();
                Call<UpdateDetails> call = apiInterface.updateVehicle(headers, vehicle, vehicle.getVehicleId());
                call.enqueue(new Callback<UpdateDetails>() {
                    @Override
                    public void onResponse(Call<UpdateDetails> call, Response<UpdateDetails> response) {
                        Toast.makeText(getApplicationContext(), R.string.vehicle_updated, Toast.LENGTH_SHORT).show();
                        progressDialogClass.dismissProgressDialog();
                        exampleDao.updateVehicle(vehicle);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<UpdateDetails> call, Throwable t) {
                        progressDialogClass.dismissProgressDialog();
                    }
                });

            }
        });

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
