package com.example.ramya.savaari.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.ByteArrayInputStream;

public class VehicleImages implements Parcelable {

    public static String CREATE_TABLE = "CREATE TABLE vehicleImage (objectId TEXT, image TEXT, vehicleId TEXT);";
    @SerializedName("objectId")
    private String objectId;

    @SerializedName("image")
    private String image;

    @SerializedName("vehicleId")
    private String vehicleId;


    public VehicleImages() {
        this.objectId = null;
        this.image = null;
        this.vehicleId = null;
    }
    public VehicleImages( String image, String vehicleId) {
        this.image = image;
        this.vehicleId = vehicleId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.image);
        dest.writeString(this.vehicleId);
    }

    private VehicleImages(Parcel in) {
        this.image = in.readString();
        this.vehicleId = in.readString();
    }

    public static final Creator<VehicleImages> CREATOR = new Creator<VehicleImages>() {
        @Override
        public VehicleImages createFromParcel(Parcel source) {
            return new VehicleImages(source);
        }

        @Override
        public VehicleImages[] newArray(int size) {
            return new VehicleImages[size];
        }
    };
}
