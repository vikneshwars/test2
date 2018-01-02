package io.mob.resu.reandroidsdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.json.JSONObject;

import java.net.URLDecoder;

import io.mob.resu.reandroidsdk.error.Log;


public class InstallReferrerReceiver extends BroadcastReceiver {

    private JSONObject referrerObject = new JSONObject();


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("InstallReferrerReceiver", "" + intent.getStringExtra("referrer"));
        String rawReferrerString = intent.getStringExtra("referrer");
        if (rawReferrerString != null) {
            try {
                referrerObject.put(context.getString(R.string.resulticksApiParamIsNewUser), true);
                referrerObject.put(context.getString(R.string.resulticksDeepLinkParamIsViaDeepLinkingLauncher), true);
                rawReferrerString = URLDecoder.decode(rawReferrerString, "UTF-8");
                //HashMap<String, String> referrerMap = new HashMap<>();
                String[] referralParams = rawReferrerString.split("&");
                for (String referrerParam : referralParams) {
                    String[] keyValue = referrerParam.split("=");
                    if (keyValue.length > 1) {
                        // To make sure that there is one key value pair in referrer

                        Log.e("key", URLDecoder.decode(keyValue[0], "UTF-8"));
                        Log.e("value", URLDecoder.decode(keyValue[1], "UTF-8"));
                        referrerObject.put(URLDecoder.decode(keyValue[0], "UTF-8"), URLDecoder.decode(keyValue[1], "UTF-8"));
                    }
                }

                SharedPref.getInstance().setSharedValue(context, context.getString(R.string.resulticksSharedCampaignId), referrerObject.getString(context.getString(R.string.resulticksDeepLinkParamReferralId)));
                SharedPref.getInstance().setSharedValue(context, context.getString(R.string.resulticksApiParamIsNewUser), true);

                if (referrerObject.has(context.getString(R.string.resulticksDeepLinkParamReferralId)))

                    DataNetworkHandler.getInstance().campaignHandler(context, referrerObject.getString(context.getString(R.string.resulticksDeepLinkParamReferralId)), "2", true, null, null);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}