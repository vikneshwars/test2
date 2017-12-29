package io.mob.resu.reandroidsdk;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import io.mob.resu.reandroidsdk.error.ExceptionTracker;
import io.mob.resu.reandroidsdk.error.Log;

import static io.mob.resu.reandroidsdk.Util.getCurrentUTC;
import static io.mob.resu.reandroidsdk.Util.getTime;


/**
 * Created by Interakt on 8/29/17.
 */

class AppLifecyclePresenter implements IAppLifecycleListener {

    static FragmentLifecycleCallbacks fragmentLifecycleCallbacks;
    private static AppLifecyclePresenter trackerHelper;

    private ArrayList<JSONObject> events;
    private HashMap<String, String> EditTextMAP;
    private JSONObject referrerObject = new JSONObject();

    public static AppLifecyclePresenter getInstance() {
        if (trackerHelper == null)
            return trackerHelper = new AppLifecyclePresenter();
        else
            return trackerHelper;
    }

    /**
     * Get Screen wise field capture list
     *
     * @return
     * @throws Exception
     */
    private static ArrayList<MScreenTracker> getScreenTrackers() throws Exception {
        try {
            ArrayList<MScreenTracker> mScreenTrackers = new ArrayList<>();
            ArrayList<MRecord> mRecords = new ArrayList<>();
            mRecords.add(new MRecord("", "value", "input_email"));
            mRecords.add(new MRecord("", "value", "input_name"));
            mRecords.add(new MRecord("", "length", "input_password"));
            mRecords.add(new MRecord("", "action", "btnRegister", "New Register"));

            ArrayList<MRecord> mRecords1 = new ArrayList<>();
            mRecords1.add(new MRecord("", "value", "email"));
            mRecords1.add(new MRecord("", "length", "password"));
            mRecords1.add(new MRecord("", "action", "btnLogin", "User Login"));

            ArrayList<MRecord> mRecords2 = new ArrayList<>();
            mRecords2.add(new MRecord("", "value", "amount"));
            mRecords2.add(new MRecord("", "action", "tv_personal_loan", "Personal Loan"));

            ArrayList<MRecord> mRecords3 = new ArrayList<>();
            mRecords3.add(new MRecord("", "value", "ed_name"));
            mRecords3.add(new MRecord("", "value", "et_phone"));
            mRecords3.add(new MRecord("", "value", "et_email"));
            mRecords3.add(new MRecord("", "action", "btnbuynow", "Submit"));


            MScreenTracker screenTracker = new MScreenTracker(mRecords, "SignUpActivity", "");
            MScreenTracker screenTracker0 = new MScreenTracker(mRecords, "LauncherActivity", "ApplicationFormFragment");
            MScreenTracker screenTracker1 = new MScreenTracker(mRecords1, "LoginActivity", "");
            MScreenTracker screenTracker2 = new MScreenTracker(mRecords2, "LauncherActivity", "DashboardFragment");
            mScreenTrackers.add(screenTracker);
            mScreenTrackers.add(screenTracker0);
            mScreenTrackers.add(screenTracker1);
            mScreenTrackers.add(screenTracker2);


            return mScreenTrackers;
        } catch (Exception e) {
            ExceptionTracker.track(e);

        }
        return null;
    }

