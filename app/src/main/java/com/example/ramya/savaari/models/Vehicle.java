package com.example.ramya.savaari.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Vehicle implements Parcelable{

    public static String CREATE_TABLE = "CREATE TABLE vehicle (vehicleId INTEGER, model TEXT, brand TEXT, year INTEGER, color TEXT, price INTEGER, owner TEXT);";
    @SerializedName("objectId")
    private String vehicleId;

    @SerializedName("model")
    private String model;

    @SerializedName("brand")
    private String brand;

    @SerializedName("year")
    private int year;

    @SerializedName("color")
    private String color;

    @SerializedName("owner")
    private Owner owner;

    @SerializedName("price")
    private int price;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String image;

    public Vehicle() {
        this.vehicleId = null;
        this.model = null;
        this.brand = null;
        this.year = 0;
        this.color = null;
        this.owner = null;
        this.price = 0;
    }
    public Vehicle( String model, String brand, int year, String color, Owner owner, int price) {
        this.model = model;
        this.brand = brand;
        this.year = year;
        this.color = color;
        this.owner = owner;
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.vehicleId);
        dest.writeString(this.model);
        dest.writeString(this.brand);
        dest.writeInt(this.year);
        dest.writeString(this.color);
        dest.writeParcelable(this.owner, flags);
        dest.writeInt(this.price);
        dest.writeString(this.image);
    }

    protected Vehicle(Parcel in) {
        this.vehicleId = in.readString();
        this.model = in.readString();
        this.brand = in.readString();
        this.year = in.readInt();
        this.color = in.readString();
        this.owner = in.readParcelable(Owner.class.getClassLoader());
        this.price = in.readInt();
        this.image = in.readString();
    }

    public static final Creator<Vehicle> CREATOR = new Creator<Vehicle>() {
        @Override
        public Vehicle createFromParcel(Parcel source) {
            return new Vehicle(source);
        }

        @Override
        public Vehicle[] newArray(int size) {
            return new Vehicle[size];
        }
    };
}
