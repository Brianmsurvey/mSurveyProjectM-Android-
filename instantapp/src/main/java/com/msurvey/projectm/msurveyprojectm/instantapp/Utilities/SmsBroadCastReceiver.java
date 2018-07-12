package com.msurvey.projectm.msurveyprojectm.instantapp.Utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class SmsBroadCastReceiver extends BroadcastReceiver {

    private static final String TAG = "SmsBroadCastReceiver";

    private String serviceProviderNumber;

    private String serviceProviderSmsCondition;

    private Listener listener;

    public SmsBroadCastReceiver(String serviceProviderNumber, String serviceProviderSmsCondition){
        this.serviceProviderNumber = serviceProviderNumber;
        this.serviceProviderSmsCondition = serviceProviderSmsCondition;
    }

    public SmsBroadCastReceiver(){};



    final SmsManager sms = SmsManager.getDefault();

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e(TAG, "Message detected");
        Toast.makeText(context, "SMS received", Toast.LENGTH_SHORT).show();

        StringBuilder sb  = new StringBuilder();
        sb.append("Action: " + intent.getAction() + "\n");
        sb.append("URI: " + intent.toUri(Intent.URI_INTENT_SCHEME));

        String log = sb.toString();

        Toast.makeText(context, log, Toast.LENGTH_LONG).show();

    }

    void setListener(Listener listener){
        this.listener = listener;
    }

    interface Listener{
        void onTextReceived(String text);
    }
}
