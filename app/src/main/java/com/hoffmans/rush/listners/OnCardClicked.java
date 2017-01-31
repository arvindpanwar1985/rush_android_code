package com.hoffmans.rush.listners;

import android.view.View;

/**
 * Created by devesh on 31/1/17.
 */

public interface OnCardClicked {

        void onitemclicked(View view, int position);
        void onCardDelet(View view, int position);
        void oncardSelected(View view, int position);


}
