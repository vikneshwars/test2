package io.mob.resu.reandroidsdk;

import android.app.Activity;
import android.content.Context;

import java.util.Calendar;



 interface IAppLifecycleListener {

    /*void onActivityCreated(Activity activity, Bundle savedInstanceState);

    void onActivityStarted(Activity activity);

    void onActivityResumed(Activity activity);

    void onActivityPaused(Activity activity);

    void onActivityStopped(Activity activity);

    void onActivitySaveInstanceState(Activity activity, Bundle outState);

    void onActivityDestroyed(Activity activity);

    void onFragmentPreAttached(FragmentManager fm, Fragment f, Context context);

    void onFragmentAttached(FragmentManager fm, Fragment f, Context context);

    void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState);

    void onFragmentActivityCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState);

    void onFragmentViewCreated(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState);

    void onFragmentStarted(FragmentManager fm, Fragment f);

    void onFragmentResumed(FragmentManager fm, Fragment f);

    void onFragmentPaused(FragmentManager fm, Fragment f);

    void onFragmentStopped(FragmentManager fm, Fragment f);

    void onFragmentSaveInstanceState(FragmentManager fm, Fragment f, Bundle outState);

    void onFragmentViewDestroyed(FragmentManager fm, Fragment f);

    void onFragmentDestroyed(FragmentManager fm, Fragment f);

    void onFragmentDetached(FragmentManager fm, Fragment f);
*/


    void screenByUserActivity(Context mActivity, Calendar start, Calendar end, String screenName, String subScreenName, String appCrashValue);

    void screenSessionUpdate(Activity mActivity, String screenName);

}
