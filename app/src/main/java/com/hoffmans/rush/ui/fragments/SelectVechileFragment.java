package com.hoffmans.rush.ui.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.hoffmans.rush.R;
import com.hoffmans.rush.listners.OnitemClickListner;
import com.hoffmans.rush.location.LocationData;
import com.hoffmans.rush.location.LocationInterface;
import com.hoffmans.rush.model.FetchAddressEvent;
import com.hoffmans.rush.model.PickDropAddress;
import com.hoffmans.rush.model.Service;
import com.hoffmans.rush.services.BuildAddressService;
import com.hoffmans.rush.ui.adapters.LoadAddressAdapter;
import com.hoffmans.rush.utils.DateUtils;
import com.hoffmans.rush.utils.Progress;
import com.hoffmans.rush.utils.Utils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;


public class SelectVechileFragment extends BaseFragment implements OnitemClickListner.OnFrequentAddressClicked,View.OnClickListener,GoogleApiClient.OnConnectionFailedListener,LocationInterface ,OnMapReadyCallback{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int  PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final int REQUEST_LOCATION_PERMISSION=2;
    private static final int DESTINATION_SELECTED=1;
    private String TAG=SelectVechileFragment.class.getCanonicalName();
    private int clickedAddressPostion;

    private UiHandler mHandler=new UiHandler();


    private String mParam1;
    private String mParam2;
    private ArrayList<PickDropAddress> listAddressData=new ArrayList<>();
    private View view;
    private RecyclerView recyclerView;
    private LoadAddressAdapter addressAdapter;
    private ImageView imageViewLoadMoreAddress;
    private TextView txtNow,txtReservation;
    private LocationData mLocationData;
    private TextView txtEstimateCost;
    private ImageView imgTypeCycle,imgTypeBike,imgTypeCar,imgTypeTruck;
    private GoogleMap mGoogleMap;
    private Location mCurrentLocation;
    private boolean isVehicleSelected;
    private DateUtils mDateUtils;


    private GoogleApiClient mGoogleApiClient;
    private Geocoder mGeocoder = new Geocoder(mActivity, Locale.getDefault());





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

        mDateUtils=DateUtils.getInstance();