    /**
     * App lifecycle listener Init asserts
     *
     * @param activity
     */
    void Init(Activity activity) {
        try {

            if (!Util.itHasFragment(activity)) {

                AppWidgets.DialogHandler(true);

                showSplashNotification(activity);

                isDeepLinkingLaunch(activity);

                registerFragmentLifeCycle(activity);

                setIdWiseTracking(activity, activity.getClass().getSimpleName());
            }

        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
    }

    /**
     * Notification via launch show the Splash, Banner, Serves, Rating Notification
     *
     * @param context
     */
    private void showSplashNotification(Activity context) {

        try {
            if (context.getIntent().getExtras() != null && context.getIntent().getExtras().containsKey(context.getString(R.string.resulticksApiParamNavigationScreen))) {
                Bundle bundle = context.getIntent().getExtras();
                // Show Notification

                if (bundle.getString(context.getString(R.string.resulticksApiParamCategory)).equalsIgnoreCase(context.getString(R.string.resulticksNotificationTypeRating)) || bundle.getString(context.getString(R.string.resulticksApiParamCategory)).equalsIgnoreCase(context.getString(R.string.resulticksNotificationTypeQuickSurvey)) || bundle.getString(context.getString(R.string.resulticksApiParamCategory)).equalsIgnoreCase(context.getString(R.string.resulticksNotificationTypeSplash))) {
                    if (bundle.getString(context.getString(R.string.resulticksApiParamCategory)).equalsIgnoreCase(context.getString(R.string.resulticksNotificationTypeRating))) {
                        new AppWidgets().showRatingDialog(context, bundle.getString(context.getString(R.string.resulticksApiParamTitle)), bundle.getString(context.getString(R.string.resulticksApiParamBody)), context.getIntent());
                    } else if (bundle.getString(context.getString(R.string.resulticksApiParamCategory)).equalsIgnoreCase(context.getString(R.string.resulticksNotificationTypeSplash))) {
                        new AppWidgets().showBannerDialog(context, bundle.getString(context.getString(R.string.resulticksApiParamTitle)), bundle.getString(context.getString(R.string.resulticksApiParamBody)), context.getIntent(), bundle.getString(context.getString(R.string.resulticksApiParamUrl)));
                    } else if (bundle.getString(context.getString(R.string.resulticksApiParamCategory)).equalsIgnoreCase(context.getString(R.string.resulticksNotificationTypeQuickSurvey))) {
                        new AppWidgets().showServeyDialog(context, bundle.getString(context.getString(R.string.resulticksApiParamTitle)), bundle.getString(context.getString(R.string.resulticksApiParamBody)), context.getIntent(), bundle.getString(context.getString(R.string.resulticksApiParamUrl)));
                    }
                }
                SharedPref.getInstance().setSharedValue(context, context.getString(R.string.resulticksSharedCampaignId), bundle.getString(context.getString(R.string.resulticksApiParamId)));
                campaignTracker(context, bundle.getString(context.getString(R.string.resulticksApiParamId)));

                AppNotification.cancel(context, bundle.getInt(context.getString(R.string.resulticksAppNotificationId)));

            }
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }


    }

    /**
     * Smart link app launch Listener
     * @param context
     */
    private void isDeepLinkingLaunch(Activity context) {

        try {
            if (context.getIntent().getExtras() != null && context.getIntent().getExtras().containsKey(context.getString(R.string.resulticksDeepLinkParamReferralId))) {
                referrerObject.put(context.getString(R.string.resulticksDeepLinkParamIsNewInstall), false);
                referrerObject.put(context.getString(R.string.resulticksDeepLinkParamIsViaDeepLinkingLauncher), true);

                Object[] parameters = context.getIntent().getExtras().keySet().toArray();
                for (Object o : parameters) {
                    String key = "" + o;
                    String value = "" + context.getIntent().getExtras().get(key);
                    Log.e("key", "" + o);
                    Log.e("values", "" + context.getIntent().getExtras().get(key));
                    referrerObject.put(key, value);
                }
                // Server update
                if (referrerObject.has(context.getString(R.string.resulticksDeepLinkParamReferralId))) {
                    campaignTracker(context, referrerObject.getString(context.getString(R.string.resulticksDeepLinkParamReferralId)));
                }
                SharedPref.getInstance().setSharedValue(context, context.getString(R.string.resulticksSharedReferral), referrerObject.toString());
                SharedPref.getInstance().setSharedValue(context, context.getString(R.string.resulticksSharedCampaignId), referrerObject.getString(context.getString(R.string.resulticksDeepLinkParamReferralId)));
            }

        } catch (Exception e) {
            ExceptionTracker.track(e);
        }

    }

    /**
     * Add fragment lifecycle register callbacks
     * @param mActivity
     */
    private void registerFragmentLifeCycle(Activity mActivity) {
        // Fragment Screens
        try {
            if (fragmentLifecycleCallbacks == null) {
                if (mActivity instanceof AppCompatActivity) {
                    FragmentManager manager = ((AppCompatActivity) mActivity).getSupportFragmentManager();
                    manager.registerFragmentLifecycleCallbacks(new FragmentLifecycleCallbacks(), true);
                }
            }
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
    }

    /**
     * Each Screen Field capture list wise Adding field capture listener
     * @param activity
     * @param screenName
     */
    private void setIdWiseTracking(Activity activity, String screenName) {

        try {
            //printTree(activity.getWindow().getDecorView().getRootView(), 0);
            MScreenTracker mScreenTracker = GetScreenTracker(screenName);
            if (mScreenTracker != null) {
                View view = activity.getWindow().getDecorView().getRootView();
                for (MRecord mRecord : mScreenTracker.getMRecord()) {
                    String id = mRecord.getIdentifier();
                    int resID = activity.getResources().getIdentifier(id, "id", activity.getPackageName());
                    View view1 = view.findViewById(resID);
                    if (view1 != null) {
                        view1.setAccessibilityDelegate(new EventTrackingListener());
                    }
                }
            }
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }


    }

    /*************************/
    /*private void printTree(View view, int indent) {
        try {

            if (view.getId() > 0) {
                view.setAccessibilityDelegate(new EventTrackingListener());
            }

            if (view instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) view;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    printTree(vg.getChildAt(i), indent++);
                }
            }
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }

    }
*/

    /**
     * Screen wise provide field capture list
     * @param ScreenName
     * @return
     * @throws Exception
     */
    private MScreenTracker GetScreenTracker(String ScreenName) throws Exception {
        try {
            ReAndroidSDK.mScreenTrackers = getScreenTrackers();

            for (MScreenTracker screenTracker : ReAndroidSDK.mScreenTrackers) {
                if (screenTracker.getScreen().contains(ScreenName) || screenTracker.getSubscreen().contains(ScreenName))
                    return screenTracker;
            }
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
        return null;
    }

    /**
     * Field wise capture data Listener
     *
     * @param host
     */
    void fieldWiseDataListener(View host) {

        try {
            if (EditTextMAP == null)
                EditTextMAP = new HashMap<String, String>();
            if (host instanceof EditText)
                EditTextMAP.put(host.getResources().getResourceName(host.getId()), ((EditText) host).getText().toString());
            else
                EditTextMAP.put(host.getResources().getResourceName(host.getId()), "");


        } catch (Resources.NotFoundException e) {
            ExceptionTracker.track(e);
        }

    }

    /**
     * Custom Event Type: 1
     * <p>
     * eventName: i.e Product purchased
     * <p>
     * data:   {"productName:"Nexus 5","amount": "35,000"}
     *
     * @param context
     * @param data
     * @param eventName
     * @throws Exception
     */
    void userEventTracking(Context context, JSONObject data, String eventName) throws Exception {

        try {
            if (events == null)
                events = new ArrayList<>();

            String timeStamp = getCurrentUTC();
            JSONObject eventObject = new JSONObject();
            eventObject.put(context.getString(R.string.resulticksApiParamEventName), eventName);
            eventObject.put(context.getString(R.string.resulticksApiParamData), data);
            eventObject.put(context.getString(R.string.resulticksApiParamTimeStamp), timeStamp);
            events.add(eventObject);
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }

    }

    /**
     * Custom Event Type: 2
     * <p>
     * eventName:  i.e NFC detected, QR Code Scanned
     *
     * @param context
     * @param eventName
     */
    void userEventTracking(Context context, String eventName) {

        try {
            if (events == null)
                events = new ArrayList<>();
            String timeStamp = getCurrentUTC();
            JSONObject eventObject = new JSONObject();
            eventObject.put(context.getString(R.string.resulticksApiParamEventName), eventName);
            eventObject.put(context.getString(R.string.resulticksApiParamTimeStamp), timeStamp);
            events.add(eventObject);

        } catch (Exception e) {
            ExceptionTracker.track(e);
        }

    }

    /**
     * Campaign Wise User engagement update
     *
     * @param context
     * @param id
     * @throws JSONException
     */
    private void campaignTracker(Context context, String id) throws JSONException {
        DataNetworkHandler.getInstance().campaignHandler(context, id, "2", false, null, null);
    }

    /**
     * Screen Session Listener
     *
     * @param context
     * @param start
     * @param end
     * @param screenName
     * @param subScreenName
     * @param appCrashValue
     */
    @Override
    public void screenByUserActivity(Context context, Calendar start, Calendar end, String screenName, String subScreenName, String appCrashValue) {
        try {

            JSONObject screenActivities = new JSONObject();
            screenActivities.put(context.getString(R.string.resulticksApiParamStartTime), getTime(start));
            screenActivities.put(context.getString(R.string.resulticksApiParamEndTime), getTime(end));
            screenActivities.put(context.getString(R.string.resulticksApiParamScreenName), screenName);
            screenActivities.put(context.getString(R.string.resulticksApiParamEvents), new JSONArray(events));
            screenActivities.put(context.getString(R.string.resulticksApiParamSubScreenName), subScreenName);
            getFieldData(context, screenActivities);
            getAppCrashData(context, appCrashValue, screenActivities);
            new DataBase(context).insertData(screenActivities.toString(), DataBase.Table.SCREENS_TABLE);

        } catch (Exception e) {
            ExceptionTracker.track(e);
        }

    }

    /**
     * Get App Crash Reasons
     *
     * @param mActivity
     * @param appCrashValue
     * @param screenObject
     * @throws JSONException
     */
    private void getAppCrashData(Context mActivity, String appCrashValue, JSONObject screenObject) throws JSONException {
        // App Crash
        if (appCrashValue != null) {
            JSONObject appCrash = new JSONObject();
            appCrash.put(mActivity.getString(R.string.resulticksApiParamCrashText), appCrashValue);
            appCrash.put(mActivity.getString(R.string.resulticksApiParamTimeStamp), getCurrentUTC());
            screenObject.put(mActivity.getString(R.string.resulticksApiParamAppCrash), appCrash);
        }
    }

    /**
     * Get Field Wise Tracking Data Capture
     *
     * @param mActivity
     * @param screenObject
     * @throws Exception
     */
    private void getFieldData(Context mActivity, JSONObject screenObject) throws Exception {
        if (EditTextMAP != null) {
            Log.e("EditText Record ", "" + EditTextMAP.size());
            MScreenTracker mScreenTracker = GetScreenTracker(mActivity.getClass().getSimpleName());
            ArrayList<JSONObject> jsonObjects = new ArrayList<>();
            if (mScreenTracker != null) {
                for (MRecord mRecord : mScreenTracker.getMRecord()) {
                    JSONObject jsonObject = new JSONObject();

                    for (Map.Entry map : EditTextMAP.entrySet()) {
                        if (map.getKey().toString().contains(mRecord.getIdentifier())) {
                            if (mRecord.getCaptureType().equalsIgnoreCase("value"))
                                mRecord.setResult("" + map.getValue());
                            else if (mRecord.getCaptureType().equalsIgnoreCase("length"))
                                mRecord.setResult("" + map.getValue().toString().length());
                            else if (mRecord.getCaptureType().equalsIgnoreCase("action"))
                                mRecord.setResult("Clicked");
                        }
                        System.out.println(map.getKey() + " " + map.getValue());
                    }
                    jsonObject.put("viewID", mRecord.getIdentifier());
                    jsonObject.put("captureType", mRecord.getCaptureType());
                    jsonObject.put("result", mRecord.getResult());
                    jsonObject.put("description", mRecord.getDescription());
                    jsonObjects.add(jsonObject);
                }
                screenObject.put("filedCapture", new JSONArray(jsonObjects));

            }
        }
    }

    /**
     * Screen wise User journey
     *
     * @param mActivity
     * @param screenName
     */
    @Override
    public void screenSessionUpdate(Activity mActivity, String screenName) {
        if (!Util.itHasFragment(mActivity)) {
            DataNetworkHandler.getInstance().onMakeTrackingRequest(mActivity);
            setIdWiseTracking(mActivity, screenName);
        }
    }
}
