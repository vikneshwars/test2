package io.mob.resu.reandroidsdk;

import android.app.Activity;
import android.content.Context;

import java.util.Calendar;


interface IAppLifecycleListener {

    void onSessionStop(Context context, Calendar start, Calendar end, String screenName, String subScreenName, String appCrash);

    void onSessionStart(Activity mActivity, String screenName,boolean isFragment);

}

