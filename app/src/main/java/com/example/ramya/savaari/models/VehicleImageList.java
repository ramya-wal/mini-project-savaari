package com.example.ramya.savaari.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by LENONO on 4/5/2018.
 */

public class VehicleImageList {
    @SerializedName("results")
    @Expose
    private List<VehicleImages> vehicleImages = null;

    public List<VehicleImages> getResults() {
        return vehicleImages;
    }

    public void setResults(List<VehicleImages> vehicleImages) {
        this.vehicleImages = vehicleImages;
    }
}
