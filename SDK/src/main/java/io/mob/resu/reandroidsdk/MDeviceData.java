package io.mob.resu.reandroidsdk;

import android.content.Context;
import android.os.Build;

import java.util.ArrayList;

/**
 * Created by Interakt on 8/22/17.
 */
class MDeviceData {

    Context context;
    private String appId = "";
    private String name = "";
    private String phone = "";
    private String email = "";
    private String deviceToken = "";
    private String deviceId = "";
    private String deviceOs = "";
    private String deviceIdfa = "";
    private String packageName = "";
    private String deviceOsVersion = "";
    private String deviceManufacture = "";
    private String deviceModel = "";
    private String appVersionName = "";
    private String appVersionCode = "";
    private String appInstallDate = "";
    private String appUpdate = "";
    private ArrayList<String> activityList = new ArrayList<>();

    public MDeviceData() {
    }

    public MDeviceData(Context context) {
        this.context = context;
        appId = SharedPref.getInstance().getStringValue(context, context.getString(R.string.resulticksSharedAPIKey));
        //deviceToken = FirebaseInstanceId.getInstance().getToken();
        deviceId = Util.getDeviceId(context);
        deviceIdfa = SharedPref.getInstance().getStringValue(context, context.getString(R.string.resulticksSharedAdverId));
        deviceOsVersion = "" + Build.VERSION.RELEASE;
        deviceManufacture = android.os.Build.MANUFACTURER;
        deviceModel = android.os.Build.MODEL;
        deviceOs = "Android";
        appVersionName = Util.getAppVersionName(context);
        appVersionCode = Util.getAppVersionCode(context);
        packageName = context.getApplicationContext().getPackageName();
        activityList = Util.getAllActivity(context);
    }


    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getDeviceManufacture() {
        return deviceManufacture;
    }

    public void setDeviceManufacture(String deviceManufacture) {
        this.deviceManufacture = deviceManufacture;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getAppVersionName() {
        return appVersionName;
    }

    public void setAppVersionName(String appVersionName) {
        this.appVersionName = appVersionName;
    }

    public String getAppVersionCode() {
        return appVersionCode;
    }

    public void setAppVersionCode(String appVersionCode) {
        this.appVersionCode = appVersionCode;
    }

    public String getAppInstallDate() {
        return appInstallDate;
    }

    public void setAppInstallDate(String appInstallDate) {
        this.appInstallDate = appInstallDate;
    }

    public String getAppUpdate() {
        return appUpdate;
    }

    public void setAppUpdate(String appUpdate) {
        this.appUpdate = appUpdate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceOs() {
        return deviceOs;
    }

    public void setDeviceOs(String deviceOs) {
        this.deviceOs = deviceOs;
    }

    public String getDeviceIdfa() {
        return deviceIdfa;
    }

    public void setDeviceIdfa(String deviceIdfa) {
        this.deviceIdfa = deviceIdfa;
    }

    public String getDeviceOsVersion() {
        return deviceOsVersion;
    }

    public void setDeviceOsVersion(String deviceOsVersion) {
        this.deviceOsVersion = deviceOsVersion;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public ArrayList<String> getActivityList() {
        return activityList;
    }

    public void setActivityList(ArrayList<String> activityList) {
        this.activityList = activityList;
    }


}
