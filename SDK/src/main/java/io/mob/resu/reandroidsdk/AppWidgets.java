package io.mob.resu.reandroidsdk;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.Window;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import org.json.JSONObject;

import io.mob.resu.reandroidsdk.error.ExceptionTracker;
import io.mob.resu.reandroidsdk.error.Log;


class AppWidgets {

    // static String screenName;
    //static String viewId;
    //static ArrayList<JSONObject> registerEvent = new ArrayList<>();
    private static boolean flag = false;
    private static Dialog dialog;
    private RatingBar ratingBar;
    private WebView banner;


    private IResponseListener IResponseListener = new IResponseListener() {
        @Override
        public void onSuccess(String response, int flag) {
            Log.e("response", response);
        }

        @Override
        public void onFailure(Throwable throwable, int flag) {
            Log.e("response", throwable.getMessage());
        }

        @Override
        public void showDialog(String response, int flag) {
            Log.e("response", response);

        }

        @Override
        public void showErrorDialog(String errorResponse, int flag) {
            Log.e("response", errorResponse);
        }

        @Override
        public void showInternalServerErrorDialog(String errorResponse, int flag) {
            Log.e("response", errorResponse);
        }

        @Override
        public void logOut(int flag) {
            Log.e("response", "" + flag);
        }
    };

    /*
     * Snake event filed tracking
     * @param views
     */
    /*static void showEventDialog(final View views) {

        try {
            screenName = views.getContext().getClass().getSimpleName();
            viewId = views.getResources().getResourceName(views.getId());
            showDialog();
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }

    }*/
    /*
     * Snake event filed tracking
     *
     */
    /*static void showDialog(View view) {
        final Activity context;
        try {
           // context = ActivityLifecycleCallbacks.mActivity;
            context.runOnUiThread(new Runnable() {

                public void run() {
                    dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.dialog_event);
                    final EditText editText = dialog.findViewById(R.id.identifier);
                    final EditText eventName = dialog.findViewById(R.id.event_name);
                    final TextView close = dialog.findViewById(R.id.btn_close);
                    final Button mSave = dialog.findViewById(R.id.btn_save);
                    final Button mCancel = dialog.findViewById(R.id.btn_cancel);
                    String[] id = viewId.split("/");
                    editText.setText(id[1]);
                    final MRegisterEvent mRegisterEvent = new DataBase(context).getData(DataBase.Table.REGISTER_EVENT_TABLE, viewId, screenName);
                    if (mRegisterEvent != null && mRegisterEvent.getEventName() != null) {
                        eventName.setText(mRegisterEvent.getEventName());
                        eventName.setEnabled(false);
                        eventName.setFocusable(false);
                        eventName.setFocusableInTouchMode(false);
                        mSave.setText("Edit");
                        mCancel.setText("Remove");
                    } else {
                        eventName.setEnabled(true);
                        eventName.setFocusable(true);
                        eventName.setFocusableInTouchMode(true);
                        mSave.setText("Save");
                        mCancel.setText("Cancel");
                    }
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    mSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (mSave.getText().toString().equalsIgnoreCase("Save")) {
                                try {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("eventName", eventName.getText().toString());
                                    jsonObject.put("viewId", viewId);
                                    jsonObject.put("screenName", screenName);
                                    jsonObject.put("timeStamp", Util.getCurrentUTC());
                                    new DataBase(context).insertOrUpdateData(jsonObject.toString(), viewId, screenName, DataBase.Table.REGISTER_EVENT_TABLE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                dialog.dismiss();
                            } else {
                                eventName.setEnabled(true);
                                eventName.setFocusable(true);
                                eventName.setFocusableInTouchMode(true);
                                mSave.setText("Save");
                            }

                        }
                    });
                    mCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (mCancel.getText().toString().equalsIgnoreCase("Remove")) {
                                new DataBase(context).deleteEventData(mRegisterEvent.getEventID(), DataBase.Table.REGISTER_EVENT_TABLE);
                            }
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            });
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }


    }*/

