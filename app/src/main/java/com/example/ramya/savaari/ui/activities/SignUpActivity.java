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
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.ramya.savaari.R;
import com.example.ramya.savaari.models.SignInResponse;
import com.example.ramya.savaari.models.User;
import com.example.ramya.savaari.network.APIClient;
import com.example.ramya.savaari.network.APIInterface;
import com.example.ramya.savaari.util.Constants;
import com.example.ramya.savaari.util.ProgressDialogClass;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.ramya.savaari.util.Constants.SHOW_DIALOG_ID;

public class SignUpActivity extends AppCompatActivity {

    //User object for holding user details
    User user;

    //Profile Image
    ImageButton ibUploadImage;
    ImageView ivProfileImage;

    //Date of Birth
    TextInputEditText etSetDate;
    Calendar calendar;
    String dateOfBirth;
    int day, month, year;

    RadioGroup rgGender;

    byte[] image;

    EditText etFirstName, etLastName, etAddress, etCity, etCountry, etPhoneNo, etEmail, etPassword, etConfirmPassword;

    Button btnSignUp;

    ProgressDialogClass progressDialogClass;

    APIInterface apiInterface;
    Map<String, String> userDetailUploadHeaders = new HashMap<>();
    Map<String, String> userImageUploadHeaders = new HashMap<>();

    String fName, lName, gender, address, city, country, phoneNo, email, password, password2;
    Date dob;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Parse.initialize(this);

        progressDialogClass = new ProgressDialogClass(SignUpActivity.this);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        userDetailUploadHeaders.put("X-Parse-Application-Id", Constants.APPLICATION_ID);
        userDetailUploadHeaders.put("X-Parse-REST-API-Key", Constants.API_KEY);
        userDetailUploadHeaders.put("X-Parse-Revocable-Session", "1");
        userDetailUploadHeaders.put("Content-Type", "application/json");

        userImageUploadHeaders.put("X-Parse-Application-Id", Constants.APPLICATION_ID);
        userImageUploadHeaders.put("X-Parse-REST-API-Key", Constants.API_KEY);
        userImageUploadHeaders.put("Content-Type", "image/jpeg");

        init();

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        etSetDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                showDialog(SHOW_DIALOG_ID);
            }
        });

        etSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(SHOW_DIALOG_ID);
            }
        });

        ibUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SignUpActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.PHOTO_REQUEST_GALLERY);
                } else {

                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");

                    startActivityForResult(intent, Constants.PHOTO_REQUEST_GALLERY);
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fName = etFirstName.getText().toString();
                lName = etLastName.getText().toString();
                address = etAddress.getText().toString();
                city = etCity.getText().toString();
                country = etCountry.getText().toString();
                phoneNo = etPhoneNo.getText().toString();
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                password2 = etConfirmPassword.getText().toString();

                if (isValid()) {
                    int id = rgGender.getCheckedRadioButtonId();
                    RadioButton radioButton = findViewById(id);
                    gender = radioButton.getText().toString();
                    progressDialogClass.showProgressDialog();

                    ParseFile file = new ParseFile("car.jpg", image);
                    try {
                        file.save();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String url = file.getUrl();

                    uploadUserDetails(url, phoneNo, password, email, fName, lName, gender, dateOfBirth, address, city, country);

                }

            }
        });
    }

    public void init() {
        ibUploadImage = findViewById(R.id.activity_sign_up_ibUploadImage);
        ivProfileImage = findViewById(R.id.activity_sign_up_ivProfileImage);

        etSetDate = findViewById(R.id.activity_sign_up_etSetDate);

        rgGender = findViewById(R.id.activity_sign_up_rgGender);

        etFirstName = findViewById(R.id.activity_sign_up_etFirstName);
        etLastName = findViewById(R.id.activity_sign_up_etLastName);
        etAddress = findViewById(R.id.activity_sign_up_etAddress);
        etCity = findViewById(R.id.activity_sign_up_etCity);
        etCountry = findViewById(R.id.activity_sign_up_etCountry);
        etPhoneNo = findViewById(R.id.activity_sign_up_etPhoneNo);
        etEmail = findViewById(R.id.activity_sign_up_etEmail);
        etPassword = findViewById(R.id.activity_sign_up_etPassword);
        etConfirmPassword = findViewById(R.id.activity_sign_up_etConfirmPassword);
        etSetDate = findViewById(R.id.activity_sign_up_etSetDate);

        btnSignUp = findViewById(R.id.activity_sign_up_btnSignUp);
    }

    public boolean isValid(String password) {
        if (password.length() >= 6) {
            Pattern pattern;
            Matcher matcher;
            final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
            pattern = Pattern.compile(PASSWORD_PATTERN);
            matcher = pattern.matcher(password);

            return matcher.matches();
        } else {
            return false;
        }
    }

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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void showDate(int year, int month, int day) {

        dob = new Date(year - 1900, month, day);
        DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy");
        dateOfBirth = formatter.format(dob);

        etSetDate.setText(dateOfBirth);
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

    public void uploadUserDetails(String url, String phoneNo, String password, String email, String fName, String lName, String gender, String dob, String address, String city, String country) {
        user = new User(url, phoneNo, password, email, fName, lName, gender, dob, phoneNo, address, city, country);

        //Upload Profile Details
        Call<SignInResponse> call = apiInterface.createUser(userDetailUploadHeaders, user);
        call.enqueue(new Callback<SignInResponse>() {
            @Override
            public void onResponse(Call<SignInResponse> call, Response<SignInResponse> response) {
                if (response.errorBody() == null) {
                    SignInResponse signInResponse = response.body();
                    user.setObjectId(signInResponse.getObjectId());
                    progressDialogClass.dismissProgressDialog();
                    finish();
                } else {
                    progressDialogClass.dismissProgressDialog();
                    Toast.makeText(getApplicationContext(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SignInResponse> call, Throwable t) {
                Log.e("LOG", "Failed");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constants.PHOTO_REQUEST_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, Constants.PHOTO_REQUEST_GALLERY);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public boolean isValid() {
        boolean validity = false;
        if (!(fName.isEmpty() || lName.isEmpty() || email.isEmpty() || password.isEmpty() || password2.isEmpty() || address.isEmpty() || city.isEmpty() || country.isEmpty() || phoneNo.isEmpty() || dateOfBirth.isEmpty()) || !(rgGender.getCheckedRadioButtonId() == -1)) {
            if (password.equals(password2)) {
                if (isValid(password)) {
                    if (email.contains("@")) {
                        if (image != null) {
                            validity = true;

                        } else {
                            Toast.makeText(getApplicationContext(), R.string.add_profile_picture, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.invalid_emailId, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), R.string.invalid_password, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), R.string.password_mismatch, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.fill_fields, Toast.LENGTH_SHORT).show();
        }
        return validity;
    }
}

