package io.mob.resu.reandroidsdk;

import android.app.Activity;
import android.content.Context;

import java.util.Calendar;

/**
 * Created by Buvaneswaran on 29/12/17.
 */

public interface IAppLifecycleListener {

    void screenByUserActivity(Context mActivity, Calendar start, Calendar end, String screenName, String subScreenName, String appCrashValue);

    void screenSessionUpdate(Activity mActivity, String screenName);

}
