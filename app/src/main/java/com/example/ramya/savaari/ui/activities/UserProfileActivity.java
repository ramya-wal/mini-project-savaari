package com.example.ramya.savaari.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.ramya.savaari.R;
import com.example.ramya.savaari.database.ExampleDao;
import com.example.ramya.savaari.models.User;
import com.example.ramya.savaari.util.Constants;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    String currentUser;
    User user;
    ExampleDao exampleDao;

    FloatingActionButton updateUser;

    TextView tvName, tvLivesIn, tvAddress, tvDob, tvPhoneNo, tvEmail;
    CircleImageView civProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        sharedpreferences = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        currentUser = sharedpreferences.getString(Constants.CURRENT_USER, null);

        exampleDao = new ExampleDao(getApplicationContext());

        String labelText = user.getFirstName() + " " + user.getLastName();
        setTitle(labelText);
    }

    protected void onResume() {
        super.onResume();
        user = exampleDao.getUser(currentUser);
        init();
    }

    public void init() {
        tvAddress = findViewById(R.id.activity_user_profile_tvAddress);
        tvPhoneNo = findViewById(R.id.activity_user_profile_tvPhoneNo);
        tvEmail = findViewById(R.id.activity_user_profile_tvEmail);
        tvName = findViewById(R.id.activity_user_profile_tvName);
        tvDob = findViewById(R.id.activity_user_profile_tvDob);
        tvLivesIn = findViewById(R.id.activity_user_profile_tvLivesIn);

        civProfileImage = findViewById(R.id.activity_user_profile_civProfileImage);

        updateUser = findViewById(R.id.activity_user_profile_fabUpdateUser);
        updateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, UpdateUserActivity.class);
                intent.putExtra("USER",user);
                startActivity(intent);
            }
        });

        String name = user.getFirstName() + " " + user.getLastName();

        Picasso.with(this).load(user.getProfileImageStream()).resize(400, 400).centerCrop().into(civProfileImage);
        tvAddress.setText(user.getAddress());
        tvPhoneNo.setText(user.getPhoneNo());
        tvEmail.setText(user.getEmail());
        tvDob.setText(user.getDateOfBirth());
        tvLivesIn.setText(user.getCity()+ " " + user.getCountry());
        tvName.setText(name);
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
