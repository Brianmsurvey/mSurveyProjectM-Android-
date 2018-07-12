package com.msurvey.projectm.msurveyprojectm.instantapp;

import android.app.Application;
import android.content.IntentFilter;
import android.provider.Telephony;

import com.msurvey.projectm.msurveyprojectm.instantapp.Utilities.SmsBroadCastReceiver;

public class MSurvey extends Application {

    private SmsBroadCastReceiver smsBroadCastReceiver;


    @Override
    public void onCreate() {
        super.onCreate();

        smsBroadCastReceiver = new SmsBroadCastReceiver();
        registerReceiver(smsBroadCastReceiver, new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));


    }

    @Override
    public void onTerminate() {

        unregisterReceiver(smsBroadCastReceiver);
        super.onTerminate();
    }
}
