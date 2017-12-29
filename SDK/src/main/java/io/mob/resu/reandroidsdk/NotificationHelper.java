package io.mob.resu.reandroidsdk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Random;

import io.mob.resu.reandroidsdk.error.ExceptionTracker;
import io.mob.resu.reandroidsdk.error.Log;

/**
 * Created by Interakt on 9/14/17.
 */

class NotificationHelper {

    private static Context appContext;

    public NotificationHelper(Context context) {
        appContext = context;
    }

    @NonNull
    public Intent getIntent(Map<String, String> map) {
        Intent intent = new Intent();
        try {

            intent = new Intent(appContext, Class.forName(map.get("navigationScreen")));
            for (String value : map.keySet()) {
                intent.putExtra(value, map.get(value));
            }
            intent.putExtra("notificationId", new Random().nextInt(50) + 1);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            return intent;
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
        return intent;

    }

    @NonNull
    public Intent getIntent(Bundle map) {
        Intent intent = new Intent();
        try {

            intent = new Intent(appContext, Class.forName(map.getString("navigationScreen")));

            Object[] parameters = map.keySet().toArray();
            for (Object o : parameters) {
                String key = "" + o;
                String value = "" + map.get(key);
                Log.e("key", "" + o);
                Log.e("values", "" + map.get(key));
                intent.putExtra(key, value);
            }

            intent.putExtra("notificationId", new Random().nextInt(50) + 1);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            return intent;
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
        return intent;

    }

    public void handleDataMessage(Bundle map) {
        try {
            if (map.containsKey("navigationScreen")) {
                String title = map.getString("title");
                String body = map.getString("body");
                String category = map.getString("category");
                String url = map.getString("url");
                addNotification(body, title);
                if (category.equalsIgnoreCase("Splash")) {
                    bannerNotification(getIntent(map), title, body, category, url);
                } else if (category.equalsIgnoreCase("Rating"))
                    ratingNotification(getIntent(map), title, body, category, url);
                else {
                    if (!TextUtils.isEmpty(url))
                        new PictureStyleNotification(appContext, title, body, url, category, getIntent(map));
                    else
                        new AppNotification().showNotification(appContext, title, body, category, getIntent(map), null);
                }


            }
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }

    }


    public void handleDataMessage(Map<String, String> map) {
        try {
            if (map.containsKey("navigationScreen")) {
                String title = map.get("title");
                String body = map.get("body");
                String category = map.get("category");
                String url = map.get("url");

                if (category.equalsIgnoreCase("Splash")) {
                    bannerNotification(getIntent(map), title, body, category, url);
                } else if (category.equalsIgnoreCase("Rating"))
                    ratingNotification(getIntent(map), title, body, category, url);
                else {
                    if (!TextUtils.isEmpty(url))
                        new PictureStyleNotification(appContext, title, body, url, category, getIntent(map));
                    else
                        new AppNotification().showNotification(appContext, title, body, category, getIntent(map), null);
                }


            }
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }

    }

    private void addNotification(String message, String title) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("timeStamp", Util.getCurrentUTC());
            jsonObject.put("message", message);
            jsonObject.put("title", title);
            new DataBase(appContext).insertData(jsonObject.toString(), DataBase.Table.NOTIFICATION_TABLE);
        } catch (JSONException e) {
            ExceptionTracker.track(e);
        }
    }

    private void ratingNotification(Intent intent, String title, String body, String category, String url) {
        try {
            if (Util.isAppIsInBackground(appContext))
                new PictureStyleNotification(appContext, title, body, url, category, intent);
            else {
                if (ActivityLifecycleCallbacks.mActivity != null)
                    new AppWidgets().showRatingDialog(ActivityLifecycleCallbacks.mActivity, title, body, intent);
                else
                    new PictureStyleNotification(appContext, title, body, url, category, intent);
            }
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
    }

    private void bannerNotification(Intent intent, String title, String body, String category, String url) {
        try {
            if (Util.isAppIsInBackground(appContext))
                new PictureStyleNotification(appContext, title, body, url, category, intent);
            else {
                if (ActivityLifecycleCallbacks.mActivity != null)
                    new AppWidgets().showBannerDialog(ActivityLifecycleCallbacks.mActivity, title, body, intent, url);
                else
                    new PictureStyleNotification(appContext, title, body, url, category, intent);
            }
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
    }

}
