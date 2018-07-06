package com.msurvey.projectm.msurveyprojectm.instantapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.msurvey.projectm.msurveyprojectm.instantapp.Utilities.HTTPDataHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Provides UI for the main screen.
 */
public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private CircleImageView mAvator;
    private Profile profile;


    private static final String TAG = "MainActivity.java";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "We here now");


        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String messagen = "The message arrived : " + getIntent().getExtras().getString("airtime");

        fragment_profile f = new fragment_profile();
        Bundle bundle = new Bundle();
        bundle.putString("some", messagen);
        f.setArguments(bundle);

        Log.e(TAG, messagen);


        // Setting ViewPager for each Tabs
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);


        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


        // Create Navigation drawer and inlfate layout
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);


        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            VectorDrawableCompat indicator
                    = VectorDrawableCompat.create(getResources(), R.drawable.ic_menu, getTheme());
            indicator.setTint(ContextCompat.getColor(this ,R.color.white));
            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }




        // Set behavior of Navigation drawer
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Set item in checked state
                        menuItem.setChecked(true);

                        int id = menuItem.getItemId();

                        if(id == R.id.item_profile){

                            return true;
                        }

                        else if(id == R.id.item_about_msurvey){

                            return true;
                        }

                        // Closing drawer on item click
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
        // Adding Floating Action Button to bottom right of main view
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Hello there!",
                        Snackbar.LENGTH_LONG).show();
            }
        });

    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new fragment_profile(), "Profile");
        adapter.addFragment(new fragment_surveys(), "Surveys");
        adapter.addFragment(new fragment_cashbacks(), "Cashbacks");
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement


        if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

    private class ProfileAsyncTask extends AsyncTask<String, Void, String>{


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

            Log.e(TAG, s);

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
                    profile.setCommId(profileJSON.getString("commId"));

//                    String mes = "The length of docs is : " + String.valueOf(docs.length);
//                    //Log.e(TAG, mes);
//                    JSONObject current = new JSONObject(docs[3]);
//                    mes = "The current incentive is : " + current.getString("surveyIncentive");
//                    Log.e(TAG, mes);

                    for(int i=0; i<docs.length; i++){
                        JSONObject current = new JSONObject(docs[i]);

                        //Log.e(TAG, current.getString("surveyIncentive"));

                        if(current.getString("surveyIncentive") != null){
                            profile.setSurveyIncentives(current.getString("surveyIncentive"));
                        }

                    }


                    int incentivesNo = profile.getSurveyIncentives().size();
                    int totalAirtimeEarned = 0;
//
                    for(int i=0; i<incentivesNo; i++){

                        totalAirtimeEarned = totalAirtimeEarned + Integer.parseInt(profile.getSurveyIncentives().get(i));
                    }
//
                    String value = String.valueOf(totalAirtimeEarned);

                    profile.setAirtimeEarned(value);

                    fragment_profile f = new fragment_profile();
                    Bundle args = new Bundle();
                    args.putString("airtime", value);
                    f.setArguments(args);
                    String mess = "The bundle contains some of that: " + args.get("airtime");
                    Log.e(TAG, mess);


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
