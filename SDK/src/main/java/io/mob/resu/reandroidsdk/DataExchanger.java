package io.mob.resu.reandroidsdk;

import android.os.AsyncTask;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import io.mob.resu.reandroidsdk.error.ExceptionTracker;

import static io.mob.resu.reandroidsdk.AppConstants.baseUrl;


class DataExchanger extends AsyncTask<String, String, String> {

    private String url;
    private String parameters;
    private ModelResponseData modelResponseData;
    private IResponseListener listener;
    private int requestCode;


    public DataExchanger(String url, String parameters, IResponseListener listener, int requestCode) {
        this.url = url;
        this.parameters = parameters;
        this.listener = listener;
        this.requestCode = requestCode;

    }

    @Override
    protected String doInBackground(String... params) {
        try {
            modelResponseData = sendPost(url, parameters);

        } catch (Exception e) {
            ExceptionTracker.track(e);
        }

        return "";
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        try {
            if (modelResponseData != null) {
                switch (modelResponseData.getResponseCode()) {
                    case 200:
                        listener.showDialog(modelResponseData.getResponse(), requestCode);
                        break;
                    case 201:
                        listener.onSuccess(modelResponseData.getResponse(), requestCode);
                        break;
                    case 400:
                        if (modelResponseData.getResponse() != null) {
                            listener.showErrorDialog(modelResponseData.getResponse(), requestCode);
                        }
                        break;
                    case 401:
                        listener.logOut(requestCode);
                        break;
                    case 500:
                        listener.showInternalServerErrorDialog(modelResponseData.getResponse(), requestCode);
                        break;
                    default:
                        listener.logOut(requestCode);
                        break;
                }
            } else {
                listener.onFailure(new Throwable(), requestCode);
            }
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }

    }

    private ModelResponseData sendPost(String _url, String values) throws Exception {


        String USER_AGENT = "android";

        String result;


        ModelResponseData modelResponseData;
        String url;
        if (_url.contains("http"))
            url = _url;
        else {
            if (!TextUtils.isEmpty(baseUrl))
                url = baseUrl + _url;
            else
                url = "http://resulticks.biz:81/Home/" + _url;
        }


        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "UTF-8");

        con.setDoOutput(true);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(con.getOutputStream());
        outputStreamWriter.write(values);
        outputStreamWriter.flush();

        int responseCode = con.getResponseCode();

        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + values);
        System.out.println("Response Code : " + responseCode);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in;

        if (200 <= con.getResponseCode() && con.getResponseCode() <= 299) {
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine + "\n");
        }
        in.close();

        result = response.toString();

        modelResponseData = new ModelResponseData(result, responseCode);

        System.out.println("Response : " + result);

        return modelResponseData;


    }
}

