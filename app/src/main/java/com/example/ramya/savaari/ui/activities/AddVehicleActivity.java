package com.example.ramya.savaari.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.ramya.savaari.R;
import com.example.ramya.savaari.adapters.HorizontalRecyclerViewAdapter;
import com.example.ramya.savaari.database.ExampleDao;
import com.example.ramya.savaari.models.Owner;
import com.example.ramya.savaari.models.Vehicle;
import com.example.ramya.savaari.models.VehicleAddedResponse;
import com.example.ramya.savaari.models.VehicleImages;
import com.example.ramya.savaari.network.APIClient;
import com.example.ramya.savaari.network.APIInterface;
import com.example.ramya.savaari.util.Constants;
import com.example.ramya.savaari.util.ProgressDialogClass;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddVehicleActivity extends AppCompatActivity {

    ImageView ivAddImage;
    List<String> imageList;
    RecyclerView rcvImageList;

    EditText etBrand, etModel, etYear, etColour, etPrice;
    Button btnAddVehicle;

    ProgressDialogClass progressDialogClass;

    SharedPreferences sharedpreferences;

    ExampleDao exampleDao;
    String brand, model, colour, year, price, currentUser;

    Map<String, String> vehicleUploadHeaders = new HashMap<>();

    APIInterface apiInterface;

    byte[] image;
    Vehicle vehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        Parse.initialize(this);

        vehicleUploadHeaders.put("X-Parse-Application-Id", Constants.APPLICATION_ID);
        vehicleUploadHeaders.put("X-Parse-REST-API-Key", Constants.API_KEY);
        vehicleUploadHeaders.put("Content-Type", "application/json");

        apiInterface = APIClient.getClient().create(APIInterface.class);

        exampleDao = new ExampleDao(getApplicationContext());
        imageList = new ArrayList<>();

        progressDialogClass = new ProgressDialogClass(AddVehicleActivity.this);
        etBrand = findViewById(R.id.activity_add_vehicle_etBrand);
        etModel = findViewById(R.id.activity_add_vehicle_etModel);
        etColour = findViewById(R.id.activity_add_vehicle_etColour);
        etYear = findViewById(R.id.activity_add_vehicle_etYear);
        etPrice = findViewById(R.id.activity_add_vehicle_etPrice);
        ivAddImage = findViewById(R.id.activity_add_vehicle_ivAddImage);
        rcvImageList = findViewById(R.id.activity_add_vehicle_rcvImageList);

        sharedpreferences = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        currentUser = sharedpreferences.getString(Constants.CURRENT_USER, null);

        btnAddVehicle = findViewById(R.id.activity_add_vehicle_btnAddVehicle);

        btnAddVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brand = etBrand.getText().toString();
                model = etModel.getText().toString();
                year = etYear.getText().toString();
                price = etPrice.getText().toString();
                colour = etColour.getText().toString();

                vehicle = new Vehicle(model, brand, Integer.parseInt(year), colour, new Owner(currentUser), Integer.parseInt(price));
                progressDialogClass.showProgressDialog();

                Call<VehicleAddedResponse> call = apiInterface.addVehicle(vehicleUploadHeaders, vehicle);
                call.enqueue(new Callback<VehicleAddedResponse>() {
                    @Override
                    public void onResponse(Call<VehicleAddedResponse> call, Response<VehicleAddedResponse> response) {
                        VehicleAddedResponse vehicleAddedResponse = response.body();
                        vehicle.setVehicleId(vehicleAddedResponse.getObjectId());

                        exampleDao.insertVehicle(vehicle);

                        try {
                            uploadVehicleImages();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<VehicleAddedResponse> call, Throwable t) {
                        progressDialogClass.dismissProgressDialog();
                    }
                });
            }
        });

        ivAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");

                startActivityForResult(intent, Constants.PHOTO_REQUEST_GALLERY);
                rcvImageList.setVisibility(View.VISIBLE);

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


    @Override
    protected void onResume() {
        super.onResume();
        if (imageList != null) {
            HorizontalRecyclerViewAdapter adapter = new HorizontalRecyclerViewAdapter(imageList, AddVehicleActivity.this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AddVehicleActivity.this, LinearLayoutManager.HORIZONTAL, true);

            DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.HORIZONTAL);
            rcvImageList.addItemDecoration(decoration);

            rcvImageList.setLayoutManager(layoutManager);
            rcvImageList.setAdapter(adapter);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == Constants.PHOTO_REQUEST_GALLERY) {

            Uri selectedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            if (selectedImage != null) {
                Cursor cursor = getContentResolver().query(selectedImage, filePath, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePath[0]);
                    cursor.moveToFirst();
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    imageList.add(picturePath);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void uploadVehicleImages() throws ParseException {
        for (int i = 0; i < imageList.size(); i++) {
            Bitmap thumbnail = (BitmapFactory.decodeFile(imageList.get(i)));

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream);
            image = stream.toByteArray();

            ParseFile file = new ParseFile("vehicle.jpg", image);
            file.save();

            String imageUrl = file.getUrl();
            String vehicleId = vehicle.getVehicleId();

            final VehicleImages vehicleImage = new VehicleImages(imageUrl, vehicleId);
            Call<VehicleAddedResponse> imageAddCall = apiInterface.addVehicleImage(vehicleUploadHeaders, vehicleImage);
            imageAddCall.enqueue(new Callback<VehicleAddedResponse>() {
                @Override
                public void onResponse(Call<VehicleAddedResponse> call, Response<VehicleAddedResponse> response) {
                    vehicleImage.setObjectId(response.body().getObjectId());
                    progressDialogClass.dismissProgressDialog();
                    exampleDao.insertVehicleImages(vehicleImage);
                    finish();
                }

                @Override
                public void onFailure(Call<VehicleAddedResponse> call, Throwable t) {
                    progressDialogClass.dismissProgressDialog();
                }
            });
        }
    }
}
