package com.hoffmans.rush.ui.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.hoffmans.rush.R;
import com.hoffmans.rush.bean.BaseBean;
import com.hoffmans.rush.bean.ConfirmServiceBean;
import com.hoffmans.rush.http.request.ServiceRequest;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.model.CardData;
import com.hoffmans.rush.model.ConfirmService;
import com.hoffmans.rush.model.Estimate;
import com.hoffmans.rush.model.EstimateServiceParams;
import com.hoffmans.rush.model.PickDropAddress;
import com.hoffmans.rush.model.Service;
import com.hoffmans.rush.ui.activities.BookServiceActivity;
import com.hoffmans.rush.ui.activities.CardListActivity;
import com.hoffmans.rush.ui.activities.ConfirmServiceActivity;
import com.hoffmans.rush.ui.activities.ReceiptActivity;
import com.hoffmans.rush.ui.adapters.LoadAddressAdapter;
import com.hoffmans.rush.utils.Constants;
import com.hoffmans.rush.utils.Progress;

import java.util.ArrayList;
import java.util.List;


public class ConfirmServiceFragment extends BaseFragment implements View.OnClickListener ,OnMapReadyCallback{


    private RelativeLayout viewCardDetails;
    private static final int REQUEST_SELECT_CARD=108;
    private ImageView imgCardType;
    private TextView txtCardData,txtCurrency,txtAmount,txtEstimatedTime;
    private int mTransactionId;
    private Estimate mesTimatedData;
    private CardData defaultCardData;
    private Service mServiceParams;
    private RecyclerView recyclerView;
    private Button   btnMakeOrder;
    private LoadAddressAdapter addressAdapter;
    private GoogleMap mGoogleMap;
    private List<PickDropAddress>listAddressData=new ArrayList<>();

    public ConfirmServiceFragment() {
        // Required empty public constructor
    }