    /**
     * screen navigation time keep showing dialog handler
     *
     * @param isShowing
     */
    static void DialogHandler(boolean isShowing) {
        try {
            if (isShowing) {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        flag = true;
                        dialog.dismiss();
                    }
                }
            } else {
                if (flag) {
                    flag = false;
                    // showDialog();
                }
            }
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }

    }

    /**
     * Push notification Rating Campaign
     *
     * @param context
     * @param dialogTitle
     * @param dialogMessage
     * @param intent
     */
    void showRatingDialog(final Activity context, final String dialogTitle, final String dialogMessage, final Intent intent) {


        try {
            context.runOnUiThread(new Runnable() {
                public void run() {
                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.dialog_rating);
                    //TextView title = dialog.findViewById(R.id.tv_title);
                    // TextView description = dialog.findViewById(R.id.tv_description);
                    ratingBar = dialog.findViewById(R.id.rating_bar);
                    Button dialogButton = dialog.findViewById(R.id.btn_submit);

                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            if (intent.getExtras().containsKey("rating")) {
                                if (ratingBar.getRating() > Integer.parseInt(intent.getExtras().getString("rating"))) {
                                    DataNetworkHandler.getInstance().campaignHandler(context, intent.getExtras().getString(context.getString(R.string.resulticksApiParamId)), "4", false, "" + ratingBar.getRating(), null);
                                    shareIntent(dialogTitle, dialogMessage, context);
                                } else {
                                    showFeedBackDialog(context, dialogTitle, dialogMessage, intent);
                                }
                            }
                        }
                    });
                    dialog.show();
                }
            });
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }

    }

    /**
     * Rating wise share option
     *
     * @param dialogTitle
     * @param dialogMessage
     * @param context
     */
    private void shareIntent(String dialogTitle, String dialogMessage, Activity context) {

        try {
            Intent sharingIntent;
            sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, dialogTitle);
            sharingIntent.putExtra(Intent.EXTRA_TEXT, dialogMessage);
            context.startActivity(Intent.createChooser(sharingIntent, "Share With"));
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
    }

    /**
     * low rating Comments
     *
     * @param context
     * @param dialogTitle
     * @param dialogMessage
     * @param intent
     */
    private void showFeedBackDialog(final Activity context, String dialogTitle, String dialogMessage, final Intent intent) {


        try {
            context.runOnUiThread(new Runnable() {
                public void run() {

                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.dialog_feedback);

                    final EditText comments = dialog.findViewById(R.id.ed_comments);
                    Button cancel = dialog.findViewById(R.id.btn_cancel);
                    Button submit = dialog.findViewById(R.id.btn_submit);
                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(context, "Thanks for your feedback", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            DataNetworkHandler.getInstance().campaignHandler(context, intent.getExtras().getString(context.getString(R.string.resulticksApiParamId)), "4", false, "" + ratingBar.getRating(), comments.getText().toString().trim());

                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            });
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }

    }

    /**
     * Promotion Banner Campaign
     *
     * @param context
     * @param dialogTitle
     * @param dialogMessage
     * @param intent
     * @param Url
     */
    void showBannerDialog(final Activity context, String dialogTitle, String dialogMessage, final Intent intent, final String Url) {


        try {
            context.runOnUiThread(new Runnable() {
                public void run() {
                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.dialog_banner);
                    // RelativeLayout root = dialog.findViewById(R.id.action_bar_root);
                    banner = dialog.findViewById(R.id.img_banner);
                    banner.setWebViewClient(new MyWebViewClient());
                    banner.loadUrl(Url);
                    ImageView close = dialog.findViewById(R.id.iv_close);

                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                dialog.dismiss();
                                //                            Intent intents = new Intent(context, Class.forName(intent.getExtras().getString("navigationScreen")));
                                //                            context.startActivity(intents);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    dialog.show();
                }
            });
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }

    }

    /**
     * Serve Campaign
     *
     * @param context
     * @param dialogTitle
     * @param dialogMessage
     * @param intent
     * @param Url
     */
    void showServeDialog(final Activity context, String dialogTitle, String dialogMessage, final Intent intent, final String Url) {


        try {
            context.runOnUiThread(new Runnable() {
                public void run() {
                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.dialog_banner);
                    // RelativeLayout root = dialog.findViewById(R.id.action_bar_root);
                    banner = dialog.findViewById(R.id.img_banner);
                    banner.setWebViewClient(new MyWebViewClient());
                    banner.loadUrl(Url);
                    ImageView close = dialog.findViewById(R.id.iv_close);


                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                dialog.dismiss();
                                //                            Intent intents = new Intent(context, Class.forName(intent.getExtras().getString("navigationScreen")));
                                //                            context.startActivity(intents);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    dialog.show();
                }
            });
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }

    }

    /**
     * Server Update
     *
     * @param id
     * @param comments
     */
    private void userRating(String id, String comments) {

        try {
            JSONObject userDetail;
            userDetail = new JSONObject();
            userDetail.put("id", id);
            userDetail.put("rating", ratingBar.getRating());
            userDetail.put("status", "2");
            userDetail.put("comments", comments);
            new DataExchanger("campaignTracking", userDetail.toString(), IResponseListener, AppConstants.SDK_USER_REGISTER).execute();

        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
       /* ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.callCampaignRating(id, "" + ratingBar.getRating(), comments);
        RetroFitUtils.getInstance().retrofitEnqueue(call, IResponseListener, 1000);*/
    }

    /**
     * Serve form webView MyWebViewClient
     */
    private class MyWebViewClient extends WebViewClient {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest url) {
            view.loadUrl(url.getUrl().getPath());

            return true;
        }


        @Override
        public void onPageFinished(WebView webView, String url) {

            webView.loadUrl("javascript:(function() { " +
                    "document.getElementsByClassName('main-wrapper')[0].style.display='none'; " + "})()");

        }
    }
}
