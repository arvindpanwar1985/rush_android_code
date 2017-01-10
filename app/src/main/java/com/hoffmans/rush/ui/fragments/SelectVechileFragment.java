package com.hoffmans.rush.ui.fragments;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.hoffmans.rush.R;
import com.hoffmans.rush.listners.OnitemClickListner;
import com.hoffmans.rush.location.LocationData;
import com.hoffmans.rush.location.LocationInterface;
import com.hoffmans.rush.ui.adapters.LoadAddressAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;


public class SelectVechileFragment extends BaseFragment implements OnitemClickListner,View.OnClickListener,GoogleApiClient.OnConnectionFailedListener,LocationInterface {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int  PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final int REQUEST_LOCATION_PERMISSION=2;
    private String TAG=SelectVechileFragment.class.getCanonicalName();
    private int clickedAddressPostion;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<String> listAddressData=new ArrayList<>();
    private View view;
    private RecyclerView recyclerView;
    private LoadAddressAdapter addressAdapter;
    private ImageView imageViewLoadMoreAddress;
    private TextView txtNow,txtReservation;
    private LocationData mLocationData;


    private GoogleApiClient mGoogleApiClient;






    public SelectVechileFragment() {
        // Required empty public constructor
    }



    public static SelectVechileFragment newInstance(String param1, String param2) {
        SelectVechileFragment fragment = new SelectVechileFragment();
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

        mGoogleApiClient = new GoogleApiClient
                .Builder(mActivity)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(mActivity, this)
                .build();

        checkPermission();
    }

    @Override
    protected void initViews(View view) {

        recyclerView            =(RecyclerView)view.findViewById(R.id.addressRecycler);
        imageViewLoadMoreAddress=(ImageView)view.findViewById(R.id.imgAddMoreAddress);
        txtNow                  =(TextView)view.findViewById(R.id.txtACNow);
        txtReservation          =(TextView)view.findViewById(R.id.txtACReserve);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mActivity);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);


    }

    @Override
    protected void initListeners() {
     imageViewLoadMoreAddress.setOnClickListener(this);
     txtNow.setOnClickListener(this);
        txtReservation.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(view==null) {
            view = inflater.inflate(R.layout.fragment_select_vechile, container, false);
            initViews(view);
            initListeners();
            listAddressData.clear();
            listAddressData.add("");

            addressAdapter=new LoadAddressAdapter(mActivity,listAddressData,this);
            recyclerView.setAdapter(addressAdapter);
        }
        return view;
    }





    /**
     * check the permission
     */
    private void checkPermission(){
        String [] arrPermission=new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
        if(mActivity.isPermissionGranted(arrPermission)){
            mLocationData=new LocationData(mActivity,this);

        }else {
            requestPermissions(arrPermission, REQUEST_LOCATION_PERMISSION);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgAddMoreAddress:
                if(listAddressData.size()!=4){
                    listAddressData.add("");
                    addressAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.txtACNow:

                break;
            case R.id.txtACReserve:
                break;
        }
    }


    @Override
    public void onitemclicked(View view, int position) {

       clickedAddressPostion=position;
       try {

            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(mActivity);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == mActivity.RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(mActivity, data);
                try {
                    listAddressData.set(clickedAddressPostion, place.getAddress().toString());
                    addressAdapter.notifyDataSetChanged();
                }catch (Exception e){
                    mActivity.showSnackbar("Something went Wrong",0);
                }

                Log.i(TAG, "Place: " + place.getName()+ ""+place.getAddress());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(mActivity, data);
                mActivity.showSnackbar(status.getStatusMessage(),0);
                } else if (resultCode == RESULT_CANCELED) {
                }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mActivity.showSnackbar(connectionResult.getErrorMessage(),0);
    }


    @Override
    public void onLocation(Location location) {

    }

    @Override
    public void onLocationFailed() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_LOCATION_PERMISSION:
                if(mActivity.isPermissionGranted(grantResults)){
                    mLocationData=new LocationData(mActivity,this);
                }else{
                    Toast.makeText(mActivity,"Permission denied.",Toast.LENGTH_LONG).show();
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
