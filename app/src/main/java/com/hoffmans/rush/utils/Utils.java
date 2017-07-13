package com.hoffmans.rush.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.hoffmans.rush.R;

import java.util.List;
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
            }
            else {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                Log.e("pic url",contentUri.getPath());
                String path =cursor.getString(column_index);
                if(path!=null && !TextUtils.isEmpty(path)) {
                    return cursor.getString(column_index);
                }
            }

            return null;
         } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
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
     * Get credit card type using this method
     * @param creditCardNumber provide credit card number
     * @return
     */
    public static String getCreditCardTypeByNumber(String creditCardNumber) {

        String regVisa = "^4[0-9]{12}(?:[0-9]{3})?$";
        String regMaster = "^5[1-5][0-9]{14}$";
        String regExpress = "^3[47][0-9]{13}$";
        String regDiners = "^3(?:0[0-5]|[68][0-9])[0-9]{11}$";
        String regDiscover = "^6(?:011|5[0-9]{2})[0-9]{12}$";
        String regJCB= "^(?:2131|1800|35\\d{3})\\d{11}$";

        if(creditCardNumber.matches(regVisa))
            return "Visa";
        if (creditCardNumber.matches(regMaster))
            return "mastercard";
        if (creditCardNumber.matches(regExpress))
            return "amex";
        if (creditCardNumber.matches(regDiners))
            return "DINERS";
        if (creditCardNumber.matches(regDiscover))
            return "discover";
        if (creditCardNumber.matches(regJCB))
            return "Jcb";
        return "invalid";
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


    public static  boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }
}
