package com.resulticks.sample;

import android.app.Application;

import io.mob.resu.reandroidsdk.ReAndroidSDK;


/**
 * Created by Interakt on 11/21/17.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ReAndroidSDK.getInstance(this);
    }
}
