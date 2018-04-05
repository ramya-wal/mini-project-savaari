package com.example.ramya.savaari.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by LENONO on 4/3/2018.
 */

public class VehicleAddedResponse {
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("objectId")
    @Expose
    private String objectId;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
