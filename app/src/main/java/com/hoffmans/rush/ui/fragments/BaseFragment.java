package com.hoffmans.rush.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.View;

import com.hoffmans.rush.ui.activities.BaseActivity;

/**
 * Created by devesh on 19/12/16.
 */

public abstract class BaseFragment extends Fragment {

    protected BaseActivity mActivity;
    //protected Utils mUtils;
   // protected AppPreference mPreference;
    protected DateUtils mDateUtils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            mActivity = (BaseActivity) context;
        }
    }

    protected void replaceFragment(Fragment fragment) {
        if(mActivity.getToast()!=null){
            mActivity.getToast().cancel();
        }
        mActivity.replaceFragment(fragment, 10);
    }


    protected void replaceFragment(Fragment fragment, Boolean storeInStack) {
        if(mActivity.getToast()!=null){
            mActivity.getToast().cancel();
        }
        mActivity.replaceFragment(fragment, 10, storeInStack);
    }

    protected void replaceFragment(Fragment fragment, boolean storeInStack, boolean isAdd) {
        if(mActivity.getToast()!=null){
            mActivity.getToast().cancel();
        }
        mActivity.replaceFragment(fragment, 10, storeInStack, isAdd);
    }
    /**
     * initialize all views of activity under this method
     */
    protected abstract void initViews(View view);

    /**
     * initialize all Listeners under this method
     */
    protected abstract void initListeners();




    @Override
    public void onStop() {
        super.onStop();

    }
}
