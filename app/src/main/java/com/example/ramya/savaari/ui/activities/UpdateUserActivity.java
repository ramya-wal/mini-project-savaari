package com.example.ramya.savaari.ui.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ramya.savaari.R;
import com.example.ramya.savaari.database.ExampleDao;
import com.example.ramya.savaari.models.UpdateDetails;
import com.example.ramya.savaari.models.User;
import com.example.ramya.savaari.models.Vehicle;
import com.example.ramya.savaari.network.APIClient;
import com.example.ramya.savaari.network.APIInterface;
import com.example.ramya.savaari.util.Constants;
import com.example.ramya.savaari.util.ProgressDialogClass;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.ramya.savaari.util.Constants.SHOW_DIALOG_ID;

public class UpdateUserActivity extends AppCompatActivity {
    Button btnUpdateUser;

    EditText etFirstName, etLastName,etDob, etAddress, etCity, etCountry, etPhoneNo, etEmail;
    ImageButton ibUploadImage;
    ImageView ivProfileImage;

    Calendar calendar;
    String dateOfBirth;
    int day, month, year;

    byte[] image;
    User user;
    Date dob;

    String fName, lName, address, city, country, phoneno, email;
    APIInterface apiInterface;
    ProgressDialogClass progressDialogClass;

    Map<String, String> headers = new HashMap<>();

    ExampleDao exampleDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        Parse.initialize(this);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        etFirstName = findViewById(R.id.activity_update_user_etFirstName);
        etLastName = findViewById(R.id.activity_update_user_etLastName);
        etAddress = findViewById(R.id.activity_update_user_etAddress);
        etCity = findViewById(R.id.activity_update_user_etCity);

        etCountry = findViewById(R.id.activity_update_user_etCountry);
//        etPhoneNo = findViewById(R.id.activity_update_user_etPhoneNo);
//        etEmail = findViewById(R.id.activity_update_user_etEmail);
        etDob = findViewById(R.id.activity_update_user_etSetDate);
        ibUploadImage = findViewById(R.id.activity_update_user_ibUploadImage);
        ivProfileImage = findViewById(R.id.activity_update_user_ivProfileImage);


        etDob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    showDialog(SHOW_DIALOG_ID);
            }
        });
        btnUpdateUser = findViewById(R.id.activity_update_user_btnUpdate);
        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(SHOW_DIALOG_ID);
            }
        });

        headers.put("X-Parse-Application-Id", Constants.APPLICATION_ID);
        headers.put("X-Parse-REST-API-Key", Constants.API_KEY);
        headers.put("Content-Type", "application/json");

        exampleDao = new ExampleDao(this);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        progressDialogClass = new ProgressDialogClass(this);

        Intent intent = getIntent();
        user = intent.getParcelableExtra("USER");

        etFirstName.setText(user.getFirstName());
        etLastName.setText(user.getLastName());
        etAddress.setText(user.getAddress());
        etCity.setText(user.getCity());
        etCountry.setText(user.getCountry());
        etDob.setText(user.getDateOfBirth());
//        etPhoneNo.setText(user.getPhoneNo());
//        etEmail.setText(user.getEmail());
        Picasso.with(this).load(user.getProfileImageStream()).into(ivProfileImage);

        ibUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(UpdateUserActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UpdateUserActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.PHOTO_REQUEST_GALLERY);
                } else {

                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");

                    startActivityForResult(intent, Constants.PHOTO_REQUEST_GALLERY);
                }
            }
        });
        btnUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fName = etFirstName.getText().toString();
                lName = etLastName.getText().toString();
                address = etAddress.getText().toString();
                city = etCity.getText().toString();
                country = etCountry.getText().toString();
//                phoneno = etPhoneNo.getText().toString();
//                email = etEmail.getText().toString();

                ParseFile file = new ParseFile("car.jpg", image);
                try {
                    file.save();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String url = file.getUrl();

                user.setFirstName(fName);
                user.setLastName(lName);
                user.setAddress(address);
                user.setCountry(country);
                user.setCity(city);
//                user.setPhoneNo(phoneno);
//                user.setEmail(email);
                user.setProfileImageStream(url);
                user.setDateOfBirth(dateOfBirth);
                progressDialogClass.showProgressDialog();
                Call<UpdateDetails> call = apiInterface.updateUser(headers, user, user.getObjectId());
                call.enqueue(new Callback<UpdateDetails>() {
                    @Override
                    public void onResponse(Call<UpdateDetails> call, Response<UpdateDetails> response) {
                        Toast.makeText(getApplicationContext(), R.string.user_updated, Toast.LENGTH_SHORT).show();
                        progressDialogClass.dismissProgressDialog();
                        exampleDao.updateUser(user);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<UpdateDetails> call, Throwable t) {
                        progressDialogClass.dismissProgressDialog();
                    }
                });

            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == Constants.PHOTO_REQUEST_GALLERY) {

            Uri selectedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            if (selectedImage != null) {
                Cursor cursor = getContentResolver().query(selectedImage, filePath, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePath[0]);
                    cursor.moveToFirst();
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                    ivProfileImage.setImageBitmap(thumbnail);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    image = stream.toByteArray();
                }
            }
        }
    }
    private void showDate(int year, int month, int day) {

        dob = new Date(year - 1900, month, day);
        DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy");
        dateOfBirth = formatter.format(dob);

        etDob.setText(dateOfBirth);
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int y, int m, int dayOfMonth) {
            day = dayOfMonth;
            month = m;
            year = y;
            showDate(year, month, dayOfMonth);
        }
    };
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == SHOW_DIALOG_ID) {
            DatePickerDialog dialog = new DatePickerDialog(this, myDateListener, year, month, day);

            Calendar calendar = Calendar.getInstance();
//            calendar.add(Calendar.YEAR, -10);
            dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
            calendar.add(Calendar.YEAR, -90);
            dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            return dialog;
        }
        return null;
    }
}
