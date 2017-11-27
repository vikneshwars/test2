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

/**
 * Created by P Buvaneswaran on 01-08-2017.
 */

  class PictureStyleNotification {

    Intent intent;
    Bitmap theBitmap;
    private Context mContext;
    private String title,
            message,
            actionName,
            imageUrl;

    public PictureStyleNotification(Context context, String title, String message, String imageUrl, String actionName, Intent intent) {
        super();
        this.mContext = context;
        this.title = title;
        this.message = message;
        this.imageUrl = imageUrl;
        this.actionName = actionName;
        this.intent = intent;
        new MyAsync().execute();
    }


    public class MyAsync extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... params) {

            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap theBitmap = BitmapFactory.decodeStream(input);
                return theBitmap;
            } catch (IOException e) {
                Util.catchMessage(e);
                return null;
            }

        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            try {
                if (result != null)
                    new AppNotification().showNotification(mContext, title, message, actionName, intent, result);
                else
                    new AppNotification().showNotification(mContext, title, message, actionName, intent, null);

            } catch (Exception e) {
                Util.catchMessage(e);
            }
        }
    }
}
