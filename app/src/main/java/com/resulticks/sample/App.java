package com.resulticks.sample;

import android.app.Application;
import android.os.StrictMode;

import io.mob.resu.reandroidsdk.ReAndroidSDK;


/**
 * Created by Interakt on 11/21/17.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().build());

        ReAndroidSDK.getInstance(this);
    }
}
