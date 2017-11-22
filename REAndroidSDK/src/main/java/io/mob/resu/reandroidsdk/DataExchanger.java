package io.mob.resu.reandroidsdk;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Interakt on 11/15/17.
 */

public class DataExchanger extends AsyncTask<String, String, String> {

    private HttpsURLConnection connection = null;
    private BufferedReader reader = null;
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

        modelResponseData = sendPost(url, parameters);

        return "";
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (modelResponseData != null) {
            switch (modelResponseData.getResponseCode()) {
                case 200:
                    listener.onSuccess(modelResponseData.getResponse(), requestCode);
                    break;
                case 201:
                    listener.showDialog(modelResponseData.getResponse(), requestCode);
                    break;
                case 400:
                    //if (response.errorBody() != null) {
                    listener.showErrorDialog("", requestCode);
                    //}
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

    }

    public ModelResponseData sendPost(String _url, String values) {


        String USER_AGENT = "android";

        String result = "";
        String baseUrl = "http://resulticks.biz:81/Home/";
        ModelResponseData modelResponseData = null;
        try {

            String url = baseUrl+_url;
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


            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine + "\n");
            }
            in.close();

            result = response.toString();

            modelResponseData = new ModelResponseData(result, responseCode);

            System.out.println("Response : " + result);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            return modelResponseData;
        }

    }
}

