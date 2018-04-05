package com.example.ramya.savaari.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by LENONO on 3/30/2018.
 */

public class VehicleList {
    @SerializedName("results")
    @Expose
    private List<Vehicle> vehicles = null;

    public List<Vehicle> getResults() {
        return vehicles;
    }

    public void setResults(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }
}
