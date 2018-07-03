package com.msurvey.projectm.msurveyprojectm.instantapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;



public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1500;

    private final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e(TAG, "We here now");

        setContentView(R.layout.activity_splashscreen2);

        final SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
        final String onboardingComplete = "onboarding_complete";

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent mainIntent = new Intent(SplashActivity.this, LoginOrSignUpActivity.class);
                startActivity(mainIntent);
                finish();

            }

        }, SPLASH_TIME_OUT);
    }
}
