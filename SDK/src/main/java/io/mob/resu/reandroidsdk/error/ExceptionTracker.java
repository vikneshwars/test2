package io.mob.resu.reandroidsdk.error;

/**
 * Created by Buvaneswaran on 2/23/2017.
 */

public class ExceptionTracker {

    public static void track(Exception exception) {
        //Crashlytics.logException(exception);
        exception.printStackTrace();
    }

    public static void track(String message) {
        //Crashlytics.log(message);
        //Log.e();
    }
}