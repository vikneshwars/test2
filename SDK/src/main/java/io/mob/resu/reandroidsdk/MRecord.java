package io.mob.resu.reandroidsdk;



public class MRecord {
    private String result;
    private String captureType;
    private String identifier;
    private String description;

     MRecord(String result, String captureType, String identifier, String description) {
        this.result = result;
        this.captureType = captureType;
        this.identifier = identifier;
        this.description = description;
    }

    public String getDescription() {
        return description;

    }

    public void setDescription(String description) {
        this.description = description;
    }



    public MRecord(String result, String captureType, String identifier) {
        this.result = result;
        this.captureType = captureType;
        this.identifier = identifier;
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
        return "MRecord [result = " + result + ", captureType = " + captureType + ", identifier = " + identifier + "]";
    }
}

