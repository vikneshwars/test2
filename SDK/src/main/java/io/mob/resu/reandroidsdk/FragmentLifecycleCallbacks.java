package io.mob.resu.reandroidsdk;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import java.util.Calendar;

import io.mob.resu.reandroidsdk.error.ExceptionTracker;


class FragmentLifecycleCallbacks extends FragmentManager.FragmentLifecycleCallbacks {

    private Calendar sCalendar = Calendar.getInstance();
    private Calendar oldCalendar = Calendar.getInstance();

    FragmentLifecycleCallbacks() {
        super();
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
           String newScreenName = f.getClass().getSimpleName();
            oldCalendar = sCalendar;
            sCalendar = Calendar.getInstance();
            AppWidgets.DialogHandler(false);
            AppLifecyclePresenter.getInstance().screenSessionUpdate(f.getActivity(), newScreenName);
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
    }


    @Override
    public void onFragmentStopped(FragmentManager fm, Fragment f) {
        super.onFragmentStopped(fm, f);
        AppLifecyclePresenter.getInstance().screenByUserActivity(f.getActivity(), oldCalendar, Calendar.getInstance(), f.getActivity().getClass().getSimpleName(), f.getClass().getSimpleName(), null);
    }


}
