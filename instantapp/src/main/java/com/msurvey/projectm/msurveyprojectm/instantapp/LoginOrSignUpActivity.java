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

    CallbackManager callbackManager = CallbackManager.Factory.create();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loginorsignup);


        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();


        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);

        Button signup = findViewById(R.id.btn_signup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = "https://dev.msurvey.co.ke:8984/solr/leen/select?q=commId:%22%2B254713740504%22&fq=-surveyIncentive:0&wt=json";
                new ProfileAsyncTask().execute(url);

            }
        });

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = "https://dev.msurvey.co.ke:8984/solr/leen/select?q=commId:%22%2B254713740504%22&fq=-surveyIncentive:0&wt=json";
                new ProfileAsyncTask().execute(url);
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

        }

        @Override
        protected String doInBackground(String... url) {

            if(url.length == 0) {
                //no url
                return null;
            }


            String stream;
            String urlString = url[0];
            Log.e(TAG, urlString);

            HTTPDataHandler httpDatahandler = new HTTPDataHandler();
            stream = httpDatahandler.GetHTTPData(urlString);
            Log.e(TAG, stream);

            return stream;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //Log.e(TAG, s);

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

                    //Set profile information
                    //profile.setCommId(profileJSON.getString("commId"));
                    String phoneNum = profileJSON.getString("commId");

                    ArrayList<String> survIncentives = new ArrayList<>();

//                    String mes = "The length of docs is : " + String.valueOf(docs.length);
//                    //Log.e(TAG, mes);
//                    JSONObject current = new JSONObject(docs[3]);
//                    mes = "The current incentive is : " + current.getString("surveyIncentive");
//                    Log.e(TAG, mes);

                    for(int i=0; i<docs.length; i++){
                        JSONObject current = new JSONObject(docs[i]);

                        //Log.e(TAG, current.getString("surveyIncentive"));

                        if(current.getString("surveyIncentive") != null){
                            survIncentives.add(current.getString("surveyIncentive"));
                        }

                    }


                    int incentivesNo = survIncentives.size();
                    int totalAirtimeEarned = 0;
//
                    for(int i=0; i<incentivesNo; i++){

                        totalAirtimeEarned = totalAirtimeEarned + Integer.parseInt(survIncentives.get(i));
                    }
//
                    String airtime = String.valueOf(totalAirtimeEarned);

                    //profile.setAirtimeEarned(value);

                    NetworkUtils.setAirtimeEarned(airtime);
                    NetworkUtils.setPhoneNumber(phoneNum);
                    NetworkUtils.setSurveysCompletedNo(String.valueOf(incentivesNo));


                    Intent mainIntent = new Intent(LoginOrSignUpActivity.this, MainActivity.class);
                    startActivity(mainIntent);

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
