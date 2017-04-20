package com.hoffmans.rush.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hoffmans.rush.ui.activities.BaseActivity;
import com.hoffmans.rush.utils.AppPreference;

/**
 * Created by devesh on 19/12/16.
 */

public abstract class BaseFragment extends Fragment {

    protected BaseActivity mActivity;
    //protected Utils mUtils;
   // protected AppPreference mPreference;
    protected DateUtils mDateUtils;
    protected Gson mGson;
    protected AppPreference appPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            mActivity = (BaseActivity) context;
            mGson=new GsonBuilder().create();
            appPreference=AppPreference.newInstance(mActivity);
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

    //addmarker
    protected Marker addlocationMarker(LatLng latLng, int marker, GoogleMap mMap,boolean draggable){
        if(marker==0){
            //only animate to position
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
            return   null;
        }else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

            return  mMap.addMarker(new MarkerOptions().position(latLng).draggable(draggable).icon(BitmapDescriptorFactory.fromResource(marker)));

        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
