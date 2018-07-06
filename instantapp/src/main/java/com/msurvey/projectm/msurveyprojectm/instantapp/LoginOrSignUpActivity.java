package com.msurvey.projectm.msurveyprojectm.instantapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.msurvey.projectm.msurveyprojectm.instantapp.Utilities.HTTPDataHandler;
import com.msurvey.projectm.msurveyprojectm.instantapp.Utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class LoginOrSignUpActivity extends AppCompatActivity {

    private static final String EMAIL = "email";

    private static final String TAG = "Log in says this: ";

    private LinearLayout layoutRest;

    private TextView SigningIn;

    private ProgressBar progressBar;

    CallbackManager callbackManager = CallbackManager.Factory.create();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loginorsignup);


        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();


        Button loginButton = findViewById(R.id.login_button);
        //loginButton.setReadPermissions(Arrays.asList(EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);

        Button signup = findViewById(R.id.btn_signup);

        layoutRest = findViewById(R.id.layout_rest);

        SigningIn = findViewById(R.id.tv_signing_in);

        progressBar = findViewById(R.id.progress_bar);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(NetworkUtils.getPhoneNumber().equals("0")){
                    new ProfileAsyncTask().execute(NetworkUtils.getTestUrl());
                }else{
                    Intent mainIntent = new Intent(LoginOrSignUpActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                }



            }
        });



        // Callback registration
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                // App code
//            }
//
//            @Override
//            public void onCancel() {
//                // App code
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                // App code
//            }
//        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent loginIntent = new Intent(LoginOrSignUpActivity.this, LoginActivity.class);
                startActivity(loginIntent);

                //Facebook auth logic
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }



    private class ProfileAsyncTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            SigningIn.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            layoutRest.setVisibility(View.INVISIBLE);

        }

        @Override
        protected String doInBackground(String... url) {

            if(url.length == 0) {
                //no url
                return null;
            }


            String stream;
            String urlString = url[0];

            HTTPDataHandler httpDatahandler = new HTTPDataHandler();
            stream = httpDatahandler.GetHTTPData(urlString);

            return stream;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            if(s != null){

                try{
                    //Get HTTP as JSONObject
                    JSONObject reader = new JSONObject(s);


                    //Get JSON Object Results
                    String response = reader.getString("response");
                    JSONObject outerDocs = new JSONObject(response);

                    JSONArray docsArray = outerDocs.getJSONArray("docs");
                    String[] docs = new String[docsArray.length()];
                    for(int i = 0; i<docsArray.length(); i++){
                        docs[i] = docsArray.getString(i);
                    }

                    JSONObject profileJSON = new JSONObject(docs[0]);

//                    Retrieve Phone Number
                    String phoneNum = profileJSON.getString("commId");

//                    String Array to capture financial incentive so far;
                    ArrayList<String> survIncentives = new ArrayList<>();


                    //Retrieve the current incentive from every row entry
                    for(int i=0; i<docs.length; i++){
                        JSONObject current = new JSONObject(docs[i]);

//                      Add incentives to the String array
                        if(current.getString("surveyIncentive") != null){
                            survIncentives.add(current.getString("surveyIncentive"));
                        }

                    }

                    //Retrieve Number of surveys done
                    int incentivesNo = survIncentives.size();

                    //Compute total airtime earned
                    int totalAirtimeEarned = 0;
//
                    for(int i=0; i<incentivesNo; i++){

                        totalAirtimeEarned = totalAirtimeEarned + Integer.parseInt(survIncentives.get(i));
                    }
//
                    String airtime = String.valueOf(totalAirtimeEarned);
                    //

                    //Pass the values to the profile fragment via the NetworkUtils class
                    NetworkUtils.setAirtimeEarned(airtime);
                    NetworkUtils.setPhoneNumber(phoneNum);
                    NetworkUtils.setSurveysCompletedNo(String.valueOf(incentivesNo));




                    Intent mainIntent = new Intent(LoginOrSignUpActivity.this, MainActivity.class);
                    startActivity(mainIntent);

                    SigningIn.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    layoutRest.setVisibility(View.VISIBLE);

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

            if(s != null){

            }

        }

    }



}
