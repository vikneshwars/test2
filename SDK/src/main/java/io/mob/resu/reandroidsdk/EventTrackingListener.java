package io.mob.resu.reandroidsdk;

import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Interakt on 9/7/17.
 */

class EventTrackingListener extends View.AccessibilityDelegate {

    static boolean isDebugMode = false;
    static ArrayList<HashMap> Events = new ArrayList<>();


    @Override
    public void sendAccessibilityEvent(View host, int eventType) {

        if (host instanceof EditText) {
            TrackerHelper.getInstance().marketerTracking(host);
            // Log.e("sendAccessibilityEvent", "" + eventType + "   " + host.getResources().getResourceName(host.getId()));
        } else if (host instanceof Button) {
            TrackerHelper.getInstance().marketerTracking(host);
            // Log.e("sendAccessibilityEvent", "" + eventType + "   " + host.getResources().getResourceName(host.getId()));
        }
        Log.e("sendAccessibilityEvent", "" + eventType );

        if (AccessibilityEvent.TYPE_VIEW_CLICKED == eventType || AccessibilityEvent.TYPE_VIEW_LONG_CLICKED == eventType) {
            TrackerHelper.getInstance().marketerTracking(host);
           /* TrackerHelper.getInstance().autoEventTracking(host);
            if (isDebugMode)
                AppWidgets.showEventDialog(host);*/
        }
        super.sendAccessibilityEvent(host, eventType);
    }


}
