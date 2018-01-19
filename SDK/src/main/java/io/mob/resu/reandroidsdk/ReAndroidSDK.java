package io.mob.resu.reandroidsdk;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;

import io.mob.resu.reandroidsdk.error.ExceptionTracker;
import io.mob.resu.reandroidsdk.error.Log;


public class ReAndroidSDK {

    private static final String TAG = ReAndroidSDK.class.getSimpleName();
    static ArrayList<MScreenTracker> mScreenTrackers = new ArrayList<>();
    static ActivityLifecycleCallbacks activityLifecycleCallbacks;
    private static Context appContext;
    private static ReAndroidSDK tracker;

    /**
     * Sever call response listener
     */
    private IResponseListener IResponseListener = new IResponseListener() {
        @Override
        public void onSuccess(String response, int flag) {
            Log.e("response", response);
            JSONObject jsonObject;
            try {
                switch (flag) {
                    case AppConstants.SDK_USER_REGISTER:
                        jsonObject = new JSONObject(response);
                        SharedPref.getInstance().setSharedValue(appContext, appContext.getString(R.string.resulticksSharedUserId), jsonObject.optString("userID"));
                        break;
                    case AppConstants.SDK_API_KEY:
                        jsonObject = new JSONObject(response);
                        android.util.Log.e("deviceId",jsonObject.optString(appContext.getString(R.string.resulticksApiParamsDeviceId)));
                        SharedPref.getInstance().setSharedValue(appContext, appContext.getString(R.string.resulticksSharedDatabaseDeviceId), jsonObject.optString(appContext.getString(R.string.resulticksApiParamsDeviceId)));
                        break;

                }
            } catch (Exception e) {
                ExceptionTracker.track(e);
            }
        }


        @Override
        public void onFailure(Throwable throwable, int flag) {
            Log.e("response", throwable.getMessage());
        }

        @Override
        public void showDialog(String response, int flag) {
            Log.e("response", response);

        }

        @Override
        public void showErrorDialog(String errorResponse, int flag) {
            Log.e("response", errorResponse);
        }

        @Override
        public void showInternalServerErrorDialog(String errorResponse, int flag) {
            Log.e("response", errorResponse);
        }

        @Override
        public void logOut(int flag) {
            Log.e("response", "" + flag);
        }

    };

