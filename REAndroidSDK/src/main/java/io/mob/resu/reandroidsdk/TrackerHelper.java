package io.mob.resu.reandroidsdk;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;


/**
 * Created by Interakt on 8/29/17.
 */

class TrackerHelper {

    static FragmentLifecycleCallbacks fragmentLifecycleCallbacks;
    private static TrackerHelper trackerHelper;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private Activity mActivity;
    private ArrayList<JSONObject> events;
    private HashMap<String, String> EditTextMAP;
    private JSONObject referrerObject = new JSONObject();
    // The following are used for the shake detection

    public static TrackerHelper getInstance() {
        if (trackerHelper == null)
            return trackerHelper = new TrackerHelper();
        else
            return trackerHelper;
    }

    public static boolean itHasFragment(Activity activity) {
        try {
            FragmentManager manager = ((AppCompatActivity) activity).getSupportFragmentManager();
            if (manager != null && manager.getFragments() != null && manager.getFragments().size() > 0) {
                Log.e("This Activity Have a", "Fragment");
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
          Util.catchMessage(e);
            return false;
        }
    }

    private static ArrayList<MScreenTracker> getScreenTrackers() throws Exception {
        try {
            ArrayList<MScreenTracker> mScreenTrackers = new ArrayList<>();
            ArrayList<MRecord> mRecords = new ArrayList<>();
            mRecords.add(new MRecord("", "value", "input_email"));
            mRecords.add(new MRecord("", "value", "input_name"));
            mRecords.add(new MRecord("", "length", "input_password"));
            MScreenTracker screenTracker = new MScreenTracker(mRecords, "SignUpActivity", "");
            mScreenTrackers.add(screenTracker);
            return mScreenTrackers;
        } catch (Exception e) {
            Util.catchMessage(e);

        }
        return null;
    }

    /**
     * Tracking Init
     *
     * @param activity
     */

    void InitTrack(Activity activity) {

        try {
            //Campaign Notification
            if (activity.getIntent().getExtras() != null && activity.getIntent().getExtras().containsKey(activity.getString(R.string.resulticksApiParamNavigationScreen))) {
                Bundle bundle = activity.getIntent().getExtras();
                // Show Notification
                isNotificationLaunch(activity, bundle);
                // App notification Dismiss
                AppNotification.cancel(activity, bundle.getInt(activity.getString(R.string.resulticksAppNotificationId)));
            }
            // Deep linking
            isDeepLinkingLaunch(activity);
            registerFragmentLifeCycle(activity);
            addViewEventListener(activity);
        } catch (Exception e) {
            Util.catchMessage(e);
        }
    }

    private void isNotificationLaunch(Activity context, Bundle bundle) {
        try {

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

        } catch (Exception e) {
            Util.catchMessage(e);
        }

    }

    private void isDeepLinkingLaunch(Activity context) {

        this.mActivity = context;
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
            Util.catchMessage(e);
        }

    }

