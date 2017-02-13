package com.hoffmans.rush.ui.driver.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.hoffmans.rush.R;
import com.hoffmans.rush.location.LocationData;
import com.hoffmans.rush.location.LocationInterface;
import com.hoffmans.rush.ui.fragments.BaseFragment;

/**
 * A simple {@link com.hoffmans.rush.ui.fragments.BaseFragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends BaseFragment implements LocationInterface ,OnMapReadyCallback,View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_LOCATION_PERMISSION=2;
    private Location  mCurrentLocation;
    private GoogleMap mGoogleMap;
    private LocationData mLocationData;
    private TextView txtInservice,txtOutOfService;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



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
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        initViews(view);
        initListeners();
        checkPermission();
        return view;
    }



    @Override
    protected void initViews(View view) {

        txtInservice             =(TextView)view.findViewById(R.id.txtInService);
        txtOutOfService          =(TextView)view.findViewById(R.id.txtOutOfservice);
        txtInservice.setSelected(true);
    }

    @Override
    protected void initListeners() {
        txtInservice.setOnClickListener(this);
        txtOutOfService.setOnClickListener(this);
    }




    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txtInService:
                enableInService();
                break;
            case R.id.txtOutOfservice:
                enableOutOfservice();
                break;
        }
    }



    private void enableInService(){

        if(txtOutOfService.isSelected()){
            txtInservice.setBackground(ContextCompat.getDrawable(mActivity,R.drawable.in_service));
            txtInservice.setTextColor(ContextCompat.getColor(mActivity,R.color.colorPrimaryDark));
            txtOutOfService.setBackground(ContextCompat.getDrawable(mActivity,R.drawable.out_of_service));
            txtOutOfService.setTextColor(ContextCompat.getColor(mActivity,android.R.color.white));
            txtInservice.setSelected(true);
            txtOutOfService.setSelected(false);
        }

    }

    private void enableOutOfservice(){
        if(txtInservice.isSelected()){
            txtOutOfService.setBackground(ContextCompat.getDrawable(mActivity,R.drawable.in_service));
            txtOutOfService.setTextColor(ContextCompat.getColor(mActivity,R.color.colorPrimaryDark));
            txtInservice.setBackground(ContextCompat.getDrawable(mActivity,R.drawable.out_of_service));
            txtInservice.setTextColor(ContextCompat.getColor(mActivity,android.R.color.white));
            txtOutOfService.setSelected(true);
            txtInservice.setSelected(false);
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
            addlocationMarker(latLng,R.drawable.marker,mGoogleMap);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
        }
    }
    @Override
    public void onLocationFailed() {

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
            addlocationMarker(latLng,R.drawable.marker,mGoogleMap);
         }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case LocationData.REQUEST_CHECK_SETTINGS:
                switch (resultCode)
                {
                    case Activity.RESULT_OK:
                    {
                        Snackbar.make(getView(),"Location not found ",Snackbar.LENGTH_LONG)
                                .setAction("Try again", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //find the last known location
                                        if(mLocationData!=null&&mLocationData.getLatKnowLocation()!=null){
                                            mCurrentLocation=mLocationData.getLatKnowLocation();
                                            LatLng latLng=new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());
                                            addlocationMarker(latLng,R.drawable.marker,mGoogleMap);
                                        }
                                    }
                                }).show();
                        break;
                    }
                    case Activity.RESULT_CANCELED:
                    {

                        mActivity.finish();
                        // The user was asked to change settings, but chose not to
                        break;
                    }

                }
                break;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationData.disconnect();

    }


}
