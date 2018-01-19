package io.mob.resu.reandroidsdk.error;


import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class ExceptionTracker {

    public static ArrayList<JSONObject> errors = new ArrayList<>();

    public static void track(Exception exception) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("error", exception.getMessage());
            jsonObject.put("timeStamp", getCurrentUTC());
            errors.add(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void track(String message) {
    }

    private static String getCurrentUTC() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(Calendar.getInstance().getTime());
    }
}