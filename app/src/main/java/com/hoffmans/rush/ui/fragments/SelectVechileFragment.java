package com.hoffmans.rush.ui.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.hoffmans.rush.bean.BaseBean;
import com.hoffmans.rush.bean.ServiceBean;
import com.hoffmans.rush.http.request.FavouriteRequest;
import com.hoffmans.rush.http.request.ServiceRequest;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.listners.OnitemClickListner;
import com.hoffmans.rush.location.LocationData;
import com.hoffmans.rush.location.LocationInterface;
import com.hoffmans.rush.model.AddFavouriteBody;
import com.hoffmans.rush.model.CardData;
import com.hoffmans.rush.model.Estimate;
import com.hoffmans.rush.model.EstimateServiceParams;
import com.hoffmans.rush.model.FetchAddressEvent;
import com.hoffmans.rush.model.PickDropAddress;
import com.hoffmans.rush.model.Service;
import com.hoffmans.rush.services.BuildAddressService;
import com.hoffmans.rush.ui.activities.ConfirmServiceActivity;
import com.hoffmans.rush.ui.activities.FavouriteActivity;
import com.hoffmans.rush.ui.adapters.LoadAddressAdapter;
import com.hoffmans.rush.utils.Constants;
import com.hoffmans.rush.utils.DateUtils;
import com.hoffmans.rush.utils.Progress;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_CANCELED;


