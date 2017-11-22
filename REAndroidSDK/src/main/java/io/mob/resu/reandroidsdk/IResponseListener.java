package io.mob.resu.reandroidsdk;

/**
 * Created by Buvaneswaran on 06-12-2016.
 */
 interface IResponseListener {

    void onSuccess(String response, int flag);

    void onFailure(Throwable throwable, int flag);

    void showDialog(String response, int flag);

    void showErrorDialog(String errorResponse, int flag);

    void showInternalServerErrorDialog(String errorResponse, int flag);

    void logOut(int flag);

}
