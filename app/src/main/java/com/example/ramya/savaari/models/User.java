package com.example.ramya.savaari.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class User implements Parcelable{

    public static String CREATE_TABLE = "CREATE TABLE user (profileImage TEXT, objectId TEXT, username TEXT, password TEXT, email TEXT, firstName TEXT, lastName TEXT, gender TEXT, dateOfBirth TEXT, phoneNo TEXT, address TEXT, city TEXT, country TEXT);";

    @SerializedName("profileImage")
    private String profileImageStream;

    @SerializedName("objectId")
    private String objectId;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("email")
    private String email;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("gender")
    private String gender;

    @SerializedName("dateOfBirth")
    private String dateOfBirth;

    @SerializedName("phoneNo")
    private String phoneNo;

    @SerializedName("address")
    private String address;

    @SerializedName("city")
    private String city;

    @SerializedName("country")
    private String country;

    public User() {
        this.profileImageStream = null;
        this.objectId = null;
        this.username = null;
        this.password = null;
        this.email = null;
        this.firstName = null;
        this.lastName = null;
        this.gender = null;
        this.dateOfBirth = null;
        this.phoneNo = null;
        this.address = null;
        this.city = null;
        this.country = null;
    }

    public User(String profileImageStream, String username, String password, String email, String firstName, String lastName, String gender, String dateOfBirth, String phoneNo, String address, String city, String country) {
        this.profileImageStream = profileImageStream;
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.phoneNo = phoneNo;
        this.address = address;
        this.city = city;
        this.country = country;
    }

    public String getProfileImageStream() {
        return profileImageStream;
    }

    public void setProfileImageStream(String profileImageStream) {
        this.profileImageStream = profileImageStream;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.profileImageStream);
        dest.writeString(this.objectId);
        dest.writeString(this.username);
        dest.writeString(this.password);
        dest.writeString(this.email);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.gender);
        dest.writeString(this.dateOfBirth);
        dest.writeString(this.phoneNo);
        dest.writeString(this.address);
        dest.writeString(this.city);
        dest.writeString(this.country);
    }

    protected User(Parcel in) {
        this.profileImageStream = in.readString();
        this.objectId = in.readString();
        this.username = in.readString();
        this.password = in.readString();
        this.email = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.gender = in.readString();
        this.dateOfBirth = in.readString();
        this.phoneNo = in.readString();
        this.address = in.readString();
        this.city = in.readString();
        this.country = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
