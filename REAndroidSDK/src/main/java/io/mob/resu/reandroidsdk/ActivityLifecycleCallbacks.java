package io.mob.resu.reandroidsdk;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Calendar;


/**
 * Created by P Buvaneswaran on 31-07-2017.
 */

class ActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    public static Activity mActivity;
    private final String TAG = this.getClass().getSimpleName();
    String oldActivityName;
    String newActivityName;
    ShakeDetector mShakeDetector;
    private Calendar oldCalendar = Calendar.getInstance();
    private Calendar sCalendar = Calendar.getInstance();
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    /**
     *  Activity onCreate
     *
     * @param activity
     * @param bundle
     */

    @Override
    public void onActivityCreated(final Activity activity, Bundle bundle) {
        try {
            mActivity = activity;
            if (!TrackerHelper.itHasFragment(activity)) {
                AppWidgets.DialogHandler(true);
                TrackerHelper.getInstance().InitTrack(activity);
            }
        } catch (Exception e) {
            Util.catchMessage(e);
        }

    }

    /**
     * Activity  onStart
     *
     * @param activity
     */
    @Override
    public void onActivityStarted(final Activity activity) {
        try {
            snakeEvent(activity);
            mActivity = activity;
            newActivityName = activity.getClass().getSimpleName();
            oldCalendar = sCalendar;
            sCalendar = Calendar.getInstance();
            if (!TrackerHelper.itHasFragment(activity)) {
                AppWidgets.DialogHandler(false);
                TrackerHelper.getInstance().screenTrackingUpdateToServer(activity);
            }
        } catch (Exception e) {
            Util.catchMessage(e);
        }
    }

    /**
     *  Activity onResume
     *
     * @param activity
     */
    @Override
    public void onActivityResumed(Activity activity) {
        try {
            if (mSensorManager != null) {
                mSensorManager.registerListener(mShakeDetector,
                        mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                        SensorManager.SENSOR_DELAY_UI);
            }
        } catch (Exception e) {
            Util.catchMessage(e);
        }

    }

    /**
     *  Activity onPaused
     *
     * @param activity
     */

    @Override
    public void onActivityPaused(Activity activity) {
        try {
            if (mSensorManager != null) {
                mSensorManager.unregisterListener(mShakeDetector);
            }
        } catch (Exception e) {
            Util.catchMessage(e);
        }
    }

    /**
     * Activity onStop
     *
     * @param activity
     */

    @Override
    public void onActivityStopped(final Activity activity) {
        try {
            oldActivityName = activity.getClass().getSimpleName();
            if (!TrackerHelper.itHasFragment(activity))
                TrackerHelper.getInstance().screenTracking(activity, oldCalendar, Calendar.getInstance(), activity.getClass().getSimpleName(), null, null);
        } catch (Exception e) {
            Util.catchMessage(e);
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
            Util.catchMessage(e);
        }


    }


    private void deepLinkDataReset(Activity activity)throws Exception {
        try {
            JSONObject referrerObject = new JSONObject();
            referrerObject.put(activity.getString(R.string.resulticksDeepLinkParamIsNewInstall), false);
            referrerObject.put(activity.getString(R.string.resulticksDeepLinkParamIsViaDeepLinkingLauncher), false);
            SharedPref.getInstance().setSharedValue(mActivity, activity.getString(R.string.resulticksSharedReferral), referrerObject.toString());
            SharedPref.getInstance().setSharedValue(activity, activity.getString(R.string.resulticksSharedCampaignId), "");

        } catch (Exception e) {
            Util.catchMessage(e);
        }
    }

    /**
     * App Crash Data Handler
     *
     * @param appCrash
     */
    public void saveData(String appCrash) throws Exception{
        deepLinkDataReset(mActivity);
        String subScreenName = null;
        TrackerHelper.getInstance().screenTracking(mActivity, oldCalendar, Calendar.getInstance(), mActivity.getClass().getSimpleName(), subScreenName, appCrash);
    }

    private void snakeEvent(final Context context) throws Exception {
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


}
