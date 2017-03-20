package com.hoffmans.rush.ui.driver.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.hoffmans.rush.R;
import com.hoffmans.rush.bean.BaseBean;
import com.hoffmans.rush.bean.UserBean;
import com.hoffmans.rush.http.request.UserRequest;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.location.LocationData;
import com.hoffmans.rush.location.LocationInterface;
import com.hoffmans.rush.model.User;
import com.hoffmans.rush.services.UpdateCurentLocation;
import com.hoffmans.rush.ui.driver.activities.AcceptOrderActivity;
import com.hoffmans.rush.ui.fragments.BaseFragment;
import com.hoffmans.rush.utils.Progress;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import de.hdodenhof.circleimageview.CircleImageView;

import static org.greenrobot.eventbus.util.ErrorDialogManager.KEY_MESSAGE;


public class HomeFragment extends BaseFragment implements LocationInterface ,OnMapReadyCallback,View.OnClickListener{

    private static final String DRIVER_STATUS_ACTIVE="active";
    private static final String DRIVER_STATUS_INACTIVE="inactive";
    private static final String DRIVER_STATUS_AVAIABLE="available";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private  String KEY_SERVICE_ID   ="service_id";
    private static final int REQUEST_LOCATION_PERMISSION=2;
    private Location  mCurrentLocation;
    private GoogleMap mGoogleMap;
    private LocationData mLocationData;
    private TextView txtInservice,txtOutOfService;
    private CircleImageView imageAcceptReject;
    private String mParam1;
    private String mParam2;
    private View view;
    private String mServiceId;
    private RelativeLayout includedNotificationView;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onStart() {
        super.onStart();
        // set image of user
        if(appPreference.getUserDetails().getPic_url()!=null){
            Glide.with(mActivity).load(appPreference.getUserDetails().getPic_url()).into(imageAcceptReject);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_home, container, false);
        initViews(view);
        initListeners();
        checkPermission();
        showDriverDetails();
        return view;
    }

    @Override
    protected void initViews(View view) {
        txtInservice                =(TextView)view.findViewById(R.id.txtInService);
        txtOutOfService             =(TextView)view.findViewById(R.id.txtOutOfservice);
        imageAcceptReject           =(CircleImageView)view.findViewById(R.id.imgAcceptreject);
        includedNotificationView    =(RelativeLayout)view.findViewById(R.id.notificationBar);
        ( (FrameLayout.LayoutParams) includedNotificationView.getLayoutParams () ).gravity = Gravity.BOTTOM | Gravity.LEFT;

        txtInservice.setSelected(true);
    }


    @Override
    public void onResume() {
        super.onResume();
        appPreference.setPause(false);
        EventBus.getDefault().register(this);
    }


    @Override
    public void onPause() {
        super.onPause();
        appPreference.setPause(true);
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initListeners() {
        txtInservice.setOnClickListener(this);
        txtOutOfService.setOnClickListener(this);
        includedNotificationView.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txtInService:
                enableInService(true);
                break;
            case R.id.txtOutOfservice:
                enableOutOfservice(true);
                break;
            case R.id.notificationBar:
                if(!TextUtils.isEmpty(mServiceId)){
                    Intent acceptOrderIntent=new Intent(mActivity, AcceptOrderActivity.class);
                    acceptOrderIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    acceptOrderIntent.putExtra(KEY_MESSAGE,"");
                    acceptOrderIntent.putExtra(KEY_SERVICE_ID,mServiceId);
                    startActivity(acceptOrderIntent);
                    includedNotificationView.setVisibility(View.GONE);
                }
                break;
        }
    }


    /**
     * callback when notifcation received for new order
     * @param serviceID
     *
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotificationRecevied(String serviceID) {
        includedNotificationView.setVisibility(View.VISIBLE);
        mServiceId=serviceID;
    }

    private void enableInService(boolean showDialog){

        if(txtOutOfService.isSelected()){
            txtInservice.setBackground(ContextCompat.getDrawable(mActivity,R.drawable.in_service));
            txtInservice.setTextColor(ContextCompat.getColor(mActivity,R.color.colorPrimaryDark));
            txtOutOfService.setBackground(ContextCompat.getDrawable(mActivity,R.drawable.out_of_service));
            txtOutOfService.setTextColor(ContextCompat.getColor(mActivity,android.R.color.white));
            txtInservice.setSelected(true);
            txtOutOfService.setSelected(false);
            if(showDialog) {
                String message = "Are you sure you are In service?";
                showStatusDialog(message, true);
            }

        }
    }

    private void enableOutOfservice(boolean showDialog){
        if(txtInservice.isSelected()){
            txtOutOfService.setBackground(ContextCompat.getDrawable(mActivity,R.drawable.in_service));
            txtOutOfService.setTextColor(ContextCompat.getColor(mActivity,R.color.colorPrimaryDark));
            txtInservice.setBackground(ContextCompat.getDrawable(mActivity,R.drawable.out_of_service));
            txtInservice.setTextColor(ContextCompat.getColor(mActivity,android.R.color.white));
            txtOutOfService.setSelected(true);
            txtInservice.setSelected(false);
            if(showDialog) {
                String message = "Are you sure you are Out of service?";
                showStatusDialog(message, false);
            }
        }
    }


    /**
     * api call to update driver status
     * @param status status active/inactive
     */
   private  void setDriverStaus(final String status){
       Progress.showprogress(mActivity,getString(R.string.progress_loading),false);
       UserRequest userRequest=new UserRequest();
       String token=appPreference.getUserDetails().getToken();
       userRequest.updateDriverStatus(token, status, new ApiCallback() {
           @Override
           public void onRequestSuccess(BaseBean body) {
               Progress.dismissProgress();
               if(status.equals(DRIVER_STATUS_AVAIABLE)){
                   enableInService(false);
               }else if(status.equals(DRIVER_STATUS_INACTIVE)){
                   enableOutOfservice(false);
               }
           }

           @Override
           public void onRequestFailed(String message) {
               Progress.dismissProgress();
               mActivity.showSnackbar(message,Toast.LENGTH_LONG);
           }
       });
   }


