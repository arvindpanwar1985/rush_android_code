package com.hoffmans.rush.ui.driver.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hoffmans.rush.R;
import com.hoffmans.rush.model.CustomerDetail;
import com.hoffmans.rush.model.DateTime;
import com.hoffmans.rush.model.Estimate;
import com.hoffmans.rush.model.PickDropAddress;
import com.hoffmans.rush.model.VechileDetail;
import com.hoffmans.rush.ui.activities.BookServiceActivity;
import com.hoffmans.rush.ui.fragments.BaseFragment;
import com.hoffmans.rush.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arvind on 22/5/17.
 */

public class DriverDetailsFragment extends BaseFragment implements View.OnClickListener {

    private ImageView mImageViewMap;
    private ImageView mImageViewDriver;
    private ImageView mImageViewVehicleIcon;
    private TextView mTxtViewDriverName, mTxtViewPhoneNumber, mTxtViewCost, mTxtViewDateTime;
    private TextView mTxtViewVehicleColor, mTxtViewPlateNumber;
    private TextView mTxtPickupLocation, mTxtViewDropAddres1, mTxtViewDropAddres2, mTxtViewDropAddres3;
    private TextView mTxtViewComment;
    private RelativeLayout mLayoutVehiclesDetails;

    private static final String STATIC_MAP_URL = "http://maps.google.com/maps/api/staticmap?center=";
    private int height, widht;

    private Estimate estimateData;
    private VechileDetail vehicleData;
    private CustomerDetail driverData;
    private PickDropAddress pickAddress;
    private DateTime dateTime;
    List<PickDropAddress> drop_down;
    private String comment,staus;

    public DriverDetailsFragment() {

    }

