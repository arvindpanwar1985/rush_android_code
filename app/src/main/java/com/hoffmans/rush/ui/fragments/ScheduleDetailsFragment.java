package com.hoffmans.rush.ui.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hoffmans.rush.R;
import com.hoffmans.rush.bean.BaseBean;
import com.hoffmans.rush.bean.TrackDriverBean;
import com.hoffmans.rush.http.request.ServiceRequest;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.listners.ServiceCallbacks;
import com.hoffmans.rush.model.DateTime;
import com.hoffmans.rush.model.DriverDetail;
import com.hoffmans.rush.model.Estimate;
import com.hoffmans.rush.model.PickDropAddress;
import com.hoffmans.rush.model.VechileDetail;
import com.hoffmans.rush.services.UpdateDriversOnMapService;
import com.hoffmans.rush.ui.activities.BookServiceActivity;
import com.hoffmans.rush.ui.activities.FullScreenMapActivity;
import com.hoffmans.rush.utils.Constants;
import com.hoffmans.rush.utils.Progress;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by arvind on 13/6/17.
 */

public class ScheduleDetailsFragment extends BaseFragment implements OnMapReadyCallback,View.OnClickListener, ServiceCallbacks {

    private static final String LOCALE_ES="locale_es";
    private static final String LOCALE_EN="locale_en";

    private ImageView mImageViewMap;
    private ImageView mImageViewDriver;
    private ImageView mImageViewVehicleIcon;
    private ImageView mImageFullScreen;
    private TextView mTxtViewDriverName, mTxtViewPhoneNumber, mTxtViewCost, mTxtViewDateTime;
    private TextView mTxtViewVehicleColor, mTxtViewPlateNumber;
    private TextView mTxtPickupLocation, mTxtViewDropAddres1, mTxtViewDropAddres2, mTxtViewDropAddres3;
    private TextView mTxtViewComment;
    private static final String STATIC_MAP_URL = "http://maps.google.com/maps/api/staticmap?center=";
    private int height, widht;

    private Estimate estimateData;
    private VechileDetail vehicleData;
    private DriverDetail driverData;
    private PickDropAddress pickAddress;
    private DateTime dateTime;
    List<PickDropAddress> drop_down;
    private String comment,status,serviceId;
    private double driverLat,driverLng;
    private int driverId;
    private GoogleMap mGoogleMap;
    private boolean mBound;
    private UpdateDriversOnMapService mService;
    //private List<Marker> lastVisibleMeakers = new ArrayList<>();
    private Marker newMArker;

    public ScheduleDetailsFragment() {

    }

    public static ScheduleDetailsFragment newInstance(Estimate param1, VechileDetail param2, DriverDetail param3, PickDropAddress param4, DateTime param5,
                                                      List<PickDropAddress> drop_down, String records, String status,String serviceId) {

        ScheduleDetailsFragment fragment = new ScheduleDetailsFragment();

        Bundle args = new Bundle();
        args.putParcelable(Constants.KEY_ESTIMATE_DATA, param1);
        args.putParcelable(Constants.KEY_VEHICLE_DETAILS, param2);
        args.putParcelable(Constants.KEY_DRIVER_DETAILS, param3);
        args.putParcelable(Constants.KEY_PICK_ADDRESS, param4);
        args.putParcelable(Constants.KEY_DATA_DATE_TIME, param5);
        args.putParcelableArrayList(Constants.KEY_DROP_DOWN, (ArrayList<? extends Parcelable>) drop_down);
        args.putString(Constants.KEY_COMMENT, records);
        args.putString(Constants.SERVICE_STATUS,status);
        args.putString(Constants.SERVICE_ID,serviceId);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            estimateData = getArguments().getParcelable(Constants.KEY_ESTIMATE_DATA);
            vehicleData = getArguments().getParcelable(Constants.KEY_VEHICLE_DETAILS);
            driverData = getArguments().getParcelable(Constants.KEY_DRIVER_DETAILS);
            pickAddress = getArguments().getParcelable(Constants.KEY_PICK_ADDRESS);
            dateTime = getArguments().getParcelable(Constants.KEY_DATA_DATE_TIME);
            drop_down = getArguments().getParcelableArrayList(Constants.KEY_DROP_DOWN);
            comment = getArguments().getString(Constants.KEY_COMMENT);
            status=getArguments().getString(Constants.SERVICE_STATUS);
            serviceId=getArguments().getString(Constants.SERVICE_ID);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity.initToolBar(getString(R.string.str_order_details), true, false);
        View view = inflater.inflate(R.layout.fragment_schedule_details, container, false);
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


       // mImageViewMap = (ImageView) view.findViewById(R.id.mapImage);
        mImageViewDriver = (ImageView) view.findViewById(R.id.fEPImgProfile);
        mImageViewVehicleIcon = (ImageView) view.findViewById(R.id.img_vehicle);
        mImageFullScreen=(ImageView)view.findViewById(R.id.img_full_screenmap) ;
        mTxtViewDriverName = (TextView) view.findViewById(R.id.txt_name);
        mTxtViewPhoneNumber = (TextView) view.findViewById(R.id.txt_phone_number);
        mTxtViewCost = (TextView) view.findViewById(R.id.txt_currenty_type);
        mTxtViewDateTime = (TextView) view.findViewById(R.id.txt_date_time);
        mTxtViewVehicleColor = (TextView) view.findViewById(R.id.txt_vehicle_color);
        mTxtViewPlateNumber = (TextView) view.findViewById(R.id.txt_vehicle_plate);
        mTxtPickupLocation = (TextView) view.findViewById(R.id.txt_pickup_address);
        mTxtViewDropAddres1 = (TextView) view.findViewById(R.id.txt_drop_address1);
        mTxtViewDropAddres2 = (TextView) view.findViewById(R.id.txt_drop_address2);
        mTxtViewDropAddres3 = (TextView) view.findViewById(R.id.txt_drop_address3);
        mTxtViewComment = (TextView) view.findViewById(R.id.txt_show_comment);

        setDetailsValues();
        driverTracking();

        checkPermission();
    }

