package com.hoffmans.rush.listners;

import android.view.View;

/**
 * Created by devesh on 10/1/17.
 */

public interface OnitemClickListner {

    interface OnFrequentAddressClicked {

         void onitemclicked(View view, int position);
         void onfrequentAddressclicked(View view, int position);


    }

}
