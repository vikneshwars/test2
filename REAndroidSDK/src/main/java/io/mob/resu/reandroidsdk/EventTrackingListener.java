package io.mob.resu.reandroidsdk;

import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.EditText;

/**
 * Created by Interakt on 9/7/17.
 */

 class EventTrackingListener extends View.AccessibilityDelegate {

     static boolean isDebugMode = false;

    @Override
    public void sendAccessibilityEvent(View host, int eventType) {

        if(host instanceof EditText)
        {
            Log.e("sendAccessibilityEvent", ((EditText) host).getText().toString());
        }

        Log.e("sendAccessibilityEvent", "sendAccessibilityEvent");

        if (AccessibilityEvent.TYPE_VIEW_CLICKED == eventType || AccessibilityEvent.TYPE_VIEW_LONG_CLICKED == eventType) {
            TrackerHelper.getInstance().autoEventTracking(host);
            if (isDebugMode)
                AppWidgets.showEventDialog(host);
        }
        super.sendAccessibilityEvent(host, eventType);
    }


}
