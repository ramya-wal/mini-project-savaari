package com.example.ramya.savaari.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ramya.savaari.R;
import com.example.ramya.savaari.database.ExampleDao;
import com.example.ramya.savaari.models.User;
import com.example.ramya.savaari.network.APIClient;
import com.example.ramya.savaari.network.APIInterface;
import com.example.ramya.savaari.util.Constants;
import com.example.ramya.savaari.util.ProgressDialogClass;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText etPhoneNo, etPassword;
    Button login;
    TextView signup;

    String phoneNo, password;

    APIInterface apiInterface;
    Map<String, String> headers = new HashMap<>();
    Map<String, String> fields = new HashMap<>();

    ExampleDao exampleDao;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    ProgressDialogClass progressDialogClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedpreferences = getApplicationContext().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);

        etPhoneNo = findViewById(R.id.activity_login_etPhoneNo);
        etPassword = findViewById(R.id.activity_login_etPassword);
        etPhoneNo.setText("");
        etPassword.setText("");

        login = findViewById(R.id.activity_login_btn_login);

        signup = findViewById(R.id.tv_signup);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        headers.put("X-Parse-Application-Id", Constants.APPLICATION_ID);
        headers.put("X-Parse-REST-API-Key", Constants.API_KEY);
        headers.put("X-Parse-Revocable-Session", "1");

        progressDialogClass = new ProgressDialogClass(LoginActivity.this);
        exampleDao = new ExampleDao(getApplicationContext());

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phoneNo = etPhoneNo.getText().toString();
                password = etPassword.getText().toString();

                if (!(phoneNo.isEmpty() || password.isEmpty())) {

                    fields.put("username", phoneNo);
                    fields.put("password", password);

                    progressDialogClass.showProgressDialog();

                    Call<User> call = apiInterface.getUser(headers, fields);
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            User user = response.body();
                            if (user != null) {
                                exampleDao.insertUser(user);

                                editor = sharedpreferences.edit();
                                editor.putString(Constants.CURRENT_USER, user.getObjectId());
                                editor.apply();

                                progressDialogClass.dismissProgressDialog();
                                Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.invalid_credential, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            progressDialogClass.dismissProgressDialog();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), R.string.fill_fields, Toast.LENGTH_SHORT).show();
                }
            }
        });

        SpannableString string = new SpannableString(getResources().getString(R.string.signup));
        string.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), 14, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        string.setSpan(clickableSpan, 14, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        string.setSpan(new UnderlineSpan(), 14, 21, 0);

        signup.setText(string);
        signup.setMovementMethod(LinkMovementMethod.getInstance());
        signup.setHighlightColor(Color.TRANSPARENT);
    }
}
