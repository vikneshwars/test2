package io.mob.resu.reandroidsdk;

/**
 * Created by Interakt on 11/23/17.
 */

public class MRecord {
    private String result;
    private String captureType;
    private String identifier;
    private String type;
    private String description;

    public MRecord(String result, String captureType, String identifier, String description) {
        this.result = result;
        this.captureType = captureType;
        this.identifier = identifier;
        this.type = type;
        this.description = description;
    }

    public String getDescription() {
        return description;

    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getType() {
        return type;
    }

    public MRecord(String result, String captureType, String identifier) {
        this.result = result;
        this.captureType = captureType;
        this.identifier = identifier;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return "ClassPojo [result = " + result + ", captureType = " + captureType + ", identifier = " + identifier + "]";
    }
}

