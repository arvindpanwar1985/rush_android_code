package com.hoffmans.rush.ui.driver.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hoffmans.rush.R;
import com.hoffmans.rush.bean.BaseBean;
import com.hoffmans.rush.bean.ScheduledBean;
import com.hoffmans.rush.http.request.ServiceRequest;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.model.Record;
import com.hoffmans.rush.ui.driver.adapters.UpcomingAdapter;
import com.hoffmans.rush.ui.fragments.BaseFragment;
import com.hoffmans.rush.utils.Constants;
import com.hoffmans.rush.utils.Progress;

import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpcomingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpcomingFragment extends BaseFragment implements View.OnClickListener {

    private static final String STATUS_ACCEPTED  = "accepted";
    private static final String STATUS_RUNNING   = "running";
    private static final String STATUS_COMPLETED = "completed";
    private static final String KEY_PAGE           ="page";
    private static final String KEY_STATE          ="state";
    private static final String KEY_PER_PAGE       ="perpage";
    private TextView mTxtname,mtxtPhone,mtxtSource,mtxtdestination,mtxtPriceEstimate;
    private Button btnStart,btnComplete;
    private RecyclerView mRecyclerView;
    private UpcomingAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

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
       // showCommentDialog();
        return view;
    }


    @Override
    protected void initViews(View view) {
        mRecyclerView     =(RecyclerView)view.findViewById(R.id.currentScheduleOrderList);
        linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
      /*mTxtname          = (TextView)view.findViewById(R.id.txtARName);
        mtxtPhone         = (TextView)view.findViewById(R.id.txtARPhone);
        mtxtSource        = (TextView)view.findViewById(R.id.txtARSource);
        mtxtdestination   = (TextView)view.findViewById(R.id.txtARDestination);
        mtxtPriceEstimate = (TextView)view.findViewById(R.id.txtPriceEstimated);
        btnStart          = (Button)view.findViewById(R.id.btnStart);
        btnComplete       = (Button)view.findViewById(R.id.btnComplete);*/


       getScheduledAndCurrentSercices("1","5",STATUS_ACCEPTED);
    }

    @Override
    protected void initListeners() {
        //btnStart.setOnClickListener(this);
        //btnComplete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        /*int id=view.getId();
        switch (id){
            case R.id.btnStart:
                setServiceStatus(1,STATUS_RUNNING);
                break;
            case R.id.btnComplete:
                setServiceStatus(1,STATUS_COMPLETED);
                break;
        }*/
    }


    /**
     *
     * @param page page number to laod
     * @param perpage items per page
     * @return Hashmap params for api call
     */
    private HashMap<String ,String> buildParams(String page, String perpage){
        HashMap<String,String> params=new HashMap<>();
        params.put(KEY_PAGE,page);
        params.put(KEY_PER_PAGE,perpage);
        params.put(KEY_STATE,STATUS_COMPLETED);

        return params;
    }



    private void getScheduledAndCurrentSercices(String page,String perpage,String state){

        Progress.showprogress(mActivity,getString(R.string.progress_loading),false);
        String token=appPreference.getUserDetails().getToken();
        ServiceRequest request=new ServiceRequest();
        request.getUpcomingServices(token, page,perpage,state, new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean body) {
                Progress.dismissProgress();
                ScheduledBean bean=(ScheduledBean)body;
                if(bean!=null){
                    adapter=new UpcomingAdapter(mActivity);
                    adapter.setHeader(bean.getCurrentOrder());
                    List<Record> recordList=bean.getScheduledServices();
                    adapter.setItems(recordList);
                    mRecyclerView.setAdapter(adapter);
                }

            }

            @Override
            public void onRequestFailed(String message) {

                Progress.dismissProgress();
            }
        });
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


    private void showCommentDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Please add comment.");
        // Set up the input
        final EditText input = new EditText(mActivity);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        input.setHint("Add comment.");
        builder.setView(input);
        builder.setPositiveButton("Add Comment", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }



}
