package io.mob.resu.reandroidsdk;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;

import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * Created by P Buvaneswaran on 31-07-2017.
 */


public class ReAndroidSDK {

    private static final String TAG = ReAndroidSDK.class.getSimpleName();

    private static ActivityLifecycleCallbacks activityLifecycleCallbacks;

    private static Context appContext;

    private static ReAndroidSDK tracker;

    private IResponseListener IResponseListener = new IResponseListener() {
        @Override
        public void onSuccess(String response, int flag) {
            Log.e("response", response);
            JSONObject jsonObject;
            try {
                switch (flag) {
                    case AppConstants.SDK_USER_REGISTER:
                        jsonObject = new JSONObject(response);
                        SharedPref.getInstance().setSharedValue(appContext, appContext.getString(R.string.resulticksSharedUserId), jsonObject.getString("UserID"));
                        break;
                    case AppConstants.SDK_API_KEY:
                        jsonObject = new JSONObject(response);
                        SharedPref.getInstance().setSharedValue(appContext, appContext.getString(R.string.resulticksSharedDatabaseDeviceId), jsonObject.getString(appContext.getString(R.string.resulticksApiParamsDeviceId)));
                        break;

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable throwable, int flag) {
        }

        @Override
        public void showDialog(String response, int flag) {
        }

        @Override
        public void showErrorDialog(String errorResponse, int flag) {
        }

        @Override
        public void showInternalServerErrorDialog(String errorResponse, int flag) {

        }

        @Override
        public void logOut(int flag) {
        }

    };

    private ReAndroidSDK(Context context) {
        String user = Util.getMetadata(context, context.getString(R.string.resulticksManifestApiKey));
        Log.e(TAG, "" + user);
        if (user != null) {
            user = user.replace("api_key_", "");
            Log.e(TAG, "" + user);
            SharedPref.getInstance().setSharedValue(context, context.getString(R.string.resulticksSharedAPIKey), user);
        }
        registerActivityCallBacks(context);
        appCrashHandler();
        apiCallAPIValidation();
        Util.getAdvertisementId(appContext);
    }

    public static ReAndroidSDK getInstance(Context context) {
        appContext = context;
        Log.e(TAG, "" + tracker);
        if (tracker == null) {
            return tracker = new ReAndroidSDK(context);
        } else
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

    public void InitDeepLink(IDeepLinkInterface IDeepLinkInterface) {
        IDeepLinkInterface.onDeepLinkData(SharedPref.getInstance().getStringValue(appContext, appContext.getString(R.string.resulticksSharedReferral)));
        IDeepLinkInterface.onInstallDataReceived(SharedPref.getInstance().getStringValue(appContext, appContext.getString(R.string.resulticksSharedReferral)));
    }

    public void onDeviceUserRegister(MRegisterUser modelRegisterUser) {
        apiCallRegister(modelRegisterUser);
    }

    public boolean onReceivedCampaign(Map<String, String> data) {
        if (data.containsKey(appContext.getString(R.string.resulticksApiParamNavigationScreen))) {
            new NotificationHelper(appContext).handleDataMessage(data);
            return true;
        } else {
            return false;
        }
    }

    public boolean onReceivedCampaign(Bundle data) {

        if (data.containsKey(appContext.getString(R.string.resulticksApiParamNavigationScreen))) {
            new NotificationHelper(appContext).handleDataMessage(data);
            return true;
        } else {
            return false;
        }

    }

    public void addNotification(MData data) {

        try {
            JSONObject jsonObject = new JSONObject(data.getValues());
            jsonObject.put("timeStamp", Util.getCurrentUTC());
            new DataBase(appContext).insertData(jsonObject.toString(), DataBase.Table.NOTIFICATION_TABLE);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onLocationUpdate(double latitude, double longitude) {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("latitude", latitude);
            jsonObject.put("longitude", longitude);
            DataNetworkHandler.getInstance().onUpdateLocation(jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void deleteNotification(MData data) {
        new DataBase(appContext).deleteData(data, DataBase.Table.NOTIFICATION_TABLE);
    }

    public ArrayList<MData> getNotifications() {
        try {
            return new DataBase(appContext).getData(DataBase.Table.NOTIFICATION_TABLE);
        }catch (Exception e)
        {
            return new ArrayList<>();
        }


    }

    public void onTrackEvent(String eventName) {
        TrackerHelper.getInstance().userEventTracking(appContext, eventName);
    }

    public void onTrackEvent(JSONObject data, String eventName) {
        TrackerHelper.getInstance().userEventTracking(appContext, data, eventName);
    }

    private void apiCallRegister(MRegisterUser modelRegisterUser) {

        JSONObject userDetail = new JSONObject();
        try {
            userDetail = new JSONObject();
            userDetail.put("deviceId", SharedPref.getInstance().getStringValue(appContext, appContext.getString(R.string.resulticksSharedDatabaseDeviceId)));
            userDetail.put("name", modelRegisterUser.getName());
            userDetail.put("phone", modelRegisterUser.getPhone());
            userDetail.put("email", modelRegisterUser.getEmail());
            userDetail.put("deviceToken", modelRegisterUser.getDeviceToken());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new DataExchanger("sdkRegistration", userDetail.toString(), IResponseListener, AppConstants.SDK_USER_REGISTER).execute();


    }


    private void apiCallAPIValidation() {

        MDeviceData mDeviceData = new MDeviceData(appContext);

        JSONObject userDetail = new JSONObject();
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        new DataExchanger("apiKeyValidation", userDetail.toString(), IResponseListener, AppConstants.SDK_API_KEY).execute();

        Log.e(TAG, "apiCallAPIValidation");
    }


    private void registerActivityCallBacks(Context context) {

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
    }


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

    private void killProcessAndExit(String sw) {
        try {
            activityLifecycleCallbacks.saveData(sw.toString());
            // Thread.sleep(1000);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }


}

