package com.sapphire.HealthAssessmentPDMA.BroadcastReceiver;

import android.app.Application;
import android.content.IntentFilter;
import android.os.Build;

public class MyApplication extends Application {

    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            ConnectivityReceiver connectivityReceiver = new ConnectivityReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            registerReceiver(connectivityReceiver,intentFilter);
        }
    }

    public static synchronized MyApplication getInstance(){
        return instance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityListener = listener;
    }


}
