package com.example.ramya.savaari.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.example.ramya.savaari.R;
import com.example.ramya.savaari.util.Constants;

public class SplashActivity extends AppCompatActivity {



    private final int TIME_SPLASH = 1500;

    /**
     * Called when the activity is first created.
     */

    SharedPreferences sharedPreferences;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        mHandler.sendEmptyMessageDelayed(0, TIME_SPLASH);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            SplashActivity.this.finish();
            sharedPreferences = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
            String currentUser = sharedPreferences.getString(Constants.CURRENT_USER, null);
            if (currentUser != null) {
                startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
        }
    };

}
