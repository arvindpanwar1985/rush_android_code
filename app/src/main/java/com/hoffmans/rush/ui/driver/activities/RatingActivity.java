package com.hoffmans.rush.ui.driver.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hoffmans.rush.R;
import com.hoffmans.rush.bean.BaseBean;
import com.hoffmans.rush.bean.ServiceDetailBean;
import com.hoffmans.rush.http.request.ServiceRequest;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.model.DriverDetail;
import com.hoffmans.rush.model.Rating;
import com.hoffmans.rush.model.RatingParam;
import com.hoffmans.rush.model.ServiceData;
import com.hoffmans.rush.ui.activities.BaseActivity;
import com.hoffmans.rush.utils.AppPreference;
import com.hoffmans.rush.utils.Constants;
import com.hoffmans.rush.utils.Progress;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by devesh on 21/3/17.
 */

public class RatingActivity extends BaseActivity implements View.OnClickListener{

    private RelativeLayout topRelative;
    private  String KEY_MESSAGE      ="message";
    private  String KEY_SERVICE_ID   ="service_id";
    private ImageView imgClose;
    private RatingBar ratingBar;
    private CircleImageView imgProfile;
    private TextView mTxtname,mtxtPhone;
    private String mSeriveId,mMessage;
    private Button btnSubmitReview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolBar("",false);
        hideToolbar();
        getLayoutInflater().inflate(R.layout.activity_rating,getParentView());
        initManagers();
        initViews();
        initListeners();
    }

    @Override
    protected void initViews() {

        imgClose   =(ImageView)findViewById(R.id.imgARClose);
        topRelative=(RelativeLayout)findViewById(R.id.topRelative);
        mtxtPhone  =(TextView)findViewById(R.id.txtRName);
        mTxtname   =(TextView)findViewById(R.id.txtRPhone);
        ratingBar  =(RatingBar)findViewById(R.id.ratingBAr);
        btnSubmitReview=(Button)findViewById(R.id.btnsubmitRating);
        imgProfile  =(CircleImageView)findViewById(R.id.imgAcceptreject);
    }

    @Override
    protected void initListeners() {

        btnSubmitReview.setOnClickListener(this);
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
              case R.id.imgARClose:
                finish();
                break;
            case R.id.btnsubmitRating:
                 rateDriver(buildRatingParam());
                 break;
        }
    }


    /**
     * build the rating params
     * @return Rating params
     */
     private RatingParam buildRatingParam(){
        RatingParam ratingParam=new RatingParam();
        Rating rating=new Rating();
        rating.setRating(String.valueOf(ratingBar.getRating()));
        rating.setServiceId(mSeriveId);
        rating.setServiceReview("");
        ratingParam.setRating(rating);
        return ratingParam;
    }

    private void setData(DriverDetail driverDetail){
        mtxtPhone.setText(driverDetail.getPhone());
        mTxtname .setText(driverDetail.getName());
        if(driverDetail.getPicUrl()!=null){
            Glide.with(RatingActivity.this).load(driverDetail.getPicUrl()).into(imgProfile);
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
                topRelative.setVisibility(View.VISIBLE);
                ServiceDetailBean serviceDetailBean=(ServiceDetailBean)body;
                if(serviceDetailBean.getService()!=null){
                    ServiceData serviceData=serviceDetailBean.getService();
                    /*Estimate estimate                  =serviceData.getEstimate();
                    PickDropAddress pickAddress        =serviceData.getPicAddress();
                    List<PickDropAddress> dropAddresses =serviceData.getDropAddressList();
                    DateTime dateTime                  =serviceData.getDateTime();*/
                    DriverDetail driverDetail=serviceData.getDriverDetail();
                    if(driverDetail!=null){
                        setData(driverDetail);
                    }
                }
            }
            @Override
            public void onRequestFailed(String message) {
                topRelative.setVisibility(View.GONE);
                Progress.dismissProgress();
                showSnackbar(message,0);
                if(message.equals(Constants.AUTH_ERROR)){
                    logOutUser();
                }
            }
        });
    }
    /**
     * rate driver
     * @param param
     */
    private void rateDriver(RatingParam param){
        Progress.showprogress(this,getString(R.string.progress_loading),false);
        String token=AppPreference.newInstance(this).getUserDetails().getToken();
        ServiceRequest request=new ServiceRequest();
        request.setServiceRating(token, param, new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean body) {
                Progress.dismissProgress();
                Toast.makeText(getApplicationContext(),body.getMessage(),Toast.LENGTH_LONG).show();
                finish();
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
}