    private ReAndroidSDK(Context context) {
        try {
            registerActivityCallBacks(context);
            appCrashHandler();
            apiCallAPIValidation();
            Util.getAdvertisementId(appContext);
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
    }

    public static ReAndroidSDK getInstance(Context context) {
        try {

            String user = Util.getMetadata(context, context.getString(R.string.resulticksManifestApiKey));
            Log.e(TAG, "" + user);
            if (user != null) {
                user = user.replace("api_key_", "");
                Log.e(TAG, "" + user);
                SharedPref.getInstance().setSharedValue(context, context.getString(R.string.resulticksSharedAPIKey), user);
            } else {
                Toast.makeText(context, "Please add your SDK API KEY", Toast.LENGTH_LONG).show();
            }
            appContext = context;
            Log.e(TAG, "" + tracker);
            if (tracker == null) {

                return tracker = new ReAndroidSDK(context);
            } else
                return tracker;
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
        return tracker;
    }


    public static ReAndroidSDK getInstance(Context context, String appId) {
        appContext = context;
        Log.e(TAG, "" + tracker);
        if (tracker == null) {
            return tracker = new ReAndroidSDK(context);
        } else
            return tracker;
    }

    /**
     * smart link Data provider
     *
     * @param IDeepLinkInterface
     */
    public void InitDeepLink(IDeepLinkInterface IDeepLinkInterface) {
        try {
            if (!TextUtils.isEmpty(SharedPref.getInstance().getStringValue(appContext, appContext.getString(R.string.resulticksSharedReferral)))) {
                IDeepLinkInterface.onDeepLinkData(SharedPref.getInstance().getStringValue(appContext, appContext.getString(R.string.resulticksSharedReferral)));
                IDeepLinkInterface.onInstallDataReceived(SharedPref.getInstance().getStringValue(appContext, appContext.getString(R.string.resulticksSharedReferral)));
            }
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }


    }

    /**
     * FCM OR GCM token receive
     *
     * @param modelRegisterUser
     */
    public void onDeviceUserRegister(MRegisterUser modelRegisterUser) {
        apiCallRegister(modelRegisterUser);
    }

    /**
     * FCM Notification Receiver
     *
     * @param data
     * @return
     */
    public boolean onReceivedCampaign(Map<String, String> data) {
        try {
            if (data.containsKey(appContext.getString(R.string.resulticksApiParamNavigationScreen))) {
                new NotificationHelper(appContext).handleDataMessage(data);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
        return false;
    }

    /**
     * GCM Notification Receiver
     *
     * @param data
     * @return
     */
    public boolean onReceivedCampaign(Bundle data) {

        try {
            if (data.containsKey(appContext.getString(R.string.resulticksApiParamNavigationScreen))) {
                new NotificationHelper(appContext).handleDataMessage(data);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
        return false;
    }


    /**
     * User Moving Location update
     *
     * @param latitude
     * @param longitude
     */
    public void onLocationUpdate(double latitude, double longitude) {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("latitude", latitude);
            jsonObject.put("longitude", longitude);
            DataNetworkHandler.getInstance().onUpdateLocation(jsonObject);

        } catch (Exception e) {
            ExceptionTracker.track(e);
        }

    }

    /**
     * Notification wise Delete
     *
     * @param data
     */
    public void deleteNotification(MData data) {
        new DataBase(appContext).deleteData(data, DataBase.Table.NOTIFICATION_TABLE);
    }

    /**
     * Campaign & User Notification
     *
     * @return
     */
    public ArrayList<MData> getNotifications() {
        try {

            return new DataBase(appContext).getData(DataBase.Table.NOTIFICATION_TABLE);
        } catch (Exception e) {
            ExceptionTracker.track(e);
            return new ArrayList<>();
        }


    }

    public void addNotification(MData data) {
        try {
            JSONObject e = new JSONObject(data.getValues());
            e.put("timeStamp", Util.getCurrentUTC());
            (new DataBase(appContext)).insertData(e.toString(), DataBase.Table.NOTIFICATION_TABLE);
        } catch (Exception var3) {
            Util.catchMessage(var3);
        }

    }

    /**
     * Event type: 1
     *
     * @param eventName
     */
    public void onTrackEvent(String eventName) {
        AppLifecyclePresenter.getInstance().userEventTracking(appContext, eventName);
    }

    /**
     * Event type: 2
     *
     * @param data
     * @param eventName
     */
    public void onTrackEvent(JSONObject data, String eventName) {
        try {
            AppLifecyclePresenter.getInstance().userEventTracking(appContext, data, eventName);
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
    }

    /**
     * SDK App User register
     *
     * @param modelRegisterUser
     */
    private void apiCallRegister(MRegisterUser modelRegisterUser) {

        JSONObject userDetail;
        try {
            userDetail = new JSONObject();
            userDetail.put("deviceId", SharedPref.getInstance().getStringValue(appContext, appContext.getString(R.string.resulticksSharedDatabaseDeviceId)));
            userDetail.put("name", modelRegisterUser.getName());
            userDetail.put("phone", modelRegisterUser.getPhone());
            userDetail.put("email", modelRegisterUser.getEmail());
            userDetail.put("appId", modelRegisterUser.getEmail());
            userDetail.put("deviceToken", modelRegisterUser.getDeviceToken());

            MDeviceData mDeviceData = new MDeviceData(appContext);
            userDetail.put("appId", mDeviceData.getAppId());
            userDetail.put("deviceOs", mDeviceData.getDeviceOs());
            userDetail.put("deviceIdfa", mDeviceData.getDeviceIdfa());
            userDetail.put("packageName", mDeviceData.getPackageName());
            userDetail.put("deviceOsVersion", mDeviceData.getDeviceOsVersion());
            userDetail.put("deviceManufacture", mDeviceData.getDeviceManufacture());
            userDetail.put("deviceModel", mDeviceData.getDeviceModel());
            userDetail.put("appVersionName", mDeviceData.getAppVersionName());
            userDetail.put("appVersionCode", mDeviceData.getAppVersionCode());


            new DataExchanger("sdkRegistration", userDetail.toString(), IResponseListener, AppConstants.SDK_USER_REGISTER).execute();
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }

    }

    /**
     * SDK App id Validation
     */
    private void apiCallAPIValidation() {

        MDeviceData mDeviceData = new MDeviceData(appContext);

        JSONObject userDetail;
        try {

            userDetail = new JSONObject();
            userDetail.put("appId", mDeviceData.getAppId());
            userDetail.put("deviceToken", mDeviceData.getDeviceToken());
            userDetail.put("deviceId", mDeviceData.getDeviceId());
            userDetail.put("deviceOs", mDeviceData.getDeviceOs());
            userDetail.put("deviceIdfa", mDeviceData.getDeviceIdfa());
            userDetail.put("packageName", mDeviceData.getPackageName());
            userDetail.put("deviceOsVersion", mDeviceData.getDeviceOsVersion());
            userDetail.put("deviceManufacture", mDeviceData.getDeviceManufacture());
            userDetail.put("deviceModel", mDeviceData.getDeviceModel());
            userDetail.put("appVersionName", mDeviceData.getAppVersionName());
            userDetail.put("appVersionCode", mDeviceData.getAppVersionCode());
            new DataExchanger("apiKeyValidation", userDetail.toString(), IResponseListener, AppConstants.SDK_API_KEY).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e(TAG, "apiCallAPIValidation");
    }

    /**
     * Activity Add LifecycleListener
     *
     * @param context
     */
    private void registerActivityCallBacks(Context context) {

        try {
            if (activityLifecycleCallbacks == null) {

                Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
                Account[] accounts = AccountManager.get(context).getAccounts();
                for (Account account : accounts) {
                    String possibleEmail = account.name;
                    Log.e("Emails", possibleEmail);
                    if (emailPattern.matcher(account.name).matches()) {
                        // String possibleEmail = account.name;
                        Log.e("Emails", possibleEmail);
                    }
                }
                final Application app = (Application) context.getApplicationContext();
                activityLifecycleCallbacks = new ActivityLifecycleCallbacks();
                app.registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
            }
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
    }


    /**
     * App crash Listener
     */
    private void appCrashHandler() {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable e) {
                // Get the stack trace.
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                Log.e(TAG, "app Crash" + sw.toString());
                killProcessAndExit(sw.toString());
            }
        });
    }

    /**
     * App Exit Listener
     *
     * @param sw
     */
    private void killProcessAndExit(String sw) {
        try {
            activityLifecycleCallbacks.appCrashHandle(sw);
            // Thread.sleep(1000);

            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(10);
        } catch (Exception e1) {
            Util.catchMessage(e1);
        }
    }


}

