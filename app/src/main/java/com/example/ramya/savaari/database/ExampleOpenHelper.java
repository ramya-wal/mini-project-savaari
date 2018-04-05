package com.example.ramya.savaari.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ramya.savaari.models.User;
import com.example.ramya.savaari.models.Vehicle;
import com.example.ramya.savaari.models.VehicleImages;


public class ExampleOpenHelper extends SQLiteOpenHelper {
    public ExampleOpenHelper(Context context) {
        super(context, "savaari", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(User.CREATE_TABLE);
        sqLiteDatabase.execSQL(Vehicle.CREATE_TABLE);
        sqLiteDatabase.execSQL(VehicleImages.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