public class SelectVechileFragment extends BaseFragment implements OnitemClickListner.OnFrequentAddressClicked,View.OnClickListener,GoogleApiClient.OnConnectionFailedListener,LocationInterface ,OnMapReadyCallback{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int  PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final int REQUEST_LOCATION_PERMISSION=2;
    private static final int DESTINATION_SELECTED=1;
    private static final int REQUEST_FAVOURITE=1008;
    private String TAG=SelectVechileFragment.class.getCanonicalName();
    private int clickedAddressPostion;
    private List<PickDropAddress> dropAddressList=new ArrayList<>();
    private String mParam1;
    private String mParam2;
    private ArrayList<PickDropAddress> listAddressData=new ArrayList<>();
    private View view;
    private RecyclerView recyclerView;
    private LoadAddressAdapter addressAdapter;
    private View imageViewLoadMoreAddress;
    private TextView txtNow,txtReservation;
    private LocationData mLocationData;
    private Button btnEstimateCost;
    private ImageView imgTypeCycle,imgTypeBike,imgTypeCar,imgTypeTruck;
    private GoogleMap mGoogleMap;
    private Location mCurrentLocation;
    private boolean isVehicleSelected;
    private DateUtils mDateUtils;
    private String futureDataTime;
    private CircleImageView imageAcceptReject;
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
    public void onStart() {
        super.onStart();
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
        imageViewLoadMoreAddress=(View)view.findViewById(R.id.viewAddMoreAddress);
        txtNow                  =(TextView)view.findViewById(R.id.txtACNow);
        txtReservation          =(TextView)view.findViewById(R.id.txtACReserve);
        btnEstimateCost         =(Button) view.findViewById(R.id.btnEstimatePrice);
        recyclerView.setHasFixedSize(true);
        imgTypeCycle            =(ImageView)view.findViewById(R.id.imgTypeCycle);
        imgTypeBike             =(ImageView)view.findViewById(R.id.imgTypeBike);
        imgTypeCar              =(ImageView)view.findViewById(R.id.imgTypeCar);
        imgTypeTruck            =(ImageView)view.findViewById(R.id.imgTypeTruck);
        imageAcceptReject       =(CircleImageView)view.findViewById(R.id.imgAcceptreject);
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
        btnEstimateCost.setOnClickListener(this);
        imgTypeCar.setOnClickListener(this);
        imgTypeCycle.setOnClickListener(this);
        imgTypeTruck.setOnClickListener(this);
        imgTypeBike.setOnClickListener(this);
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
        }return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        //register event bus
        EventBus.getDefault().register(this);
    }


    @Override
    public void onPause() {
        //Unregister event bus
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
            case R.id.viewAddMoreAddress:
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
            case R.id.btnEstimatePrice:
                validateFields();
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


    /**
     * validate the inputs
     */
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
        if(txtReservation.isSelected() && TextUtils.isEmpty(futureDataTime)){
            //mActivity.showSnackbar("SEle");
            mActivity.showSnackbar("Please select reservation date ",0);
            return;
        }
         estimateService(buildEstimateServiceParams());
    }

    /**
     *
     * @param event from buildAddress intent service
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFetchAddressEvent(FetchAddressEvent event) {
        Progress.dismissProgress();
        if(event.isSucess()) {
               if (clickedAddressPostion == 0) {
                    PickDropAddress pickDropAddress = listAddressData.get(clickedAddressPostion);
                    pickDropAddress.setCountry(event.getCountry());
                    pickDropAddress.setState(event.getState());
                    pickDropAddress.setCity(event.getCity());
                    listAddressData.set(clickedAddressPostion, pickDropAddress);
                } else {
                    PickDropAddress dropAddress = listAddressData.get(clickedAddressPostion);
                    dropAddress.setCountry(event.getCountry());
                    dropAddress.setState(event.getState());
                    dropAddress.setCity(event.getCity());
                    listAddressData.set(clickedAddressPostion, dropAddress);
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
            txtReservation.setSelected(false);
        }

    }

    private void enableReservation(){
        if(txtNow.isSelected()){
            txtReservation.setBackground(ContextCompat.getDrawable(mActivity,R.drawable.bg_now_btn));
            txtReservation.setTextColor(ContextCompat.getColor(mActivity,R.color.colorPrimaryDark));
            txtNow.setBackground(ContextCompat.getDrawable(mActivity,R.drawable.bg_reservation_btn));
            txtNow.setTextColor(ContextCompat.getColor(mActivity,android.R.color.white));
            txtReservation.setSelected(true);
            txtNow.setSelected(false);
        }showDatePicker();
    }


    /**
     *
     * @return the vechile type/return -1 if not selected
     */
    private  int getVechileType(){
        if(imgTypeCar.isSelected()){
            return 3;
        }
        if(imgTypeBike.isSelected()){
            return 2;
        }
        if(imgTypeCycle.isSelected()){
            return 1;
        }
        if(imgTypeTruck.isSelected()){
            return 4;
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
        //Intent intent =new Intent(mActivity, com.hoffmans.rush.ui.activities.PlaceAutocomplete.class);
       // startActivity(intent);
    }

    /**
     * callback for frequent address clicked
     * @param view view clicked
     * @param position postion from the adapter
     */
    @Override
    public void onfrequentAddressclicked(View view, int position) {
        clickedAddressPostion=position;
        Intent favIntent=new Intent(mActivity, FavouriteActivity.class);
        favIntent.putExtra(Constants.KEY_IS_FAVOURITE_SELECTABLE,false);
        startActivityForResult(favIntent,REQUEST_FAVOURITE);
    }

    @Override
    public void onFavoriteAddressclicked(View view, final int position) {
        clickedAddressPostion=position;
        // dialog to add to favourite
        try {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity);
            builder.setTitle(R.string.app_name)
                    .setMessage(R.string.str_fav_message)
                    .setCancelable(false)
                    .setNegativeButton(getString(R.string.str_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton(getString(R.string.str_fav), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            PickDropAddress favouritePickDropAddress= listAddressData.get(position);
                            // Thread and Handler using Geocoder
                            Progress.showprogress(mActivity,getString(R.string.str_marking_fav),false);
                            addToFavourite(favouritePickDropAddress);


                        }
                    }).create().show();
        }catch (Exception e){

        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == mActivity.RESULT_OK) {
                Progress.showprogress(mActivity,"Please wait..",false);
                try {
                    Place place = PlaceAutocomplete.getPlace(mActivity, data);
                    Log.i(TAG, "Place: " + place.getName()+ ""+place.getAddress());
                    setAddress(place, clickedAddressPostion);
                }catch (Exception e){

                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(mActivity, data);
                mActivity.showSnackbar(status.getStatusMessage(),0);
                }
                else if (resultCode == RESULT_CANCELED) {
                }
        }else if(requestCode==REQUEST_FAVOURITE && resultCode==mActivity.RESULT_OK){
            if(data!=null){
                PickDropAddress favouriteSelectedAddress=data.getParcelableExtra("fav_data");
                setFavouriteSelected(favouriteSelectedAddress,clickedAddressPostion);
            }
        }
    }


    private void setFavouriteSelected(PickDropAddress favouriteSelected,int position){
        listAddressData.set(clickedAddressPostion,favouriteSelected);
        addressAdapter.notifyDataSetChanged();
        if(position==DESTINATION_SELECTED){
            btnEstimateCost.setVisibility(View.VISIBLE);
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
            pickDropAddress.setAddress_label(place.getName().toString());
            listAddressData.set(position, pickDropAddress);
            addressAdapter.notifyDataSetChanged();
            if(position==DESTINATION_SELECTED){
                btnEstimateCost.setVisibility(View.VISIBLE);
            }
        //build address using place id .................
       BuildAddressService.buildAddresses(mActivity,place.getId());

    }


    /**
     * show date picker
     */
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
        // set minimum date for future booking Reservation.
        Calendar nextDaycalendar = Calendar.getInstance();
        nextDaycalendar.add(Calendar.DATE, 1);
        dpd.setMinDate(nextDaycalendar);


        dpd.setTitle("Select date");
        dpd.show(mActivity.getFragmentManager(), "Datepickerdialog");
    }


    /**
     * show time picker
     * @param date date selected from date picker
     */
    private void showTimePicker(final String date){
        Calendar now = Calendar.getInstance();
        TimePickerDialog dpd = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

                        Log.e("dateTime",date+" "+hourOfDay+":"+minute+":"+second);
                        String dateTime=date+" "+hourOfDay+":"+minute+":"+second;
                        if(mDateUtils.getUtcDateTime(dateTime)!=null){
                            //set future scheduled date
                            futureDataTime=mDateUtils.getUtcDateTime(dateTime);
                        }
                    }
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                now.get(Calendar.SECOND),true
        );
        dpd.show(mActivity.getFragmentManager(), "Datepickerdialog");
    }


    /**
     * build the params of estimate service
     * @return estimate service param
     */
    private EstimateServiceParams buildEstimateServiceParams(){
        if(listAddressData!=null){
        PickDropAddress pickAddress=listAddressData.get(0);
        dropAddressList.clear();
        for(int i=1;i<listAddressData.size();i++){
            PickDropAddress dropAddress=listAddressData.get(i);
            if(!TextUtils.isEmpty(dropAddress.getStreetAddress())) {
                dropAddressList.add(dropAddress);
            }
        }
        DateUtils dateUtils=DateUtils.getInstance();
        Service service =new Service();
        service.setVehicle_type_id(getVechileType());
        service.setPick_address(pickAddress);
        service.setDrop_addresses(dropAddressList);
        if(txtReservation.isSelected()){
            //future order
            service.setDate(futureDataTime);
            service.setDate_time(futureDataTime);
        }else {
            //current order
            if (dateUtils.getUtcDateTime() != null) {
                Log.e("date", dateUtils.getUtcDateTime());
                service.setDate(dateUtils.getUtcDateTime());
                service.setDate_time(dateUtils.getUtcDateTime());
           }
        }
        EstimateServiceParams estimateParams=new EstimateServiceParams();
        estimateParams.setService(service);
        return  estimateParams;
        }
        return null;
    }


    /**
     * api call for estimating  the order
     * @param estimateServiceParams params sent to server
     */
    private void estimateService(final EstimateServiceParams estimateServiceParams){
        Progress.showprogress(mActivity,getString(R.string.progress_estimate),false);
        String token=appPreference.getUserDetails().getToken();
        ServiceRequest serviceRequest=new ServiceRequest();
        serviceRequest.estimateService(token, estimateServiceParams, new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean body) {
                Progress.dismissProgress();
                try {
                    ServiceBean serviceBean = (ServiceBean) body;
                    Estimate estimate = serviceBean.getEstimate();
                    CardData defaultCardData = serviceBean.getDefault_card();
                    Intent confirmServiceIntent=new Intent(mActivity, ConfirmServiceActivity.class);
                    confirmServiceIntent.putExtra(Constants.KEY_ESTIMATE_DATA,estimate);
                    confirmServiceIntent.putExtra(Constants.KEY_CARD_DATA,defaultCardData);
                    confirmServiceIntent.putExtra(Constants.KEY_TRANSACTION_ID,serviceBean.getTransaction_id());
                    confirmServiceIntent.putExtra(Constants.KEY_PARAM_DATA,estimateServiceParams.getService());
                    startActivity(confirmServiceIntent);
                }catch (NullPointerException e){

                }
            }

            @Override
            public void onRequestFailed(String message) {
                Progress.dismissProgress();
                mActivity.showSnackbar(message,0);
                if(message.equals(Constants.AUTH_ERROR)){
                    mActivity.logOutUser();
                }
            }
        });

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
                    Toast.makeText(mActivity,getString(R.string.str_permission_denied),Toast.LENGTH_LONG).show();
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

    /**
     * api call to add addresses to favourite.
     *
     * @param pickDropAddress PickDropAddress
     */
    private void addToFavourite(final PickDropAddress pickDropAddress){

        AddFavouriteBody favouriteBody=new AddFavouriteBody();
        favouriteBody.setAddress(pickDropAddress);
        String token=appPreference.getUserDetails().getToken();
        FavouriteRequest favouriteRequest=new FavouriteRequest();
        favouriteRequest.addToFavourite(token, favouriteBody, new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean body) {
                Progress.dismissProgress();
                mActivity.showSnackbar(body.getMessage(),0);
                // make changes to pickdropAddress
                pickDropAddress.setFavorite(true);
                listAddressData.set(clickedAddressPostion,pickDropAddress);
                addressAdapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestFailed(String message) {
                mActivity.showSnackbar(message,0);
                Progress.dismissProgress();
                if(message.equals(Constants.AUTH_ERROR)){
                    mActivity.logOutUser();
                }
            }
        });
    }


}
