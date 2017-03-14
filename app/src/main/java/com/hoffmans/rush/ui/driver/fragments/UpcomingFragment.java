package com.hoffmans.rush.ui.driver.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hoffmans.rush.R;
import com.hoffmans.rush.bean.BaseBean;
import com.hoffmans.rush.http.request.ServiceRequest;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.ui.fragments.BaseFragment;
import com.hoffmans.rush.utils.Constants;
import com.hoffmans.rush.utils.Progress;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpcomingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpcomingFragment extends BaseFragment implements View.OnClickListener {

    private static final String STATUS_ACCEPTED  = "accepted";
    private static final String STATUS_RUNNING   = "running";
    private static final String STATUS_COMPLETED = "completed";
    private TextView mTxtname,mtxtPhone,mtxtSource,mtxtdestination,mtxtPriceEstimate;
    private Button btnStart,btnComplete;

    public UpcomingFragment() {
        // Required empty public constructor
    }


    public static UpcomingFragment newInstance() {
        UpcomingFragment fragment = new UpcomingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity.initToolBar("Upcoming",true);
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_upcoming, container, false);
        initViews(view);
        initListeners();
        return view;
    }


    @Override
    protected void initViews(View view) {

        mTxtname          = (TextView)view.findViewById(R.id.txtARName);
        mtxtPhone         = (TextView)view.findViewById(R.id.txtARPhone);
        mtxtSource        = (TextView)view.findViewById(R.id.txtARSource);
        mtxtdestination   = (TextView)view.findViewById(R.id.txtARDestination);
        mtxtPriceEstimate = (TextView)view.findViewById(R.id.txtPriceEstimated);
        btnStart          = (Button)view.findViewById(R.id.btnStart);
        btnComplete       = (Button)view.findViewById(R.id.btnComplete);


    }

    @Override
    protected void initListeners() {

        btnStart.setOnClickListener(this);
        btnComplete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        int id=view.getId();
        switch (id){
            case R.id.btnStart:
                setServiceStatus(1,STATUS_RUNNING);
                break;
            case R.id.btnComplete:
                setServiceStatus(1,STATUS_COMPLETED);
                break;
        }
    }

    /**
     * set the service status
     * @param serviceId id of service
     * @param service_status accepted/running/completed;
     */
    private void setServiceStatus(int serviceId,String service_status){
        Progress.showprogress(mActivity,getString(R.string.progress_loading),false);
        String token=appPreference.getUserDetails().getToken();
        ServiceRequest serviceRequest=new ServiceRequest();
        serviceRequest.setServiceStatus(token, String.valueOf(serviceId), service_status, new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean body) {
                Progress.dismissProgress();
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
}
