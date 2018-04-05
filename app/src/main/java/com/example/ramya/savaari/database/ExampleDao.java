package com.example.ramya.savaari.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ramya.savaari.models.Owner;
import com.example.ramya.savaari.models.User;
import com.example.ramya.savaari.models.Vehicle;
import com.example.ramya.savaari.models.VehicleImages;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//CREATE TABLE user (profileImage TEXT, objectId TEXT, username TEXT, password TEXT, email TEXT, firstName TEXT, lastName TEXT, gender TEXT, dateOfBirth TEXT, phoneNo TEXT, address TEXT, city TEXT, country TEXT
//CREATE TABLE vehicle (vehicleId INTEGER, model TEXT, brand TEXT, year INTEGER, color TEXT, price INTEGER, owner TEXT
//CREATE TABLE vehicleImage (image TEXT, vehicleId TEXT


public class ExampleDao {
    private SQLiteDatabase database;

    public ExampleDao(Context context) {
        SQLiteOpenHelper helper = new ExampleOpenHelper(context);
        database = helper.getWritableDatabase();
    }


    public void insertUser(User user) {
        ContentValues values = new ContentValues();

        values.put("profileImage", user.getProfileImageStream());
        values.put("objectId", user.getObjectId());
        values.put("username", user.getUsername());
        values.put("password", user.getPassword());
        values.put("email", user.getEmail());
        values.put("firstName", user.getFirstName());
        values.put("lastName", user.getLastName());
        values.put("gender", user.getGender());
        values.put("dateOfBirth", user.getDateOfBirth());
        values.put("phoneNo", user.getPhoneNo());
        values.put("address", user.getAddress());
        values.put("city", user.getCity());
        values.put("country", user.getCountry());

        database.insert("user", null, values);
    }

    public User getUser(String objectId) {
        User user = new User();
        String selection = "objectId=?";
        String[] selectionArgs = {objectId};

        Cursor cursor = database.query("user", null, selection, selectionArgs, null, null, null);

        cursor.moveToFirst();

        String profileImage = cursor.getString(cursor.getColumnIndex("profileImage"));
        String phoneNo = cursor.getString(cursor.getColumnIndex("phoneNo"));
        String username = cursor.getString(cursor.getColumnIndex("username"));
        String password = cursor.getString(cursor.getColumnIndex("password"));
        String email = cursor.getString(cursor.getColumnIndex("email"));
        String firstName = cursor.getString(cursor.getColumnIndex("firstName"));
        String lastName = cursor.getString(cursor.getColumnIndex("lastName"));
        String gender = cursor.getString(cursor.getColumnIndex("gender"));
        String dateOfBirth = cursor.getString(cursor.getColumnIndex("dateOfBirth"));
        String address = cursor.getString(cursor.getColumnIndex("address"));
        String city = cursor.getString(cursor.getColumnIndex("city"));
        String country = cursor.getString(cursor.getColumnIndex("country"));

        user.setProfileImageStream(profileImage);
        user.setObjectId(objectId);
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setGender(gender);
        user.setDateOfBirth(dateOfBirth);
        user.setPhoneNo(phoneNo);
        user.setAddress(address);
        user.setCity(city);
        user.setCountry(country);

        cursor.close();
        return user;
    }

    public void deleteUser(String userId) {
        String selection = "objectId=?";
        String[] selectionArgs = {userId};

        deleteAllVehiclesOfUser(userId);
        database.delete("user", selection, selectionArgs);
    }

    public void updateUser(User user) {
        ContentValues values = new ContentValues();

        values.put("profileImage", user.getProfileImageStream());
        values.put("objectId", user.getObjectId());
        values.put("username", user.getUsername());
        values.put("password", user.getPassword());
        values.put("email", user.getEmail());
        values.put("firstName", user.getFirstName());
        values.put("lastName", user.getLastName());
        values.put("gender", user.getGender());
        values.put("dateOfBirth", user.getDateOfBirth());
        values.put("phoneNo", user.getPhoneNo());
        values.put("address", user.getAddress());
        values.put("city", user.getCity());
        values.put("country", user.getCountry());

        String selection = "objectId=?";
        String[] selectionArgs = {user.getObjectId()};

        database.update("user", values, selection, selectionArgs);
    }


    public void insertVehicle(Vehicle vehicle) {
        ContentValues values = new ContentValues();

        values.put("vehicleId", vehicle.getVehicleId());
        values.put("model", vehicle.getModel());
        values.put("brand", vehicle.getBrand());
        values.put("year", vehicle.getYear());
        values.put("color", vehicle.getColor());
        values.put("price", vehicle.getPrice());
        values.put("owner", vehicle.getOwner().getObjectId());

        // TODO Check whether vehicleID exists
        database.insert("vehicle", null, values);
    }

    public ArrayList<Vehicle> getVehiclesOfUser(String owner) {
        ArrayList<Vehicle> vehicleArrayList = new ArrayList<>();
        String selection = "owner=?";
        String[] selectionArgs = {owner};

        Cursor cursor = database.query("vehicle", null, selection, selectionArgs, null, null, null);

        cursor.moveToFirst();

        boolean isVehicleValid = false;
        while (!cursor.isAfterLast()) {
            Vehicle vehicle = new Vehicle();

            String vehicleId = cursor.getString(cursor.getColumnIndex("vehicleId"));
            String model = cursor.getString(cursor.getColumnIndex("model"));
            String brand = cursor.getString(cursor.getColumnIndex("brand"));
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            String color = cursor.getString(cursor.getColumnIndex("color"));
            int price = cursor.getInt(cursor.getColumnIndex("price"));

            vehicle.setVehicleId(vehicleId);
            vehicle.setModel(model);
            vehicle.setBrand(brand);
            vehicle.setYear(year);
            vehicle.setColor(color);
            vehicle.setPrice(price);
            vehicle.setOwner(new Owner(owner));

            isVehicleValid = true;
            vehicleArrayList.add(vehicle);
            cursor.moveToNext();
        }

        cursor.close();

        if (isVehicleValid) {
            return vehicleArrayList;
        } else {
            return null;
        }
    }

