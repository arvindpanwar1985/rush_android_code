package com.hoffmans.rush.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by devesh on 23/12/16.
 */

public class Progress {

    private static ProgressDialog mprogressDialog;


    public static void showprogress(Context context, String message, boolean isCancellable) {
        try {
            mprogressDialog = new ProgressDialog(context);
            mprogressDialog.setTitle("");
            mprogressDialog.setIndeterminate(false);
            mprogressDialog.setMessage(message);
            mprogressDialog.setCancelable(isCancellable);
            mprogressDialog.show();
        }catch (Exception e){

        }
      }



    public static void dismissProgress() {
        try {
            if (mprogressDialog != null && mprogressDialog.isShowing()) {
                mprogressDialog.dismiss();
                mprogressDialog = null;
            }
        }catch (Exception e){

        }
    }

}
