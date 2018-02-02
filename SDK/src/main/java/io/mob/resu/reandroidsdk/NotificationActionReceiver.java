package io.mob.resu.reandroidsdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import io.mob.resu.reandroidsdk.error.ExceptionTracker;
import io.mob.resu.reandroidsdk.error.Log;



public class NotificationActionReceiver extends BroadcastReceiver {

    IResponseListener IResponseListener = new IResponseListener() {
        @Override
        public void onSuccess(String response, int flag) {
            Log.e("response", response);
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
            Log.e("response", "InternalServer");
        }

        @Override
        public void logOut(int flag) {

        }
    };

    @Override
    public void onReceive(Context context, Intent data) {

        try {
            Bundle bundles = data.getExtras();
            DismissWindow(context);
            DataNetworkHandler.getInstance().campaignHandler(context, bundles.getString(context.getString(R.string.resulticksApiParamId)), "3", false, null, null);
            AppNotification.cancel(context, bundles.getInt(context.getString(R.string.resulticksAppNotificationId)));
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }


    }


    private void DismissWindow(Context context) {
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(it);
    }


}
