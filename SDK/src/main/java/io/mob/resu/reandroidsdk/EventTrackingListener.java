package io.mob.resu.reandroidsdk;

import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;

import io.mob.resu.reandroidsdk.error.Log;



class EventTrackingListener extends View.AccessibilityDelegate {

    static boolean isDebugMode = false;
    static ArrayList<HashMap> Events = new ArrayList<>();


    @Override
    public void sendAccessibilityEvent(View host, int eventType) {

        if (host instanceof EditText) {
            AppLifecyclePresenter.getInstance().fieldWiseDataListener(host);
        } else if (host instanceof Button) {
            AppLifecyclePresenter.getInstance().fieldWiseDataListener(host);
        }
        Log.e("sendAccessibilityEvent", "" + eventType );

        if (AccessibilityEvent.TYPE_VIEW_CLICKED == eventType || AccessibilityEvent.TYPE_VIEW_LONG_CLICKED == eventType) {
            AppLifecyclePresenter.getInstance().fieldWiseDataListener(host);
           /* AppLifecyclePresenter.getInstance().autoEventTracking(host);
            if (isDebugMode)
                AppWidgets.showEventDialog(host);*/
        }
        super.sendAccessibilityEvent(host, eventType);
    }


}
