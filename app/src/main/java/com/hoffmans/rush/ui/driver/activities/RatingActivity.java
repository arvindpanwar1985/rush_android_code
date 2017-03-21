package com.hoffmans.rush.ui.driver.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hoffmans.rush.R;
import com.hoffmans.rush.bean.BaseBean;
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

/**
 * Created by devesh on 21/3/17.
 */

public class RatingActivity extends BaseActivity implements View.OnClickListener{

    private RelativeLayout topRelative;
    private  String KEY_MESSAGE      ="message";
    private  String KEY_SERVICE_ID   ="service_id";
    private ImageView imgClose;
    private String mSeriveId,mMessage;
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

        imgClose   =(ImageView)findViewById(R.id.imgARClose);
        topRelative=(RelativeLayout)findViewById(R.id.topRelative);
    }

    @Override
    protected void initListeners() {

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
                    Estimate estimate                  =serviceData.getEstimate();
                    PickDropAddress pickAddress        =serviceData.getPicAddress();
                    List<PickDropAddress> dropAddresses =serviceData.getDropAddressList();
                    CustomerDetail customerDetail      =serviceData.getCustomerDetail();
                    DateTime dateTime                  =serviceData.getDateTime();


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
}