        mGoogleApiClient = new GoogleApiClient
                .Builder(mActivity)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addOnConnectionFailedListener(this)
                .build();


    }

    @Override
    protected void initViews(View view) {

        recyclerView            =(RecyclerView)view.findViewById(R.id.addressRecycler);
        imageViewLoadMoreAddress=(ImageView)view.findViewById(R.id.imgAddMoreAddress);
        txtNow                  =(TextView)view.findViewById(R.id.txtACNow);
        txtReservation          =(TextView)view.findViewById(R.id.txtACReserve);
        txtEstimateCost         =(TextView)view.findViewById(R.id.txtEstimatePrice);
        recyclerView.setHasFixedSize(true);
        imgTypeCycle            =(ImageView)view.findViewById(R.id.imgTypeCycle);
        imgTypeBike             =(ImageView)view.findViewById(R.id.imgTypeBike);
        imgTypeCar              =(ImageView)view.findViewById(R.id.imgTypeCar);
        imgTypeTruck            =(ImageView)view.findViewById(R.id.imgTypeTruck);

        LinearLayoutManager llm = new LinearLayoutManager(mActivity);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        txtNow.setSelected(true);



    }

    @Override
    protected void initListeners() {
        imageViewLoadMoreAddress.setOnClickListener(this);
        txtNow.setOnClickListener(this);
        txtReservation.setOnClickListener(this);
        txtEstimateCost.setOnClickListener(this);
        imgTypeCar.setOnClickListener(this);
        imgTypeCycle.setOnClickListener(this);
        imgTypeTruck.setOnClickListener(this);
        imgTypeBike.setOnClickListener(this);

    }

    private void initMap(){
       SupportMapFragment mapFragment = ((SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map_fragment));
        if(mapFragment!=null){
        mapFragment.getMapAsync(this);
        }else {
            Toast.makeText(getActivity(),"Error in iniializing map",Toast.LENGTH_SHORT).show();
        }
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
            PickDropAddress start_address =new PickDropAddress();
            PickDropAddress end_address   =new PickDropAddress();
            start_address.setStreetAddress("");
            end_address.setStreetAddress("");
            listAddressData.add(start_address);
            listAddressData.add(end_address);
            addressAdapter=new LoadAddressAdapter(mActivity,listAddressData,this);
            recyclerView.setAdapter(addressAdapter);
            checkPermission();
        }
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }


    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgAddMoreAddress:
                if(listAddressData.size()!=4){
                    PickDropAddress newDetination=new PickDropAddress();
                    newDetination.setStreetAddress("");
                    listAddressData.add(newDetination);
                    addressAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.txtACNow:
                enableNow();
                 break;
            case R.id.txtACReserve:
                enableReservation();
                break;
            case R.id.txtEstimatePrice:
                validateFields();
                //Intent confirmSercviceIntet=new Intent(mActivity, ConfirmServiceActivity.class);
               // confirmSercviceIntet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //startActivity(confirmSercviceIntet);
                break;

            case R.id.imgTypeCycle:
                setBackgroundVehicle(imgTypeCycle);
                break;
            case R.id.imgTypeBike:
                setBackgroundVehicle(imgTypeBike);
                break;
            case R.id.imgTypeCar:
                setBackgroundVehicle(imgTypeCar);
                break;
            case R.id.imgTypeTruck:
                setBackgroundVehicle(imgTypeTruck);
                break;
        }
    }


    private void validateFields(){

        if(listAddressData==null){
            return;
        }
        String source =listAddressData.get(0).getStreetAddress();
        String destination=listAddressData.get(1).getStreetAddress();
        if(TextUtils.isEmpty(source)){
            mActivity.showSnackbar("Please select source",0);
            return;
        }
        if(TextUtils.isEmpty(destination)){
            mActivity.showSnackbar("Please select destination",0);
            return;
        }
        if(!isVehicleSelected && getVechileType()==-1){
            mActivity.showSnackbar("Please select vehicle",0);
            return;
        }

         Progress.showprogress(mActivity,"Please wait..",false);
         BuildAddressService.buildAddresses(mActivity,listAddressData);


    }


    /**
     *
     * @param event from buildAddress intent service
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFetchAddressEvent(FetchAddressEvent event) {
        Progress.dismissProgress();
        if(event.isSucess()){

            DateUtils dateUtils=DateUtils.getInstance();
            Service service =event.getService();
            service.setVehicle_type_id(getVechileType());
            if(dateUtils.getUtcDateTime()!=null){
             service.setDate(dateUtils.getUtcDateTime());
            }

        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap=googleMap;
        try {
            mGoogleMap.setMyLocationEnabled(true);
        }catch (SecurityException e){

        }
        if(mCurrentLocation!=null){
            LatLng latLng=new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());
            addlocationMarker(latLng,R.drawable.marker,mGoogleMap);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
        }
    }



    private void enableNow(){

        if(txtReservation.isSelected()){
            txtNow.setBackground(ContextCompat.getDrawable(mActivity,R.drawable.bg_now_btn));
            txtNow.setTextColor(ContextCompat.getColor(mActivity,R.color.colorPrimaryDark));
            txtReservation.setBackground(ContextCompat.getDrawable(mActivity,R.drawable.bg_reservation_btn));
            txtReservation.setTextColor(ContextCompat.getColor(mActivity,android.R.color.white));
            txtNow.setSelected(true);
        }

    }

    private void enableReservation(){
        if(txtNow.isSelected()){
            txtReservation.setBackground(ContextCompat.getDrawable(mActivity,R.drawable.bg_now_btn));
            txtReservation.setTextColor(ContextCompat.getColor(mActivity,R.color.colorPrimaryDark));
            txtNow.setBackground(ContextCompat.getDrawable(mActivity,R.drawable.bg_reservation_btn));
            txtNow.setTextColor(ContextCompat.getColor(mActivity,android.R.color.white));
            txtReservation.setSelected(true);
            showDatePicker();

        }
    }


    private  int getVechileType(){
        if(imgTypeCar.isSelected()){
            return 2;
        }
        if(imgTypeBike.isSelected()){
            return 1;
        }
        if(imgTypeCycle.isSelected()){
            return 0;
        }
        if(imgTypeTruck.isSelected()){
            return 3;
        }
        return  -1;
    }

    private void setBackgroundVehicle(ImageView view){
        if(view.isSelected()){
            view.setBackgroundColor(ContextCompat.getColor(mActivity,android.R.color.transparent));
            view.setSelected(false);
            isVehicleSelected=false;
        }else{
            view.setBackground(ContextCompat.getDrawable(mActivity,R.drawable.selection_ring));
            view.setSelected(true);
            isVehicleSelected=true;
        }
        int id=view.getId();
        if(id==R.id.imgTypeCycle){
            imgTypeCar.setBackgroundColor(ContextCompat.getColor(mActivity,android.R.color.transparent));
            imgTypeBike.setBackgroundColor(ContextCompat.getColor(mActivity,android.R.color.transparent));
            imgTypeTruck.setBackgroundColor(ContextCompat.getColor(mActivity,android.R.color.transparent));
            imgTypeCar.setSelected(false);
            imgTypeBike.setSelected(false);
            imgTypeTruck.setSelected(false);

        }else if(id==R.id.imgTypeBike){
            imgTypeCar.setBackgroundColor(ContextCompat.getColor(mActivity,android.R.color.transparent));
            imgTypeCycle.setBackgroundColor(ContextCompat.getColor(mActivity,android.R.color.transparent));
            imgTypeTruck.setBackgroundColor(ContextCompat.getColor(mActivity,android.R.color.transparent));
            imgTypeCar.setSelected(false);
            imgTypeCycle.setSelected(false);
            imgTypeTruck.setSelected(false);

        }else if(id==R.id.imgTypeCar){
            imgTypeCycle.setBackgroundColor(ContextCompat.getColor(mActivity,android.R.color.transparent));
            imgTypeBike.setBackgroundColor(ContextCompat.getColor(mActivity,android.R.color.transparent));
            imgTypeTruck.setBackgroundColor(ContextCompat.getColor(mActivity,android.R.color.transparent));
            imgTypeCycle.setSelected(false);
            imgTypeBike.setSelected(false);
            imgTypeTruck.setSelected(false);
        }else{
            imgTypeCar.setBackgroundColor(ContextCompat.getColor(mActivity,android.R.color.transparent));
            imgTypeBike.setBackgroundColor(ContextCompat.getColor(mActivity,android.R.color.transparent));
            imgTypeCycle.setBackgroundColor(ContextCompat.getColor(mActivity,android.R.color.transparent));
            imgTypeCar.setSelected(false);
            imgTypeBike.setSelected(false);
            imgTypeCycle.setSelected(false);
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

        } catch (GooglePlayServicesNotAvailableException e) {

        }
    }

    @Override
    public void onfrequentAddressclicked(View view, int position) {

    }

    @Override
    public void onFavoriteAddressclicked(View view, final int position) {
        try {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity);
            builder.setTitle(R.string.app_name)
                    .setMessage(R.string.str_fav_message)
                    .setCancelable(false)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            PickDropAddress favouritePickDropAddress= listAddressData.get(position);
                            // Thread and Handler using Geocoder
                            Thread thread = new Thread(new FindAddress(favouritePickDropAddress));
                            thread.start();

                        }
                    }).create().show();
        }catch (Exception e){

        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == mActivity.RESULT_OK) {
                try {
                    Place place = PlaceAutocomplete.getPlace(mActivity, data);
                    Log.i(TAG, "Place: " + place.getName()+ ""+place.getAddress());
                    setAddress(place, clickedAddressPostion);
                }catch (Exception e){

                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(mActivity, data);
                mActivity.showSnackbar(status.getStatusMessage(),0);
                } else if (resultCode == RESULT_CANCELED) {
                }
        }
    }


    /**
     *
     * @param place place from autocompleter
     * @param position postion of list to be refreshed.
     */
    private void setAddress(Place place ,int position){

            PickDropAddress pickDropAddress=listAddressData.get(position);
            pickDropAddress.setStreetAddress(place.getAddress().toString());
            pickDropAddress.setLatitude(place.getLatLng().latitude);
            pickDropAddress.setLongitude(place.getLatLng().longitude);
            listAddressData.set(position, pickDropAddress);
            addressAdapter.notifyDataSetChanged();
            if(position==DESTINATION_SELECTED){
                txtEstimateCost.setVisibility(View.VISIBLE);
            }

    }


    private void showDatePicker(){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        String date =mActivity.formatDate(year,monthOfYear,dayOfMonth);
                        showTimePicker(date);
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setTitle("Select date");
        dpd.show(mActivity.getFragmentManager(), "Datepickerdialog");
    }



    private void showTimePicker(final String date){
        Calendar now = Calendar.getInstance();
        TimePickerDialog dpd = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

                        Log.e("dateTime",date+" "+hourOfDay+":"+minute+":"+second);
                        String dateTime=date+"T"+hourOfDay+":"+minute+":"+second+"Z";
                        Utils.showAlertDialog(mActivity,mDateUtils.getUtcDateTime(dateTime));
                    }
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                now.get(Calendar.SECOND),true
        );
        dpd.show(mActivity.getFragmentManager(), "Datepickerdialog");
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mActivity.showSnackbar(connectionResult.getErrorMessage(),0);
    }


    @Override
    public void onLocation(Location location) {
     if(location!=null){
         mCurrentLocation=location;
     }
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
                    initMap();
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
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(mActivity);
            mGoogleApiClient.disconnect();
        }
    }


    // Handler to handle message based on thread communication
    private class UiHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2: {

                    break;
                }
                case 1: {

                    break;
                }
            }
        }
    }
    /**
     * FindAddress class responsible for find the data using Geocoder
     */
    private class FindAddress implements Runnable{
        PickDropAddress pickDropAddress;
        FindAddress(PickDropAddress pickDropAddress){
            this.pickDropAddress=pickDropAddress;
        }
        @Override
        public void run() {

            mHandler.obtainMessage(0).sendToTarget();
            try {
                List<Address> addresses = mGeocoder.getFromLocation(pickDropAddress.getLatitude(), pickDropAddress.getLongitude(), 1);
                if (addresses != null && addresses.size() > 0) {
                    Address address = addresses.get(0);
                    String country = address.getCountryName();
                    String state = address.getAdminArea();
                    String city = address.getLocality();
                    pickDropAddress.setCity(city);
                    pickDropAddress.setCountry(country);
                    pickDropAddress.setState(state);

               }
                mHandler.obtainMessage(1).sendToTarget();

            } catch (Exception ignore) {
                mHandler.obtainMessage(2).sendToTarget();
            }
        }
    }



}