    /**
     * set the driver status to active
     */
    private void showDriverDetails(){

        Progress.showprogress(mActivity,getString(R.string.progress_loading),false);
        UserRequest userRequest=new UserRequest();
        String token=appPreference.getUserDetails().getToken();
        int id=appPreference.getUserDetails().getId();
        if(id!=0) {
            String url = "/api/user/" + String.valueOf(id);
            userRequest.driverShow(token, url, new ApiCallback() {
                @Override
                public void onRequestSuccess(BaseBean body) {
                    Progress.dismissProgress();
                    UserBean bean = (UserBean) body;
                    User user = bean.getUser();
                    if (user != null) {
                        if (user.getStatus().equals(DRIVER_STATUS_ACTIVE)) {
                            txtInservice.setSelected(false);
                            txtOutOfService.setSelected(true);
                            enableInService(false);
                        } else if (user.getStatus().equals(DRIVER_STATUS_INACTIVE)) {
                            txtInservice.setSelected(true);
                            txtOutOfService.setSelected(false);
                            enableOutOfservice(false);
                        }
                    }
                }
                @Override
                public void onRequestFailed(String message) {
                    Progress.dismissProgress();
                    mActivity.showSnackbar(message, Toast.LENGTH_LONG);
                }
            });
        }
    }

    private void showStatusDialog(String message, final boolean inService){
        try {
            String positiveButtonText=(inService)?getString(R.string.in_service):getString(R.string.out_service);
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity);
            builder.setTitle(R.string.app_name)
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            String status=(inService)?DRIVER_STATUS_AVAIABLE:DRIVER_STATUS_INACTIVE;
                            setDriverStaus(status);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(getString(R.string.str_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(inService){
                                enableOutOfservice(false);
                            }else{
                                enableInService(false);
                            }
                            dialog.dismiss();



                        }
                    }).create().show();
        }catch (Exception e){

        }
    }
    /**
     * check the permission
     */
    private void checkPermission(){
        String [] arrPermission=new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
        if(mActivity.isPermissionGranted(arrPermission)){
           mLocationData=new LocationData(mActivity,this);
           initMap();
        }else {
            requestPermissions(arrPermission, REQUEST_LOCATION_PERMISSION);
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
    public void onLocation(Location location) {
        mCurrentLocation=location;
        if(mCurrentLocation!=null && mGoogleMap!=null){
            LatLng latLng=new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());
            addlocationMarker(latLng,R.drawable.marker,mGoogleMap,false);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

            //update the user location
            String auth      =appPreference.getUserDetails().getToken();
            String latitude  =String.valueOf(mCurrentLocation.getLatitude());
            String longitude =String.valueOf(mCurrentLocation.getLongitude());
            updateUserLocation(auth,latitude,longitude);
        }
    }
    @Override
    public void onLocationFailed() {
        Toast.makeText(mActivity,"Failed to get lcoation!",Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap=googleMap;
        try {
            mGoogleMap.setMyLocationEnabled(true);
        }catch (SecurityException e){
        }
        if(mCurrentLocation!=null && mGoogleMap!=null){
            LatLng latLng=new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());
            addlocationMarker(latLng,R.drawable.marker,mGoogleMap,false);
         }
    }


    /**
     * Intent service to update userLocation
     * @param auth
     * @param latitude
     * @param longitude
     */
    private void updateUserLocation(String auth ,String latitude,String longitude){

        UpdateCurentLocation.startLocationUpdate(mActivity,auth,latitude,longitude);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case LocationData.REQUEST_CHECK_SETTINGS:
                switch (resultCode)
                {
                    case Activity.RESULT_OK://user press turn on location button in location setting dialog
                    {
                        Snackbar.make(view,"Location not found ", Snackbar.LENGTH_LONG)
                                .setAction("Try again", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //find the last known location
                                        if(mLocationData!=null&&mLocationData.getLatKnowLocation()!=null){
                                            mCurrentLocation=mLocationData.getLatKnowLocation();
                                            LatLng latLng=new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());
                                            addlocationMarker(latLng,R.drawable.marker,mGoogleMap,false);

                                            //update the user location
                                            String auth      =appPreference.getUserDetails().getToken();
                                            String latitude  =String.valueOf(mCurrentLocation.getLatitude());
                                            String longitude =String.valueOf(mCurrentLocation.getLongitude());
                                            updateUserLocation(auth,latitude,longitude);
                                        }
                                    }
                                }).setDuration(6000)
                                .show();
                        break;
                    }
                    case Activity.RESULT_CANCELED:
                    {
                        // The user was asked to change settings, but chose not to
                        mActivity.finish();
                        break;
                    }

                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mLocationData!=null) {
            mLocationData.disconnect();
            mLocationData=null;
        }


    }
}
