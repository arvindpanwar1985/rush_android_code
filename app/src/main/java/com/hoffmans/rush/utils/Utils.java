package com.hoffmans.rush.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.hoffmans.rush.R;

import java.util.TimeZone;

/**
 * Created by devesh on 21/12/16.
 */

public class Utils {


    /**
     *
     * @param context
     * @param contentUri Uri of the file
     * @return the complete path of file from cursor
     */

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            if(cursor==null){
                Log.e("pic url",contentUri.getPath());
                return contentUri.getPath();
            }else {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                Log.e("pic url",contentUri.getPath());
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }



    public static void showAlertDialog(Context context,  String message) {
        try {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
            builder.setTitle(R.string.app_name)
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    }).create().show();
        }catch (Exception e){

        }
    }

    /**
     *
     * @return the time zone id
     */
    public static String getTimeZone(){
        TimeZone tz = TimeZone.getDefault();
        return tz.getID();
    }


    /**
     * Hide keyboard method
     *
     * @param activity
     */
    public static void hideKeyboard(Activity activity) {
        //start with an 'always hidden' command for the activity's window
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //now tell the IMM to hide the keyboard FROM whatever has focus in the activity
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), 0);
        }
    }
}