    public static ConfirmServiceFragment newInstance(Estimate param1, CardData param2, Service param3,int transID) {
        ConfirmServiceFragment fragment = new ConfirmServiceFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.KEY_ESTIMATE_DATA, param1);
        args.putParcelable(Constants.KEY_CARD_DATA, param2);
        args.putParcelable(Constants.KEY_PARAM_DATA, param3);
        args.putInt(Constants.KEY_TRANSACTION_ID, transID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            mesTimatedData =getArguments().getParcelable(Constants.KEY_ESTIMATE_DATA);
            defaultCardData=getArguments().getParcelable(Constants.KEY_CARD_DATA);
            mServiceParams =getArguments().getParcelable(Constants.KEY_PARAM_DATA);
            mTransactionId =getArguments().getInt(Constants.KEY_TRANSACTION_ID);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_confirm, container, false);
        initViews(view);
        initListeners();
        return view;
    }



    private void onCardSelected(CardData selectedCard){
        defaultCardData=selectedCard;
        if(selectedCard!=null){
            try{
                txtCardData.setText("***********"+defaultCardData.getLast4());
                Glide.with(mActivity).load(defaultCardData.getImageUrl()).into(imgCardType);
            }catch (NullPointerException e){

            }
        }

    }

    @Override
    protected void initViews(View view) {

        recyclerView            =(RecyclerView)view.findViewById(R.id.addressRecycler);
        LinearLayoutManager llm = new LinearLayoutManager(mActivity);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        viewCardDetails=(RelativeLayout)view.findViewById(R.id.viewCardDetails);
        imgCardType=(ImageView)view.findViewById(R.id.imgCardType);
        txtCardData=(TextView)view.findViewById(R.id.txtCardNumber);
        txtCurrency=(TextView)view.findViewById(R.id.txtCurrency);
        txtAmount=(TextView)view.findViewById(R.id.txtAmount);
        txtEstimatedTime=(TextView)view.findViewById(R.id.txtEstimatedTime);
        btnMakeOrder=(Button)view.findViewById(R.id.btnMakeOrder);
        try {
            setEstimatedPrice();
            setEstimatedTime();
            setDefaultCarData();
            setPickDropUi();
        }catch (NullPointerException e){

        }
        checkPermission();
    }

    @Override
    protected void initListeners() {

        viewCardDetails.setOnClickListener(this);
        btnMakeOrder.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.viewCardDetails:
                Intent cardListIntent=new Intent(mActivity, CardListActivity.class);
                cardListIntent.putExtra(Constants.KEY_IS_CARD_SELECTABLE,true);
                startActivityForResult(cardListIntent,REQUEST_SELECT_CARD);

                break;
            case R.id.btnMakeOrder:
                validateDefaultCard();
                break;
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

    /**
     * Validate possible fields before order
     */
    private void validateDefaultCard(){

        if(defaultCardData==null && TextUtils.isEmpty(defaultCardData.getToken())){
            mActivity.showSnackbar(getString(R.string.str_select_card),0);
            return;
        }
        buildApiparams();

    }

    private void buildApiparams(){
        if(mServiceParams!=null){
            EstimateServiceParams estimateServiceParams=new EstimateServiceParams();
            estimateServiceParams.setTransaction_id(mTransactionId);
            if(defaultCardData!=null) {
                estimateServiceParams.setPayment_method_token(defaultCardData.getToken());
                estimateServiceParams.setService(mServiceParams);
                //prompt user for dialog to show actual payment amount and card with which he pays for order
                showPaymentAlert(getPaymentMessage(),estimateServiceParams);
            }else{
                mActivity.showSnackbar(getString(R.string.str_select_card),0);
                return;
            }
        }
    }

    /**
     *
     * @return the message while payment.
     */
    private String getPaymentMessage(){
        StringBuilder stringMessage=new StringBuilder("Are you sure you want to pay ");
        stringMessage.append(mesTimatedData.getSymbol())
                .append(mesTimatedData.getApproxConvertedAmount())
                .append(" ")
                .append("using card ************"+defaultCardData.getLast4())
                .append("?");

        return stringMessage.toString();

    }


    /**
     * set estimated Price
     * @throws NullPointerException
     */
    private void setEstimatedPrice() throws NullPointerException{
        if(mesTimatedData!=null){
            txtCurrency.setText(mesTimatedData.getSymbol());
            if(mesTimatedData.getApproxConvertedAmount()!=null){
                txtAmount.setText(mesTimatedData.getApproxConvertedAmount().toString());
            }
        }
    }

    /**
     * set estimated time
     * @throws NullPointerException
     */
    private void setEstimatedTime() throws NullPointerException{
        if(mesTimatedData!=null){
            txtEstimatedTime.setText(mesTimatedData.getApproxTime());
        }
    }

    /**
     * set default card data
     * @throws NullPointerException
     */
    private void setDefaultCarData()throws NullPointerException{

        if(defaultCardData!=null){
            try{
                txtCardData.setText("***********"+defaultCardData.getLast4());
                Glide.with(mActivity).load(defaultCardData.getImageUrl()).into(imgCardType);
            }catch (NullPointerException e){

            }
        }
    }

    /**
     * set pick and drop UI
     * @throws NullPointerException
     */
   private void setPickDropUi()throws NullPointerException{
        listAddressData.clear();
        listAddressData.add(mServiceParams.getPick_address());
        listAddressData.addAll(mServiceParams.getDrop_addresses());
        if(mServiceParams!=null) {
            addressAdapter = new LoadAddressAdapter(mActivity, listAddressData, null);
            recyclerView.setAdapter(addressAdapter);
        }
    }


    /**
     * api call for confirm order
     * @param estimateServiceParams confirm service params
     */
    private void confirmService(EstimateServiceParams estimateServiceParams){
        Progress.showprogress(mActivity,getString(R.string.progress_loading),false);
        ServiceRequest serviceRequest=new ServiceRequest();
        String token=appPreference.getUserDetails().getToken();
        serviceRequest.confirmService(token, estimateServiceParams, new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean body) {
                Progress.dismissProgress();
                ConfirmServiceBean confirmServiceBean=(ConfirmServiceBean)body;
                if(confirmServiceBean.getService()!=null){
                    ConfirmService service=confirmServiceBean.getService();
                    Intent receiptIntent=new Intent(mActivity, ReceiptActivity.class);
                    receiptIntent.putExtra(Constants.KEY_DATA_DATE_TIME,service.getDate_time());
                    receiptIntent.putExtra(Constants.KEY_PICK_ADDRESS,service.getPick_address().getStreetAddress());
                    receiptIntent.putExtra(Constants.KEY_DATA_TRANSACTION,service.getTransactionDetails());
                    if(service.getDrop_addresses().size()!=0){
                       receiptIntent.putParcelableArrayListExtra(Constants.KEY_DROP_ADDRESS,service.getDrop_addresses());
                    }
                    receiptIntent.putExtra(Constants.KEY_RECEIPT_DATA,service);

                    startActivity(receiptIntent);
                    // finish confirm service
                    mActivity.finish();
                }

            }

            @Override
            public void onRequestFailed(String message) {

                Progress.dismissProgress();
                mActivity.showSnackbar(message,0);
            }
        });

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_SELECT_CARD && resultCode==mActivity.RESULT_OK){
            if(data!=null){
                CardData selectedCard=data.getParcelableExtra(ConfirmServiceActivity.KEY_CARD_DATA);
                onCardSelected(selectedCard);
            }
        }
    }

    /**
     *
     * @param message message on dialog
     * @param confirmServiceParams params for confirm service param.
     */
    private void showPaymentAlert(String message, final  EstimateServiceParams confirmServiceParams){
        try {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity);
            builder.setTitle(getString(R.string.str_confirm_order))
                    .setMessage(message)
                    .setCancelable(false)
                    .setNegativeButton(getString(R.string.str_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                            if(confirmServiceParams!=null){

                            }
                        }
                    })
                    .setPositiveButton(getString(R.string.str_pay_now), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            confirmService(confirmServiceParams);


                        }
                    }).create().show();
        }catch (Exception e){

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case BookServiceActivity.REQUEST_LOCATION_PERMISSION:
                if(mActivity.isPermissionGranted(grantResults)){
                    initMap();

                }else{
                    Toast.makeText(mActivity,getString(R.string.str_permission_denied),Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap=googleMap;
        if(mServiceParams!=null){
            PickDropAddress pickUpAddress=mServiceParams.getPick_address();
            if(pickUpAddress!=null) {
                LatLng latLng = new LatLng(pickUpAddress.getLatitude(), pickUpAddress.getLongitude());
                Marker marker = addlocationMarker(latLng, R.drawable.marker, mGoogleMap, false);
                marker.setTitle("Pick Up");
            }
        }

    }
}