    private void addViewEventListener(Activity context) {
        printFullTree(context);
    }

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
            Util.catchMessage(e);
        }
    }

    private void printFullTree(Activity activity) {
        try {


            // printTree(activity.getWindow().getDecorView().getRootView(), 0);
            MScreenTracker mScreenTracker = GetScreenTracker(activity.getClass().getSimpleName());
            if (mScreenTracker != null) {

                View view = activity.getWindow().getDecorView().getRootView();
                for (MRecord mRecord : mScreenTracker.getMRecord()) {

                    String id = mRecord.getViewId();

                    int resID = activity.getResources().getIdentifier(id, "id", activity.getPackageName());
                    View view1 = view.findViewById(resID);


                    if (view1 != null) {
                        view1.setAccessibilityDelegate(new EventTrackingListener());
                    }
                }


            }
        } catch (Exception e) {
            Util.catchMessage(e);
        }


    }

    /*************************/
    private void printTree(View view, int indent) {
        try {

            if (view.getId() > 0) {
                /*MRecord mRecord=getTag(view);
                if(mRecord!=null) {
                    view.setTag(mRecord);*/
                view.setAccessibilityDelegate(new EventTrackingListener());
                //}
            }

            if (view instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) view;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    printTree(vg.getChildAt(i), indent++);
                }
            }
        } catch (Exception e) {
            Util.catchMessage(e);
        }

    }


    /**
     * Request Making
     */

    void screenTracking(Context mActivity, Calendar start, Calendar end, String screenName, String subScreenName, String appCrashValue) {

        try {

            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            System.out.println(simpleDateFormat.format(new Date()));
            String sDate = simpleDateFormat.format(start.getTime());
            String eDate = simpleDateFormat.format(end.getTime());
            JSONObject screenObject = new JSONObject();
            screenObject.put(mActivity.getString(R.string.resulticksApiParamStartTime), sDate);
            screenObject.put(mActivity.getString(R.string.resulticksApiParamEndTime), eDate);
            showScreenSession(start, end);

            if (subScreenName != null) {
                screenObject.put(mActivity.getString(R.string.resulticksApiParamSubScreenName), subScreenName);
            }
            screenObject.put(mActivity.getString(R.string.resulticksApiParamScreenName), screenName);

            //App Events
            JSONArray eventsListArray = new JSONArray(events);
            screenObject.put(mActivity.getString(R.string.resulticksApiParamEvents), eventsListArray);

            if (EditTextMAP != null) {
                Log.e("EditText Record ", "" + EditTextMAP.size());

                MScreenTracker mScreenTracker = GetScreenTracker(mActivity.getClass().getSimpleName());
                ArrayList<JSONObject> jsonObjects = new ArrayList<>();

                if (mScreenTracker != null) {

                    for (MRecord mRecord : mScreenTracker.getMRecord()) {
                        JSONObject jsonObject = new JSONObject();

                        for (Map.Entry map : EditTextMAP.entrySet()) {

                            if (map.getKey().toString().contains(mRecord.getViewId())) {
                                if (mRecord.getCaptureType().equalsIgnoreCase("value"))
                                    mRecord.setResult(" " + map.getValue());
                                else if (mRecord.getCaptureType().equalsIgnoreCase("length"))
                                    mRecord.setResult("" + map.getValue().toString().length());
                            }
                            System.out.println(map.getKey() + " " + map.getValue());


                        }

                        jsonObject.put("viewID", mRecord.getViewId());
                        jsonObject.put("captureType", mRecord.getCaptureType());
                        jsonObject.put("result", mRecord.getResult());
                        jsonObjects.add(jsonObject);
                    }
                    screenObject.put("filedCapture", new JSONArray(jsonObjects));

                }
            }

            // App Crash
            if (appCrashValue != null) {
                JSONObject appCrash = new JSONObject();
                appCrash.put(mActivity.getString(R.string.resulticksApiParamCrashText), appCrashValue);
                appCrash.put(mActivity.getString(R.string.resulticksApiParamTimeStamp), simpleDateFormat.format(new Date()));
                screenObject.put(mActivity.getString(R.string.resulticksApiParamAppCrash), appCrash);
            }
            dataBaseScreenTrackingAddEntry(mActivity, screenObject);
        } catch (Exception e) {
            Util.catchMessage(e);
        }


    }

    private MScreenTracker GetScreenTracker(String ScreenName) throws Exception {
        try {
            ReAndroidSDK.mScreenTrackers = getScreenTrackers();

            for (MScreenTracker screenTracker : ReAndroidSDK.mScreenTrackers) {
                if (screenTracker.getScreen().contains(ScreenName))
                    return screenTracker;
            }
        } catch (Exception e) {
            Util.catchMessage(e);
        }
        return null;
    }

    /**
     * Show screen Spent Timer
     *
     * @param start
     * @param end
     */
    private void showScreenSession(Calendar start, Calendar end) throws Exception {
        long difference = start.getTime().getTime() - end.getTime().getTime();
        long differenceSeconds = difference / 1000 % 60;
        long differenceMinutes = difference / (60 * 1000) % 60;
        long differenceHours = difference / (60 * 60 * 1000) % 24;
        long differenceDays = difference / (24 * 60 * 60 * 1000);
        System.out.println(differenceDays + " days, ");
        System.out.println(differenceHours + " hours, ");
        System.out.println(differenceMinutes + " minutes, ");
        System.out.println(differenceSeconds + " seconds.");
    }

    void screenTrackingUpdateToServer(Activity mActivity) throws Exception {
        if (mActivity.getCallingActivity() != null) {
            DataNetworkHandler.getInstance().onMakeTrackingRequest(mActivity);
        }
        printFullTree(mActivity);
    }

    /**
     * Screen wise Database entry
     */
    private void dataBaseScreenTrackingAddEntry(Context context, final JSONObject jsonObject) throws Exception {
        try {
            new DataBase(context).insertData(jsonObject.toString(), DataBase.Table.SCREENS_TABLE);
        } catch (Exception e) {
            Util.catchMessage(e);
        }
    }

    private String getCurrentUTC() throws Exception {
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(Calendar.getInstance().getTime());
    }

    public void EditTextTracking(View host)  {

        try {
            if (EditTextMAP == null)
                EditTextMAP = new HashMap<String, String>();

            EditTextMAP.put(host.getResources().getResourceName(host.getId()), ((EditText) host).getText().toString());
        } catch (Resources.NotFoundException e) {
            Util.catchMessage(e);
        }

    }


    public void autoEventTracking(View host) {

        try {
            if (events == null)
                events = new ArrayList<>();
            String timeStamp = getCurrentUTC();
            JSONObject eventObject = new JSONObject();
            Context mContext = host.getContext();
            eventObject.put(mContext.getString(R.string.resulticksApiParamEventName), "AutoEvent");
            eventObject.put(mContext.getString(R.string.resulticksApiParamScreenName), host.getContext().getClass().getSimpleName());
            eventObject.put(mContext.getString(R.string.resulticksApiParamViewId), host.getResources().getResourceName(host.getId()));
            eventObject.put(mContext.getString(R.string.resulticksApiParamTimeStamp), timeStamp);
            events.add(eventObject);
        } catch (Exception e) {
            Util.catchMessage(e);
        }

    }

    public void userEventTracking(Context context, JSONObject data, String eventName) throws Exception {

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
            Util.catchMessage(e);
        }

    }

    public void userEventTracking(Context context, String eventName) {

        try {
            if (events == null)
                events = new ArrayList<>();
            String timeStamp = getCurrentUTC();
            JSONObject eventObject = new JSONObject();
            eventObject.put(context.getString(R.string.resulticksApiParamEventName), eventName);
            eventObject.put(context.getString(R.string.resulticksApiParamTimeStamp), timeStamp);
            events.add(eventObject);

        } catch (Exception e) {
            Util.catchMessage(e);
        }

    }

    /*************************/
    private void campaignTracker(Context context, String id) throws JSONException {
        DataNetworkHandler.getInstance().campaignHandler(context, id, "2", false, null, null);
    }


}
