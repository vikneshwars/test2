package io.mob.resu.reandroidsdk;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Helper class for showing and canceling new message
 * notifications.
 * <p>
 * This class makes heavy use of the {@link NotificationCompat.Builder} helper
 * class to create notifications in a backward-compatible way.
 */

public class AppNotification {


    /**
     * The unique identifier for this type of notification.
     */
    private static final String NOTIFICATION_TAG = "NewMessage";
    private static int NOTIFICATION_ID = 0;

    /**
     * Cancels any notifications of this type previously shown using
     */
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context, int id) {
        try {
            final NotificationManager nm = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                nm.cancel(NOTIFICATION_TAG, id);
            } else {
                nm.cancel(id);
            }
        } catch (Exception e) {
            Util.catchMessage(e);
        }
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private void notify(final Context context, final Notification notification) {

        final NotificationManager nm;
        try {
            nm = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            Log.e("notificationId", "" + NOTIFICATION_ID);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                nm.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notification);
            } else {
                nm.notify(NOTIFICATION_ID, notification);
            }
        } catch (Exception e) {
           Util.catchMessage(e);
        }

    }

    private NotificationCompat.Action getActionIntent(Context context, Bundle bundle, int icon, String actionName) {

        Intent actionIntent = null;
        actionIntent = new Intent(context, NotificationActionReceiver.class);
        bundle.putString("clickActionName", actionName);
        actionIntent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_ID, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return new NotificationCompat.Action.Builder(icon, actionName, pendingIntent).build();
    }

    public void showCustomerNotification(final Context context, final String title, final String text, String actionName, Intent intent, Bitmap bitmap) {



        try {
            addNotification( context, text,  title);
            NOTIFICATION_ID = intent.getExtras().getInt(context.getString(R.string.resulticksAppNotificationId));
            int icon;
            icon = R.drawable.ic_touch_app;

            if (TextUtils.isEmpty(intent.getExtras().getString("actionName"))) {
                actionName = "Dismiss";
            } else {
                actionName = intent.getExtras().getString("actionName");
            }

            final NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            // Set appropriate defaults for the notification light, sound,
            // and vibration.
            builder.setDefaults(Notification.DEFAULT_ALL);

            // Set required fields, including the small icon, the
            // notification title, and text.
            builder.setSmallIcon(R.drawable.ic_launcher);


            // All fields below this line are optional.
            // Use a default priority (recognized on devices running Android
            // 4.1 or later)
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

            // Provide a large icon, shown with the notification in the
            // notification drawer on devices running Android 3.0 or later.
            //  .setLargeIcon(picture)

            // Show a number. This is useful when stacking notifications of
            // a single type.
            builder.setNumber(0);

            // Set the pending intent to be initiated when the user touches
            // the notification.

            builder.addAction(getActionIntent(context, intent.getExtras(), icon, actionName));
            builder.setContentIntent(
                    PendingIntent.getActivity(
                            context,
                            NOTIFICATION_ID,
                            intent,
                            PendingIntent.FLAG_CANCEL_CURRENT));
            builder.setContentTitle(title);
            builder.setContentText(text);

            // Automatically dismiss the notification when it is touched.
            if (bitmap != null) {
                builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap)
                        .setBigContentTitle(title)
                        .setSummaryText(text));

            } else {
                builder.setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(text)
                        .setBigContentTitle(title)
                        .setSummaryText(text));
            }

            builder.setAutoCancel(true);
            notify(context, builder.build());
        } catch (Exception e) {
            Util.catchMessage(e);
        }
    }
    private void addNotification(Context context,String message, String title) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("timeStamp", Util.getCurrentUTC());
            jsonObject.put("message", message);
            jsonObject.put("title", title);
            new DataBase(context).insertData(jsonObject.toString(), DataBase.Table.NOTIFICATION_TABLE);
        } catch (JSONException e) {
            Util.catchMessage(e);
        }
    }

    public void showNotification(final Context context, final String title, final String text, String actionName, Intent intent, Bitmap bitmap) {



        try {
            NOTIFICATION_ID = intent.getExtras().getInt(context.getString(R.string.resulticksAppNotificationId));
            int icon;
            icon = R.drawable.ic_touch_app;

            if (TextUtils.isEmpty(intent.getExtras().getString("actionName"))) {
                actionName = "Dismiss";
            } else {
                actionName = intent.getExtras().getString("actionName");
            }

            final NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            // Set appropriate defaults for the notification light, sound,
            // and vibration.
            builder.setDefaults(Notification.DEFAULT_ALL);

            // Set required fields, including the small icon, the
            // notification title, and text.
            builder.setSmallIcon(R.drawable.ic_launcher);


            // All fields below this line are optional.
            // Use a default priority (recognized on devices running Android
            // 4.1 or later)
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

            // Provide a large icon, shown with the notification in the
            // notification drawer on devices running Android 3.0 or later.
            //  .setLargeIcon(picture)

             // Show a number. This is useful when stacking notifications of
            // a single type.
            builder.setNumber(0);

            // Set the pending intent to be initiated when the user touches
            // the notification.

            builder.addAction(getActionIntent(context, intent.getExtras(), icon, actionName));
            builder.setContentIntent(
                    PendingIntent.getActivity(
                            context,
                            NOTIFICATION_ID,
                            intent,
                            PendingIntent.FLAG_CANCEL_CURRENT));
            builder.setContentTitle(title);
            builder.setContentText(text);

            // Automatically dismiss the notification when it is touched.
            if (bitmap != null) {
                builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap)
                        .setBigContentTitle(title)
                        .setSummaryText(text));

            } else {
                builder.setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(text)
                        .setBigContentTitle(title)
                        .setSummaryText(text));
            }

            builder.setAutoCancel(true);
            notify(context, builder.build());
        } catch (Exception e) {
           Util.catchMessage(e);
        }
    }

}
