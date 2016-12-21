package com.hoffmans.rush.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

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
                return contentUri.getPath();
            }else {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
