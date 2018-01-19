package io.mob.resu.reandroidsdk;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.Calendar;

import io.mob.resu.reandroidsdk.error.ExceptionTracker;
import io.mob.resu.reandroidsdk.error.Log;

import static io.mob.resu.reandroidsdk.Util.deepLinkDataReset;

class ActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    private final String TAG = this.getClass().getSimpleName();
    Activity mActivity;
    private String newActivityName;
    private Calendar oldCalendar = Calendar.getInstance();
    private Calendar sCalendar = Calendar.getInstance();

    /**
     * Activity onCreate
     *
     * @param activity
     * @param bundle
     */

    @Override
    public void onActivityCreated(final Activity activity, Bundle bundle) {
        mActivity = activity;
        AppLifecyclePresenter.getInstance().Init(activity);
    }


    /**
     * Activity  onStart
     *
     * @param activity
     */
    @Override
    public void onActivityStarted(final Activity activity) {
        try {
            mActivity = activity;
            newActivityName = activity.getClass().getSimpleName();
            oldCalendar = sCalendar;
            sCalendar = Calendar.getInstance();
            AppLifecyclePresenter.getInstance().onSessionStart(activity, activity.getClass().getSimpleName(),false);
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
    }

    /**
     * Activity onResume
     * @param activity
     */
    @Override
    public void onActivityResumed(Activity activity) {

    }

    /**
     * Activity onPaused
     *
     * @param activity
     */

    @Override
    public void onActivityPaused(Activity activity) {
    }

    /**
     * Activity onStop
     *
     * @param activity
     */

    @Override
    public void onActivityStopped(final Activity activity) {
        try {
            if (Util.itHasFragment(activity))
                AppLifecyclePresenter.getInstance().onSessionStop(activity, oldCalendar, Calendar.getInstance(), activity.getClass().getSimpleName(), null, null);
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
    }


    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) { }

    @Override
    public void onActivityDestroyed(Activity activity) {
        try {
            if (activity.getClass().getSimpleName().equalsIgnoreCase(newActivityName)) {
                deepLinkDataReset(activity);
                Log.e(TAG, "App Terminated");
            } else {
                Log.e(TAG, "App Continue");
            }
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }


    }

    /**
     * App Crash Data Handler
     *
     * @param appCrash
     */
    public void appCrashHandle(String appCrash) {
        deepLinkDataReset(mActivity);
        AppLifecyclePresenter.getInstance().onSessionStop(mActivity, oldCalendar, Calendar.getInstance(), mActivity.getClass().getSimpleName(), null, appCrash);
    }

}