    private void setDetailsValues() {


       /* if (getImageMap(pickAddress) != null) {
            Glide.with(mActivity).load(getImageMap(pickAddress)).into(mImageViewMap);
        }*/

        if(driverData!=null){
            mTxtViewDriverName.setText(driverData.getName());
            mTxtViewPhoneNumber.setText(driverData.getPhone());


            if(getLocale().equals(LOCALE_EN)){
                mTxtViewVehicleColor.setText("Color : " + driverData.getColor());
            }else{
                mTxtViewVehicleColor.setText("Color : " + driverData.getColor_es());
            }

            mTxtViewPlateNumber.setText("Plate No: " + driverData.getPlate_number());

            if (driverData.getPicUrl() != null) {
                Glide.with(mActivity).load(driverData.getPicUrl()).into(mImageViewDriver);
            }

        }

       /* if(status!=null) {
            if (status.equals("accepted") || status.equals("pending")) {
                mTxtViewCost.setVisibility(View.GONE);
            } else {
                mTxtViewCost.setVisibility(View.VISIBLE);
                mTxtViewCost.setText(estimateData.getSymbol() + estimateData.getApproxConvertedAmount().toString());
            }
        }
*/

        if (vehicleData.getName().equals("Motorcycle")) {
            mImageViewVehicleIcon.setImageResource(R.drawable.moto_icon);

        } else if (vehicleData.getName().equals("Bike")) {
            mImageViewVehicleIcon.setImageResource(R.drawable.bike_icon);

        } else if (vehicleData.getName().equals("Vehicule")) {
            mImageViewVehicleIcon.setImageResource(R.drawable.car_icon);
        } else if (vehicleData.getName().equals("Truck upto 2tons")) {
            mImageViewVehicleIcon.setImageResource(R.drawable.truck_icon);
        }


        mTxtViewCost.setText(estimateData.getSymbol() + estimateData.getApproxConvertedAmount().toString());
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
    public void onStart() {
        super.onStart();
        if (!mBound) {
            bindService(mActivity);
        }
    }

    /**
     * binding activity to service
     *
     * @param activity
     */
    public void bindService(Activity activity) {

        Intent bindIntent = new Intent(mActivity, UpdateDriversOnMapService.class);
        mActivity.bindService(bindIntent, mConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            UpdateDriversOnMapService.MyBinder binder = (UpdateDriversOnMapService.MyBinder) service;
            mService = binder.getService();
            mBound = true;
            //register the service callback when server(Service)connected with activity
            setServiceCallback();
            Intent intent = new Intent(mActivity, UpdateDriversOnMapService.class);
            mActivity.startService(intent);


        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }


    };

    /**
     * set the service callbacks in UpdateDriverLocation
     */
    private void setServiceCallback() {
        mService.setServiceCallBack(this);

    }

    @Override
    public void onDataRecieved(final String driver_id, final double latitude, final double longitude, final String vechile_id, String driverStatus, String service_id) {
        final LatLng driverLAtLng = new LatLng(latitude, longitude);
       if(driverData!=null){

           if (driverData.getId() == Integer.parseInt(driver_id) && driverStatus.equals("active")) {
               if(service_id!=null)
                   Log.e("service running...", "####");
               if(service_id!=null) {
                   if (newMArker != null) {
                       newMArker.setPosition(driverLAtLng);
                   } else {
                       newMArker = mGoogleMap.addMarker(new MarkerOptions().position(driverLAtLng).draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_green_marker)));
                       //newMArker.setTag(driverData.getId());
                   }
               }
           } else {
               if (newMArker != null) {
                   mImageFullScreen.setVisibility(View.GONE);
                   newMArker.remove();
               }

           }
       }






    }





    @Override
    public void onStop() {
        super.onStop();
        stopService();
    }

    private void stopService() {
        if (mBound) {
            //unbind the service with activity
            mActivity.unbindService(mConnection);
            //stop service
            Intent intent = new Intent(mActivity, UpdateDriversOnMapService.class);
            mActivity.stopService(intent);
            mBound = false;
        }
    }

    /**
     * check the permission
     */
    private void checkPermission(){
        String [] arrPermission=new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
        if(mActivity.isPermissionGranted(arrPermission)){

            initMap();
        }else {
            requestPermissions(arrPermission, BookServiceActivity.REQUEST_LOCATION_PERMISSION);
        }
    }


    /**
     * init the map fragment
     */
    private void initMap(){
        SupportMapFragment mapFragment = ((SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map_fragment));
        if(mapFragment!=null){
            mapFragment.getMapAsync(this);
        }else {
            Toast.makeText(getActivity(),"Error in iniializing map",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void initListeners() {
        mTxtViewPhoneNumber.setOnClickListener(this);
        mImageFullScreen.setOnClickListener(this);
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
            checkPermisstion();

        }
        if(view.getId()==mImageFullScreen.getId()){
            Intent intent=new Intent(mActivity, FullScreenMapActivity.class);
            intent.putExtra(Constants.KEY_ESTIMATE_DATA,estimateData);
            intent.putExtra(Constants.KEY_VEHICLE_DETAILS,vehicleData);
            intent.putExtra(Constants.KEY_DRIVER_DETAILS,driverData);
            intent.putExtra(Constants.KEY_PICK_ADDRESS,pickAddress);
            intent.putExtra(Constants.KEY_DATA_DATE_TIME,dateTime);
            intent.putParcelableArrayListExtra(Constants.KEY_DROP_DOWN, (ArrayList<? extends Parcelable>) drop_down);
            intent.putExtra(Constants.KEY_COMMENT,comment);
            intent.putExtra(Constants.SERVICE_STATUS,status);
            intent.putExtra(Constants.SERVICE_ID,serviceId);
            intent.putExtra(Constants.KEY_LAT,driverLat);
            intent.putExtra(Constants.KEY_LONG,driverLng);

            startActivity(intent);
        }
    }


    private void driverTracking(){

            Progress.showprogress(mActivity,getString(R.string.progress_loading),false);
            ServiceRequest request=new ServiceRequest();
            request.getTrackDriverDetails(appPreference.getUserDetails().getToken(), serviceId, new ApiCallback() {
                @Override
                public void onRequestSuccess(BaseBean body) {
                    Progress.dismissProgress();
                    TrackDriverBean trackDriverBean=(TrackDriverBean) body;
                    double lat=trackDriverBean.getLatitude();
                   // trackDriverBean.getData().getLatitude();
                    mActivity.showSnackbar(trackDriverBean.getMessage(),0);
                    mImageFullScreen.setVisibility(View.VISIBLE);
                    driverLat=trackDriverBean.getLatitude();
                    driverLng=trackDriverBean.getLongitude();
                    driverId=trackDriverBean.getDriverId();
                    // set driver lat long marker position;
                    setDriverLocation(trackDriverBean.getLatitude(),trackDriverBean.getLongitude(),trackDriverBean.getDriverId());

                }
                @Override
                public void onRequestFailed(String message) {
                    mActivity.showSnackbar(message,0);
                    mImageFullScreen.setVisibility(View.GONE);
                    Progress.dismissProgress();
                    if(message.equals(Constants.AUTH_ERROR)){
                        mActivity.logOutUser();
                    }
                }
            });



    }

    // if driver found then set marker on position
    private void setDriverLocation(double latitude, double longitude, int driverId){
        LatLng latLng = new LatLng(latitude,longitude);
        newMArker = mGoogleMap.addMarker(new MarkerOptions().position(latLng).draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_green_marker)));
        newMArker.setTag(driverData.getId());
      //  lastVisibleMeakers.add(newMArker);
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

    /**
     *
     * @return terms and policy url according to locale
     */
    private String getLocale(){
        String locale= Locale.getDefault().getLanguage();
        Log.e("locale...",locale);

        if(locale.equals("es")){
            return LOCALE_ES;
        }
        return LOCALE_EN;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap=googleMap;
        //mGoogleMap.setMinZoomPreference(1.0f);

        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        int size=drop_down.size();
        // we are setting marker on drop address by using this loop
        for(int i=0;i<size;i++){

            LatLng latLng = new LatLng(drop_down.get(i).getLatitude(), drop_down.get(i).getLongitude());
            Marker marker = addlocationMarker(latLng, R.drawable.marker, mGoogleMap, false);
           // marker.setTitle(drop_down.get(i).getStreetAddress());
        }
        // set marker for pic address
        if(pickAddress.getStreetAddress()!=null && !pickAddress.getStreetAddress().equals("")) {
            LatLng latLng = new LatLng(pickAddress.getLatitude(),pickAddress.getLongitude());
            Marker marker = addlocationMarker(latLng, R.drawable.ic_map_blue_marker, mGoogleMap, false);
           // marker.setTitle(pickAddress.getStreetAddress());
        }

        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(6.0f ) );

    }


}
