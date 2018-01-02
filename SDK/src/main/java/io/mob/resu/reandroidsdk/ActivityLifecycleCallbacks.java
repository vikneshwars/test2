package io.mob.resu.reandroidsdk;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.Calendar;

import io.mob.resu.reandroidsdk.error.ExceptionTracker;
import io.mob.resu.reandroidsdk.error.Log;

import static io.mob.resu.reandroidsdk.Util.deepLinkDataReset;


class ActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    public  Activity mActivity;

    private final String TAG = this.getClass().getSimpleName();
    private String newActivityName;
    //ShakeDetector mShakeDetector;
    private Calendar oldCalendar = Calendar.getInstance();
    private Calendar sCalendar = Calendar.getInstance();
    //private SensorManager mSensorManager;
    //private Sensor mAccelerometer;




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
            //snakeEvent(activity);
            mActivity = activity;
            newActivityName = activity.getClass().getSimpleName();
            oldCalendar = sCalendar;
            sCalendar = Calendar.getInstance();
            AppLifecyclePresenter.getInstance().screenSessionUpdate(activity, newActivityName);

        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
    }

    /**
     * Activity onResume
     *
     * @param activity
     */
    @Override
    public void onActivityResumed(Activity activity) {
       /* try {
            if (mSensorManager != null) {
                mSensorManager.registerListener(mShakeDetector,
                        mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                        SensorManager.SENSOR_DELAY_UI);
            }
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
*/
    }

    /**
     * Activity onPaused
     *
     * @param activity
     */

    @Override
    public void onActivityPaused(Activity activity) {
        /*try {
            if (mSensorManager != null) {
                mSensorManager.unregisterListener(mShakeDetector);
            }
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }*/
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
                AppLifecyclePresenter.getInstance().screenByUserActivity(activity, oldCalendar, Calendar.getInstance(),    activity.getClass().getSimpleName(), null, null);
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
    }


    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

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
        AppLifecyclePresenter.getInstance().screenByUserActivity(mActivity, oldCalendar, Calendar.getInstance(), mActivity.getClass().getSimpleName(), null, appCrash);
    }

   /* private void snakeEvent(final Context context) throws Exception {
        // ShakeDetector initialization
        mShakeDetector = new ShakeDetector();
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {


                // if (BuildConfig.DEBUG) {
                // do something for a debug build

                if (count > 1) {
                    if (EventTrackingListener.isDebugMode) {
                        EventTrackingListener.isDebugMode = false;
                        Toast.makeText(context, "Device Debug Mode Disabled " + count, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(context, "Device Debug Mode Enabled " + count, Toast.LENGTH_SHORT).show();
                        EventTrackingListener.isDebugMode = true;
                    }

                    ShakeDetector.mShakeCount = 0;
                } else if (count < 4) {
                    Toast.makeText(context, "" + count, Toast.LENGTH_SHORT).show();
                }
            }
            //}
        });
    }
*/

}
