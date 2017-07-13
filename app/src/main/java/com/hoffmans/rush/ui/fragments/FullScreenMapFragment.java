package com.hoffmans.rush.ui.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hoffmans.rush.R;
import com.hoffmans.rush.listners.ServiceCallbacks;
import com.hoffmans.rush.model.DateTime;
import com.hoffmans.rush.model.DriverDetail;
import com.hoffmans.rush.model.Estimate;
import com.hoffmans.rush.model.PickDropAddress;
import com.hoffmans.rush.model.VechileDetail;
import com.hoffmans.rush.services.UpdateDriversOnMapService;
import com.hoffmans.rush.ui.activities.BookServiceActivity;
import com.hoffmans.rush.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by arvind on 15/6/17.
 */

public class FullScreenMapFragment extends BaseFragment implements OnMapReadyCallback, ServiceCallbacks {

    private static final String LOCALE_ES="locale_es";
    private static final String LOCALE_EN="locale_en";

    private static final String STATIC_MAP_URL = "http://maps.google.com/maps/api/staticmap?center=";
    private int height, widht;

    private Estimate estimateData;
    private VechileDetail vehicleData;
    private DriverDetail driverData;
    private PickDropAddress pickAddress;
    private DateTime dateTime;
    List<PickDropAddress> drop_down;
    private String comment,status,serviceId;
    private double driverLat, driverLng;

    private GoogleMap mGoogleMap;
    private boolean mBound;
    private UpdateDriversOnMapService mService;
   // private List<Marker> lastVisibleMeakers = new ArrayList<>();
    private Marker newMArker;

    public FullScreenMapFragment() {

    }

    public static FullScreenMapFragment newInstance(Estimate param1, VechileDetail param2, DriverDetail param3, PickDropAddress param4, DateTime param5,
                                                    List<PickDropAddress> drop_down, String records, String status, String serviceId, double latitude, double longitude) {

        FullScreenMapFragment fragment = new FullScreenMapFragment();

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
        args.putDouble(Constants.KEY_LAT,latitude);
        args.putDouble(Constants.KEY_LONG,longitude);
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
            driverLat=getArguments().getDouble(Constants.KEY_LAT);
            driverLng=getArguments().getDouble(Constants.KEY_LONG);

            Log.e("lat..#####.",driverLat+"");
            Log.e("longitude..####.",driverLng+"");



        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity.initToolBar("", true, true);
        View view = inflater.inflate(R.layout.fragment_full_screenmap, container, false);
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

        checkPermission();
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

        if(driverData.getId()==Integer.parseInt(driver_id) && driverStatus.equals("active") && serviceId.equals(service_id)) {

            int vechile_type_id = Integer.parseInt(vechile_id);
            if (newMArker != null) {
                newMArker.setPosition(driverLAtLng);
            }else{
                newMArker = mGoogleMap.addMarker(new MarkerOptions().position(driverLAtLng).draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_green_marker)));
                newMArker.setPosition(driverLAtLng);
                //newMArker.setTag(driverData.getId());
            }
        }else{
            if(newMArker!=null)
                newMArker.remove();
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




    /**
     *
     * @return terms and policy url according to locale
     */
    private String getLocale(){
        String locale= Locale.getDefault().getLanguage();
        if(locale.equals("es")){
            return LOCALE_ES;
        }
        return LOCALE_EN;
    }

    // if driver found then set marker on position
    private void setDriverLocation(double latitude, double longitude){
        LatLng latLng = new LatLng(latitude,longitude);
        newMArker = mGoogleMap.addMarker(new MarkerOptions().position(latLng).draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_green_marker)));
        newMArker.setTag(driverData.getId());
        //  lastVisibleMeakers.add(newMArker);
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

        setDriverLocation(driverLat,driverLng);

        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(6.0f ) );
    }


}
