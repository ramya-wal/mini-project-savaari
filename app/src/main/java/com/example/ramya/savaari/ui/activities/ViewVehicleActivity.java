package com.example.ramya.savaari.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.ramya.savaari.R;
import com.example.ramya.savaari.database.ExampleDao;
import com.example.ramya.savaari.models.Vehicle;
import com.example.ramya.savaari.models.VehicleImages;
import com.example.ramya.savaari.ui.fragments.ViewPagerFragment;
import com.example.ramya.savaari.util.Constants;

import java.util.ArrayList;
import java.util.Collections;


public class ViewVehicleActivity extends AppCompatActivity {

    TextView tvBrandModel, tvColour, tvYear, tvPrice;

    ExampleDao exampleDao;

    private ArrayList<VehicleImages> imagesArrayList;
    ArrayList<String> imagesArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veiw_vehicle);

        exampleDao = new ExampleDao(getApplicationContext());


        Intent intent = getIntent();
        String vehicleId = intent.getStringExtra(Constants.KEY_VEHICLE_ID);

        Vehicle vehicle = exampleDao.getVehicle(vehicleId);
        imagesArrayList = exampleDao.getVehicleImages(vehicleId);

        for (int i=0 ; i < imagesArrayList.size() ; i++)
            imagesArray.add(i, imagesArrayList.get(i).getImage());

        tvBrandModel = findViewById(R.id.activity_view_vehicle_tvBrandModel);
        tvColour = findViewById(R.id.activity_view_vehicle_tvColour);
        tvYear = findViewById(R.id.activity_view_vehicle_tvYear);
        tvPrice = findViewById(R.id.activity_view_vehicle_tvPrice);

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
        fragmentTransaction.add(R.id.activity_view_vehicle_fragViewPager, fragment);
        fragmentTransaction.commit();
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