    public static DriverDetailsFragment newInstance(Estimate param1, CustomerDetail param3, PickDropAddress param4, DateTime param5, List<PickDropAddress> drop_down, String records
    ,String status) {

        DriverDetailsFragment fragment = new DriverDetailsFragment();

        Bundle args = new Bundle();
        args.putParcelable(Constants.KEY_ESTIMATE_DATA, param1);
        args.putParcelable(Constants.KEY_DRIVER_DETAILS, param3);
        args.putParcelable(Constants.KEY_PICK_ADDRESS, param4);
        args.putParcelable(Constants.KEY_DATA_DATE_TIME, param5);
        args.putParcelableArrayList(Constants.KEY_DROP_DOWN, (ArrayList<? extends Parcelable>) drop_down);
        args.putString(Constants.KEY_COMMENT, records);
        args.putString(Constants.SERVICE_STATUS,status);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            estimateData = getArguments().getParcelable(Constants.KEY_ESTIMATE_DATA);
            driverData = getArguments().getParcelable(Constants.KEY_DRIVER_DETAILS);
            pickAddress = getArguments().getParcelable(Constants.KEY_PICK_ADDRESS);
            dateTime = getArguments().getParcelable(Constants.KEY_DATA_DATE_TIME);
            drop_down = getArguments().getParcelableArrayList(Constants.KEY_DROP_DOWN);
            comment = getArguments().getString(Constants.KEY_COMMENT);
            staus=getArguments().getString(Constants.SERVICE_STATUS);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity.initToolBar(getString(R.string.str_order_details), true, false);
        View view = inflater.inflate(R.layout.fragment_user_details, container, false);
        initViews(view);
        initListeners();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    protected void initViews(View view) {


        mImageViewMap = (ImageView) view.findViewById(R.id.mapImage);
        mImageViewDriver = (ImageView) view.findViewById(R.id.fEPImgProfile);
        mTxtViewDriverName = (TextView) view.findViewById(R.id.txt_name);
        mTxtViewPhoneNumber = (TextView) view.findViewById(R.id.txt_phone_number);
        mTxtViewCost = (TextView) view.findViewById(R.id.txt_currenty_type);
        mTxtViewDateTime = (TextView) view.findViewById(R.id.txt_date_time);
        mTxtPickupLocation = (TextView) view.findViewById(R.id.txt_pickup_address);
        mTxtViewDropAddres1 = (TextView) view.findViewById(R.id.txt_drop_address1);
        mTxtViewDropAddres2 = (TextView) view.findViewById(R.id.txt_drop_address2);
        mTxtViewDropAddres3 = (TextView) view.findViewById(R.id.txt_drop_address3);
        mTxtViewComment = (TextView) view.findViewById(R.id.txt_show_comment);
        mLayoutVehiclesDetails=(RelativeLayout)view.findViewById(R.id.layoutVehicleDetails);
        mLayoutVehiclesDetails.setVisibility(View.GONE);

        setDetailsValues();
    }

    private void setDetailsValues() {


        if (getImageMap(pickAddress) != null) {
            Glide.with(mActivity).load(getImageMap(pickAddress)).into(mImageViewMap);
        }

           mTxtViewDriverName.setText(driverData.getName());
            mTxtViewPhoneNumber.setText(driverData.getPhone());
            if (driverData.getPicUrl() != null) {
                Glide.with(mActivity).load(driverData.getPicUrl()).into(mImageViewDriver);
            }



/*
        if(vehicleData.getName().equals("Motorcycle")){
            mImageViewVehicleIcon.setImageResource(R.drawable.bike_icon);

        }else if(vehicleData.getName().equals("Bike")){
            mImageViewVehicleIcon.setImageResource(R.drawable.moto_icon);

        }else if(vehicleData.getName().equals("Vehicule")){
            mImageViewVehicleIcon.setImageResource(R.drawable.car_icon);
        }else if(vehicleData.getName().equals("Truck upto 2tons")){
            mImageViewVehicleIcon.setImageResource(R.drawable.truck_icon);
        }*/


        if(staus!=null) {
            if (staus.equals("completed")) {
                mTxtViewCost.setVisibility(View.VISIBLE);
                mTxtViewCost.setText(estimateData.getSymbol() + estimateData.getApproxConvertedAmount().toString());
            } else {
                mTxtViewCost.setVisibility(View.GONE);
            }
        }


        mTxtViewDateTime.setText(dateTime.getDate() + " " + dateTime.getTime());

        mTxtPickupLocation.setText(pickAddress.getStreetAddress());
        int dropDownSize = drop_down.size();
        if (dropDownSize == 1) {
            mTxtViewDropAddres1.setVisibility(View.VISIBLE);
            mTxtViewDropAddres1.setText("1. " + drop_down.get(0).getStreetAddress());
        } else if (dropDownSize == 2) {
            mTxtViewDropAddres1.setVisibility(View.VISIBLE);
            mTxtViewDropAddres2.setVisibility(View.VISIBLE);
            mTxtViewDropAddres1.setText("1. " + drop_down.get(0).getStreetAddress());
            mTxtViewDropAddres2.setText("2. " + drop_down.get(1).getStreetAddress());
        } else if (dropDownSize == 3) {
            mTxtViewDropAddres1.setVisibility(View.VISIBLE);
            mTxtViewDropAddres2.setVisibility(View.VISIBLE);
            mTxtViewDropAddres3.setVisibility(View.VISIBLE);
            mTxtViewDropAddres1.setText("1. " + drop_down.get(0).getStreetAddress());
            mTxtViewDropAddres2.setText("2. " + drop_down.get(1).getStreetAddress());
            mTxtViewDropAddres3.setText("3. " + drop_down.get(2).getStreetAddress());
        }

        mTxtViewComment.setText(comment);




    }

    @Override
    protected void initListeners() {
        mTxtViewPhoneNumber.setOnClickListener(this);
    }

    private String getImageMap(PickDropAddress pickDropAddress) {

        DisplayMetrics displaymetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels / 1;
        widht = displaymetrics.widthPixels;

        if (pickDropAddress != null) {
            String marker_me = "color:red|label:1|"+pickDropAddress.getStreetAddress();
            return STATIC_MAP_URL + pickDropAddress.getLatitude() + "," + pickDropAddress.getLongitude() + "&zoom=18&size=" + height + "x" + widht + "&sensor=false"+"&markers=" + marker_me;
        }
        return null;
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == mTxtViewPhoneNumber.getId()) {
            Log.e("make call....",mTxtViewPhoneNumber.getText().toString());
            checkPermisstion();

        }
    }


    private void checkPermisstion(){
        String [] arrPermission=new String[]{Manifest.permission.CALL_PHONE,Manifest.permission.CALL_PHONE};
        if(mActivity.isPermissionGranted(arrPermission)){
            makeCall();

        }else {
            requestPermissions(arrPermission, BookServiceActivity.REQUEST_LOCATION_PERMISSION);
        }
    }

    private void makeCall(){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + mTxtViewPhoneNumber.getText().toString()));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case BookServiceActivity.REQUEST_LOCATION_PERMISSION:
                if(mActivity.isPermissionGranted(grantResults)){
                    makeCall();
                }else{
                    Toast.makeText(mActivity,getString(R.string.str_permission_denied),Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}