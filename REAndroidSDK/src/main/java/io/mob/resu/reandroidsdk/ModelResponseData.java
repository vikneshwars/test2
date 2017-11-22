package io.mob.resu.reandroidsdk;

/**
 * Created by Interakt on 11/15/17.
 */

public class ModelResponseData {

    private String response;
    private int responseCode;

    public ModelResponseData(String response, int responseCode) {
        this.response = response;
        this.responseCode = responseCode;
    }

    public ModelResponseData() {
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
}
