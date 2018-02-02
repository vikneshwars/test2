package io.mob.resu.reandroidsdk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import io.mob.resu.reandroidsdk.error.ExceptionTracker;


  class PictureStyleNotification {

    private Intent intent;
    private Context mContext;
    private String title,
            message,
            imageUrl;

    public PictureStyleNotification(Context context, String title, String message, String imageUrl,  Intent intent) {
        super();
        this.mContext = context;
        this.title = title;
        this.message = message;
        this.imageUrl = imageUrl;
        this.intent = intent;
        new MyAsync().execute();
    }


    private class MyAsync extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... params) {

            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                ExceptionTracker.track(e);
                return null;
            }

        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            try {
                if (result != null)
                    new AppNotification().showNotification(mContext, title, message,  intent, result);
                else
                    new AppNotification().showNotification(mContext, title, message,  intent, null);

            } catch (Exception e) {
                ExceptionTracker.track(e);
            }
        }
    }
}
