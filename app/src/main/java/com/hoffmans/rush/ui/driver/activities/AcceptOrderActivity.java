package com.hoffmans.rush.ui.driver.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hoffmans.rush.R;
import com.hoffmans.rush.bean.BaseBean;
import com.hoffmans.rush.bean.MessageBean;
import com.hoffmans.rush.http.request.ServiceRequest;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.ui.activities.BaseActivity;
import com.hoffmans.rush.utils.AppPreference;
import com.hoffmans.rush.utils.Constants;
import com.hoffmans.rush.utils.Progress;

public class AcceptOrderActivity extends BaseActivity implements View.OnClickListener{

    private  String KEY_MESSAGE      ="message";
    private  String KEY_SERVICE_ID   ="service_id";

    private static final String STATUS_ACCEPTED  = "accepted";
    private static final String STATUS_RUNNING   = "running";
    private static final String STATUS_PENDING   = "pending";

    private TextView mTxtname,mtxtPhone,mtxtSource,mtxtdestination,mtxtPriceEstimate;
    private Button btnAccept,btnReject;
    private String mSeriveId,mMessage;
    private ImageView imgClose;
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
                MessageBean messageBean=(MessageBean)body;
                showSnackbar(messageBean.getMessage(), Toast.LENGTH_LONG);
                if(service_status.equals(STATUS_ACCEPTED)){

                    //TODO move app to upcoming intent

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
}
