package io.mob.resu.reandroidsdk;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import java.util.Calendar;

/**
 * Created by P Buvaneswaran on 30-07-2017.
 */

class FragmentLifecycleCallbacks extends FragmentManager.FragmentLifecycleCallbacks {

    Calendar sCalendar = Calendar.getInstance();
    Calendar oldCalendar = Calendar.getInstance();
    String newScreenName = "";

    FragmentLifecycleCallbacks() {
        super();
    }

    @Override
    public void onFragmentPreAttached(FragmentManager fm, Fragment f, Context context) {
        super.onFragmentPreAttached(fm, f, context);
    }

    @Override
    public void onFragmentAttached(FragmentManager fm, Fragment f, Context context) {
        super.onFragmentAttached(fm, f, context);
    }

    @Override
    public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
        super.onFragmentCreated(fm, f, savedInstanceState);
    }

    @Override
    public void onFragmentActivityCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
        super.onFragmentActivityCreated(fm, f, savedInstanceState);
    }

    @Override
    public void onFragmentViewCreated(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
        super.onFragmentViewCreated(fm, f, v, savedInstanceState);
        AppWidgets.DialogHandler(true);
    }

    @Override
    public void onFragmentStarted(FragmentManager fm, Fragment f) {
        super.onFragmentStarted(fm, f);
        try {
            newScreenName = f.getClass().getSimpleName();
            oldCalendar = sCalendar;
            sCalendar = Calendar.getInstance();
            AppWidgets.DialogHandler(false);
            TrackerHelper.getInstance().screenTrackingUpdateToServer(f.getActivity());
        } catch (Exception e) {
            Util.catchMessage(e);
        }
    }

    @Override
    public void onFragmentResumed(FragmentManager fm, Fragment f) {
        super.onFragmentResumed(fm, f);
    }

    @Override
    public void onFragmentPaused(FragmentManager fm, Fragment f) {
        super.onFragmentPaused(fm, f);
    }

    @Override
    public void onFragmentStopped(FragmentManager fm, Fragment f) {
        super.onFragmentStopped(fm, f);
        TrackerHelper.getInstance().screenTracking(f.getActivity(), oldCalendar, Calendar.getInstance(), f.getActivity().getClass().getSimpleName(), f.getClass().getSimpleName(), null);
    }

    @Override
    public void onFragmentSaveInstanceState(FragmentManager fm, Fragment f, Bundle outState) {
        super.onFragmentSaveInstanceState(fm, f, outState);
    }

    @Override
    public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
        super.onFragmentViewDestroyed(fm, f);
    }

    @Override
    public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
        super.onFragmentDestroyed(fm, f);
    }

    @Override
    public void onFragmentDetached(FragmentManager fm, Fragment f) {
        super.onFragmentDetached(fm, f);
    }


}
