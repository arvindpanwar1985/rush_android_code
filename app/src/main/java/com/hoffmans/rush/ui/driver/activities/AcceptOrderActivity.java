package com.hoffmans.rush.ui.driver.activities;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hoffmans.rush.R;
import com.hoffmans.rush.bean.BaseBean;
import com.hoffmans.rush.bean.ScheduledBean;
import com.hoffmans.rush.bean.ServiceDetailBean;
import com.hoffmans.rush.http.request.ServiceRequest;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.model.CustomerDetail;
import com.hoffmans.rush.model.DateTime;
import com.hoffmans.rush.model.Estimate;
import com.hoffmans.rush.model.PickDropAddress;
import com.hoffmans.rush.model.ServiceData;
import com.hoffmans.rush.ui.activities.BaseActivity;
import com.hoffmans.rush.utils.AppPreference;
import com.hoffmans.rush.utils.Constants;
import com.hoffmans.rush.utils.Progress;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AcceptOrderActivity extends BaseActivity implements View.OnClickListener{

    private  String KEY_MESSAGE      ="message";
    private  String KEY_SERVICE_ID   ="service_id";
    private  String NEW_LINE         ="\n";
    private static final String STATUS_ACCEPTED  = "accepted";
    private static final String STATUS_RUNNING   = "running";
    private static final String STATUS_PENDING   = "pending";

    private TextView mTxtname,mtxtPhone,mtxtSource,mtxtdestination,mtxtPriceEstimate,txtdateTime;
    private Button btnAccept,btnReject;
    private String mSeriveId,mMessage;
    private ImageView imgClose;
    private CircleImageView imgProfile;
    private SpannableStringBuilder mBuilder=new SpannableStringBuilder();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBar("",false);
        hideToolbar();
        getLayoutInflater().inflate(R.layout.activity_accept_order,getParentView());
        initManagers();
        initViews();
        initListeners();
    }

    @Override
    protected void initViews() {

        mTxtname          = (TextView)findViewById(R.id.txtARName);
        mtxtPhone         = (TextView)findViewById(R.id.txtARPhone);
        mtxtSource        = (TextView)findViewById(R.id.txtARSource);
        mtxtdestination   = (TextView)findViewById(R.id.txtARDestination);
        mtxtPriceEstimate = (TextView)findViewById(R.id.txtPriceEstimated);
        btnAccept         = (Button)findViewById(R.id.btnAccept);
        btnReject         = (Button)findViewById(R.id.btnReject);
        imgClose          =(ImageView)findViewById(R.id.imgARClose);
        imgProfile        =(CircleImageView)findViewById(R.id.imgAcceptreject);
        txtdateTime       =(TextView)findViewById(R.id.txtARDatetime);
    }

    @Override
    protected void initListeners() {

        btnAccept.setOnClickListener(this);
        btnReject.setOnClickListener(this);
        imgClose.setOnClickListener(this);
    }

    @Override
    protected void initManagers() {

        mSeriveId=getIntent().getStringExtra(KEY_SERVICE_ID);
        mMessage=getIntent().getStringExtra(KEY_MESSAGE);
        if(!TextUtils.isEmpty(mSeriveId)){
        getServiceDetail(mSeriveId);
        }else{
            finish();
        }

    }

    @Override
    public void onClick(View view) {

        int id =view.getId();
        switch (id){
            case R.id.btnAccept:
                setServiceStatus(mSeriveId,STATUS_ACCEPTED);
                break;
            case R.id.btnReject:
                setServiceStatus(mSeriveId,STATUS_PENDING);
                break;
            case R.id.imgARClose:
                finish();
                break;
        }
    }


    /**
     * get detail of service
     * @param serviceId
     */
    private void getServiceDetail(String serviceId){
        Progress.showprogress(this,getString(R.string.progress_loading),false);
        String url="/api/services/"+serviceId;
        ServiceRequest serviceDetailRequest=new ServiceRequest();
        AppPreference appPreference=AppPreference.newInstance(this);
        serviceDetailRequest.getServiceStatus(appPreference.getUserDetails().getToken(), url, new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean body) {
                Progress.dismissProgress();
                ServiceDetailBean serviceDetailBean=(ServiceDetailBean)body;
                if(serviceDetailBean.getService()!=null){
                    ServiceData serviceData=serviceDetailBean.getService();
                    Estimate estimate                  =serviceData.getEstimate();
                    PickDropAddress pickAddress        =serviceData.getPicAddress();
                    List<PickDropAddress>dropAddresses =serviceData.getDropAddressList();
                    CustomerDetail customerDetail      =serviceData.getCustomerDetail();
                    DateTime dateTime                  =serviceData.getDateTime();
                    //populate the UI
                    setData(estimate,pickAddress,dropAddresses,customerDetail,dateTime);

                }
            }

            @Override
            public void onRequestFailed(String message) {

                Progress.dismissProgress();
                showSnackbar(message,0);
                if(message.equals(Constants.AUTH_ERROR)){
                    logOutUser();
                }
            }
        });
    }
    /**
     * populate the data
     * @param estimate
     * @param pickAddress
     * @param dropAddressList
     */
    private void setData(Estimate estimate, PickDropAddress pickAddress, List<PickDropAddress> dropAddressList, CustomerDetail customerDetail,DateTime dateTime){

        //set customer detail
        if(customerDetail!=null){
            mTxtname.setText("."+customerDetail.getName()+".");
            mtxtPhone.setText(customerDetail.getPhone());
            if(customerDetail.getPicUrl()!=null){
                Glide.with(getApplicationContext()).load(customerDetail.getPicUrl()).into(imgProfile);
            }
        }
        if(dateTime!=null){
            txtdateTime.setText(dateTime.getDate()+" "+dateTime.getTime());
        }
        //set estimate price
        if(estimate!=null){
            mtxtPriceEstimate.setText(estimate.getSymbol()+" "+estimate.getApproxConvertedAmount());
        }
        // set pickup address
        if(pickAddress!=null){
            mBuilder.clear();
            String boldText=getString(R.string.str_collect)+": ";
            getSpanableBuilder(boldText,ContextCompat.getColor(getApplicationContext(),R.color.civ_border));
            String address=pickAddress.getStreetAddress();
            SpannableStringBuilder builder=getSpanableBuilder(address,ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
            mtxtSource.setText(builder, TextView.BufferType.SPANNABLE);
        }
        //set drop address
        if(dropAddressList!=null && dropAddressList.size()>0){
            if(dropAddressList.size()==1){
                //single destination order
                PickDropAddress dropAddress=dropAddressList.get(0);
                mBuilder.clear();
                String start = getString(R.string.str_deliver)+": ";
                getSpanableBuilder(start,ContextCompat.getColor(getApplicationContext(),R.color.civ_border));
                String address=dropAddress.getStreetAddress();
                SpannableStringBuilder builder=getSpanableBuilder(address,ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                mtxtdestination.setText(builder, TextView.BufferType.SPANNABLE);

            }else{
                //multiple destination order
                SpannableStringBuilder builder=null;
                 mBuilder.clear();
                // building multiple destination text
                for(PickDropAddress dropAddress:dropAddressList){
                    String start=getString(R.string.str_deliver)+": ";
                    getSpanableBuilder(start,ContextCompat.getColor(getApplicationContext(),R.color.civ_border));
                    String address=dropAddress.getStreetAddress()+NEW_LINE;
                    builder=getSpanableBuilder(address,ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                }
                mtxtdestination.setText(builder, TextView.BufferType.SPANNABLE);
            }
        }
    }
    /**
     * set the service status
     * @param serviceId id of service
     * @param service_status accepted/running/completed;
     */
    private void setServiceStatus(String serviceId,final String service_status){
        Progress.showprogress(AcceptOrderActivity.this,getString(R.string.progress_loading),false);
        String token= AppPreference.newInstance(AcceptOrderActivity.this).getUserDetails().getToken();
        ServiceRequest serviceRequest=new ServiceRequest();
        serviceRequest.setServiceStatus(token, String.valueOf(serviceId), service_status, new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean body) {
                Progress.dismissProgress();
                ScheduledBean messageBean=(ScheduledBean) body;
                showSnackbar(messageBean.getMessage(), Toast.LENGTH_LONG);
                if(service_status.equals(STATUS_ACCEPTED)){
                    showSnackbar(messageBean.getMessage(),Toast.LENGTH_LONG);
                    finish();
                }else if(service_status.equals(STATUS_PENDING)){
                    //close current activity
                    finish();
                }
            }

            @Override
            public void onRequestFailed(String message) {
                Progress.dismissProgress();
                showSnackbar(message,0);
                if(message.equals(Constants.AUTH_ERROR)){
                    logOutUser();
                }
            }
        });
    }

    /**
     *
     * @param text text
     * @param color color
     * @return SpannableStringBuilder
     */
    private SpannableStringBuilder getSpanableBuilder(String text,int color){
        SpannableString darkSpannable = new SpannableString(text);
        darkSpannable.setSpan(new ForegroundColorSpan(color), 0, text.length(), 0);
        return  mBuilder.append(darkSpannable);
    }
}
