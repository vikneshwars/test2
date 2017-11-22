package io.mob.resu.reandroidsdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


 class SharedPref {

    private static SharedPreferences preference = null;
    private static String preferenceName = "Resulticks";
    private static SharedPref sharedPref = null;

    public static SharedPref getInstance() {
        if (sharedPref != null) {
            return sharedPref;
        } else {
            sharedPref = new SharedPref();
            return sharedPref;
        }
    }


    /**
     * Singleton object for the shared preference
     *
     * @param context
     * @return SharedPreferences
     */

    public static SharedPreferences getPreferenceInstance(Context context) {

        if (preference != null) {
            return preference;
        } else {
            preference = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
            return preference;
        }
    }

    /**
     * Set the shared preference W.R.T the key
     *
     * @param context
     * @param key
     * @param value
     */

    public void setSharedValue(Context context, String key, String value) {
        getPreferenceInstance(context);
        Editor editor = preference.edit();
        editor.putString(key, value);
        editor.commit();
    }


    /**
     * Set the shared preference W.R.T the key
     *
     * @param context
     * @param key
     * @param value
     */

    public void setSharedValue(Context context, String key, int value) {
        getPreferenceInstance(context);
        Editor editor = preference.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * Set the shared preference W.R.T the key
     *
     * @param context
     * @param key
     * @param value
     */

    public void setSharedValue(Context context, String key, Boolean value) {
        getPreferenceInstance(context);
        Editor editor = preference.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * Returns the shared preference key for the given key
     *
     * @param context
     * @param key
     * @return Boolean
     */

    public Boolean getBooleanValue(Context context, String key) {
        return getPreferenceInstance(context).getBoolean(key, false);
    }

    /**
     * Returns the shared preference key for the given key
     *
     * @param context
     * @param key
     * @return Int
     */

    public int getIntVlue(Context context, String key) {
        return getPreferenceInstance(context).getInt(key, 0);
    }


    /**
     * Returns the shared preference key for the given key
     *
     * @param context
     * @param key
     * @return String
     */

    public String getStringValue(Context context, String key) {

        return getPreferenceInstance(context).getString(key, "");
    }


}
