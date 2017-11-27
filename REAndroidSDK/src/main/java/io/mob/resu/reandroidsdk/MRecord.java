package io.mob.resu.reandroidsdk;

/**
 * Created by Interakt on 11/23/17.
 */

public class MRecord {
    private String result;
    private String captureType;
    private String viewId;

    public MRecord(String result, String captureType, String viewId) {
        this.result = result;
        this.captureType = captureType;
        this.viewId = viewId;
    }

    public MRecord() {
    }

    public String getResult()

    {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCaptureType() {
        return captureType;
    }

    public void setCaptureType(String captureType) {
        this.captureType = captureType;
    }

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    @Override
    public String toString() {
        return "ClassPojo [result = " + result + ", captureType = " + captureType + ", viewId = " + viewId + "]";
    }
}

