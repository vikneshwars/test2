package io.mob.resu.reandroidsdk;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Interakt on 8/22/17.
 */

class Util {
    Context appContext;
    Util Util;

    Util(Context context) {
        this.appContext = context;
    }

    static Util getInstance(Context context) {
        return new Util(context);
    }

    /**
     * Method checks if the app is in background or not
     */
    static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    static void getAdvertisementId(final Context appContext) {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                AdvertisingIdClient.Info idInfo = null;
                try {
                    idInfo = AdvertisingIdClient.getAdvertisingIdInfo(appContext);
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    SharedPref.getInstance().setSharedValue(appContext, appContext.getString(R.string.resulticksSharedAdverId), idInfo.getId());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    SharedPref.getInstance().setSharedValue(appContext, appContext.getString(R.string.resulticksSharedAdverId), "");

                }

                return "";
            }

            @Override
            protected void onPostExecute(String advertId) {
//                Toast.makeText(getApplicationContext(), advertId, Toast.LENGTH_SHORT).show();
            }

        };
        task.execute();
    }

    static String getCurrentUTC() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(Calendar.getInstance().getTime());
    }

    static String getMetadata(Context context, String name) {

        try {
            final ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo.metaData != null && applicationInfo.metaData.containsKey(name)) {
                return applicationInfo.metaData.getString(name, null);
            } else
                return null;

        } catch (Exception e) {
        }
        return null;
    }

    static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    static ArrayList<String> getAllActivity(Context appContext) {
        ArrayList<String> activityList = new ArrayList<>();

        try {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            PackageManager pm = appContext.getPackageManager();
            PackageInfo info = pm.getPackageInfo(appContext.getApplicationContext().getPackageName(), PackageManager.GET_ACTIVITIES);
            ActivityInfo[] list = info.activities;
            for (ActivityInfo activityInfo : list) {
                Log.d("", "ActivityInfo = " + activityInfo.name);
                if (!TextUtils.isEmpty(activityInfo.name) && !activityInfo.name.startsWith("com.google.android"))
                    activityList.add(activityInfo.name);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        return activityList;
    }

    static String getAppVersionName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if (packageInfo.versionName != null)
                return packageInfo.versionName;
            else
                return "";
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return "";
    }

    static String getAppVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

            return "" + packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return "";
    }

    public boolean hasNetworkConnection() {
        ConnectivityManager cm = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the ic_mobile provider's data plan
                return true;
            }
        }
        return false;
    }


}