    public Vehicle getVehicle(String vehicleId) {
        String selection = "vehicleId=?";
        String selectionArgs[] = {vehicleId};

        Cursor cursor = database.query("vehicle", null, selection, selectionArgs, null, null, null);
        Vehicle vehicle = new Vehicle();
        cursor.moveToFirst();

        String model = cursor.getString(cursor.getColumnIndex("model"));
        String brand = cursor.getString(cursor.getColumnIndex("brand"));
        int year = cursor.getInt(cursor.getColumnIndex("year"));
        String color = cursor.getString(cursor.getColumnIndex("color"));
        int price = cursor.getInt(cursor.getColumnIndex("price"));
        String owner = cursor.getString(cursor.getColumnIndex("owner"));

        vehicle.setVehicleId(vehicleId);
        vehicle.setModel(model);
        vehicle.setBrand(brand);
        vehicle.setYear(year);
        vehicle.setColor(color);
        vehicle.setPrice(price);
        vehicle.setOwner(new Owner(owner));

        cursor.close();

        return vehicle;

    }

    public void updateVehicle(Vehicle vehicle) {
        ContentValues values = new ContentValues();

        values.put("vehicleId", vehicle.getVehicleId());
        values.put("model", vehicle.getModel());
        values.put("brand", vehicle.getBrand());
        values.put("year", vehicle.getYear());
        values.put("color", vehicle.getColor());
        values.put("price", vehicle.getPrice());
        values.put("owner", vehicle.getOwner().getObjectId());

        String selection = "vehicleId=?";
        String selectionArgs[] = {vehicle.getVehicleId()};

        database.update("vehicle", values, selection, selectionArgs);
    }

    public void deleteVehicle(String vehicleId) {
        String selection = "vehicleId=?";
        String selectionArgs[] = {vehicleId};

        deleteAllVehicleImagesOfVehicle(vehicleId);
        database.delete("vehicle", selection, selectionArgs);
    }

    public void deleteAllVehiclesOfUser(String owner) {
        String selection = "owner=?";
        String[] selectionArgs = {owner};

        Cursor cursor = database.query("vehicle", null, selection, selectionArgs, null, null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            String vehicleId = cursor.getString(cursor.getColumnIndex("vehicleId"));

            deleteVehicle(vehicleId);

            cursor.moveToNext();
        }

        cursor.close();
    }


    public void insertVehicleImages(VehicleImages vehicleImages) {
        ContentValues values = new ContentValues();

        values.put("image", vehicleImages.getImage());
        values.put("vehicleId", vehicleImages.getVehicleId());
        values.put("objectId", vehicleImages.getObjectId());

        database.insert("vehicleImage", null, values);
    }

    public String getVehicleImageThumbnail(String vehicleId) {
        String selection = "vehicleId=?";
        String selectionArgs[] = {vehicleId};

        Cursor cursor = database.query("vehicleImage", null, selection, selectionArgs, null, null, null);

        String image;
        if (cursor != null && cursor.moveToFirst()) {
            image = cursor.getString(cursor.getColumnIndex("image"));
            cursor.close();
        } else
            image = null;
        return image;
    }

    public ArrayList<VehicleImages> getVehicleImages(String vehicleId) {
        ArrayList<VehicleImages> vehicleImagesArrayList = new ArrayList<>();
        String selection = "vehicleId=?";
        String selectionArgs[] = {vehicleId};

        Cursor cursor = database.query("vehicleImage", null, selection, selectionArgs, null, null, null);

        cursor.moveToFirst();

        boolean isVehicleImageValid = false;
        while (!cursor.isAfterLast()) {
            VehicleImages vehicleImages = new VehicleImages();

            String image = cursor.getString(cursor.getColumnIndex("image"));
            String objectId = cursor.getString(cursor.getColumnIndex("objectId"));
            vehicleImages.setVehicleId(vehicleId);
            vehicleImages.setObjectId(objectId);
            vehicleImages.setImage(image);

            isVehicleImageValid = true;
            vehicleImagesArrayList.add(vehicleImages);
            cursor.moveToNext();
        }

        cursor.close();

        if (isVehicleImageValid) {
            return getUniqueVehicleImages(vehicleImagesArrayList);
        } else {
            return null;
        }
    }

    private ArrayList<VehicleImages> getUniqueVehicleImages(List<VehicleImages> vehicleImagesList) {
        Map<String, VehicleImages> vehicleImagesMap = new HashMap<>(vehicleImagesList.size());
        for (VehicleImages vehicleImages :
                vehicleImagesList) {
            vehicleImagesMap.put(vehicleImages.getObjectId(), vehicleImages);
        }
        return new ArrayList<>(vehicleImagesMap.values());
    }

    public void deleteVehicleImage(String objectId) {
        String selection = "objectId=?";
        String selectionArgs[] = {objectId + ""};

        database.delete("vehicleImage", selection, selectionArgs);
    }

    public void deleteAllVehicleImagesOfVehicle(String vehicleId) {
        String selection = "vehicleId=?";
        String selectionArgs[] = {vehicleId};

        Cursor cursor = database.query("vehicleImage", null, selection, selectionArgs, null, null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            String vehicleId1 = cursor.getString(cursor.getColumnIndex("vehicleId"));

            deleteVehicleImage(vehicleId1);

            cursor.moveToNext();
        }

        cursor.close();
    }

}
