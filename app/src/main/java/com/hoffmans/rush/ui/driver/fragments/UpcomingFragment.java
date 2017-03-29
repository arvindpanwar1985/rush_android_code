package com.hoffmans.rush.ui.driver.fragments;


import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hoffmans.rush.R;
import com.hoffmans.rush.bean.BaseBean;
import com.hoffmans.rush.bean.MessageBean;
import com.hoffmans.rush.bean.ScheduledBean;
import com.hoffmans.rush.http.request.ServiceRequest;
import com.hoffmans.rush.http.request.UserRequest;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.listners.OnHeaderButtonClickListners;
import com.hoffmans.rush.model.Record;
import com.hoffmans.rush.model.ServiceData;
import com.hoffmans.rush.ui.driver.adapters.UpcomingAdapter;
import com.hoffmans.rush.ui.fragments.BaseFragment;
import com.hoffmans.rush.utils.Constants;
import com.hoffmans.rush.utils.Progress;
import com.hoffmans.rush.utils.Status;
import com.hoffmans.rush.widgets.EndlessRecyclerViewScrollListener;

import java.util.List;

import static com.hoffmans.rush.ui.driver.activities.UpcomingActivity.DEFAULT_ITEMS;
import static com.hoffmans.rush.ui.driver.activities.UpcomingActivity.DEFAULT_PAGE_NO;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpcomingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpcomingFragment extends BaseFragment implements OnHeaderButtonClickListners {


    private RecyclerView mRecyclerView;
    private UpcomingAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private TextView txtNoRecordsFound;
    private ScheduledBean bean;
    private LinearLayout linearProgress;
    private EndlessRecyclerViewScrollListener scrollListener;
    private int records_count,currentListSize;
    private List<Record> recordList;
    private ProgressBar progressBar;


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
        txtNoRecordsFound   =(TextView)view.findViewById(R.id.txtNoRecords);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        linearProgress=(LinearLayout)view.findViewById(R.id.linearLoadMore);
        progressBar =(ProgressBar)view.findViewById(R.id.progressLoadMore);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        getScheduledAndCurrentSercices(DEFAULT_PAGE_NO,DEFAULT_ITEMS,Status.ACCEPTED);
    }

    @Override
    protected void initListeners() {
      scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                if(currentListSize!=records_count){
                    linearProgress.setVisibility(View.VISIBLE);
                     //load more schedule services
                     loadmoreItems(String.valueOf(page+1),DEFAULT_ITEMS,Status.ACCEPTED);
                }
            }
        };
        // Adds the scroll listener to RecyclerView
        mRecyclerView.addOnScrollListener(scrollListener);
    }

    /**
     * get current and schedule services
     * @param page
     * @param perpage
     * @param state
     */
    public void getScheduledAndCurrentSercices(String page,String perpage,String state){
        Progress.showprogress(mActivity,getString(R.string.progress_loading),false);
        String token=appPreference.getUserDetails().getToken();
        ServiceRequest request=new ServiceRequest();
        request.getUpcomingServices(token, page,perpage,state, new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean body) {
                Progress.dismissProgress();
                ScheduledBean bean=(ScheduledBean)body;
                if(bean!=null){
                    records_count=bean.getTotal_items();
                    setScheduledCurrentServicesAdapter(bean);
                }
            }
            @Override
            public void onRequestFailed(String message) {

                Progress.dismissProgress();
                if(message.equals(Constants.AUTH_ERROR)){
                    mActivity.logOutUser();
                }
                mActivity.showSnackbar(message,0);
            }
        });
    }

    /**
     * set upcoming and recent adapter
     * @param bean schedule bean
     */
    private void setScheduledCurrentServicesAdapter(ScheduledBean bean){
        adapter=new UpcomingAdapter(mActivity,this);
        ServiceData currentOrderData=bean.getCurrentOrder();
        if(currentOrderData==null){
            currentOrderData=new ServiceData();
            currentOrderData.setTypenoHeader(true);
        }
        adapter.setHeader(currentOrderData);
        recordList=bean.getScheduledServices();
        adapter.setItems(recordList);
        mRecyclerView.setAdapter(adapter);
        setEmptyListText(recordList);
    }



    @Override
    public void onStartStopButtonClicked(String state, int serviceId) {
         if(state!=null && serviceId!=0){
             String filteredStatus=(state.equals(Status.ACCEPTED))?Status.RUNNING:Status.COMPLETED;
             setServiceStatus(serviceId,filteredStatus);
         }
      }
    /**
     * set the service status
     * @param serviceId id of service
     * @param service_status accepted/running/completed;
     */
    private void setServiceStatus(final int serviceId, final String service_status){
        Progress.showprogress(mActivity,getString(R.string.progress_loading),false);
        String token=appPreference.getUserDetails().getToken();
        ServiceRequest serviceRequest=new ServiceRequest();
        serviceRequest.setServiceStatus(token, String.valueOf(serviceId), service_status, new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean body) {
                Progress.dismissProgress();
                ScheduledBean scheduledBean=(ScheduledBean) body;
                mActivity.showSnackbar(scheduledBean.getMessage(),0);
                if(scheduledBean!=null){
                    if(adapter!=null){
                         ServiceData currentOrderData=scheduledBean.getCurrentOrder();
                          if(currentOrderData==null){
                              currentOrderData=new ServiceData();
                              currentOrderData.setTypenoHeader(true);
                          }
                          adapter.setHeader(currentOrderData);//update header to recyclerView
                          adapter.setItems(scheduledBean.getScheduledServices()); //update listview items
                          adapter.notifyDataSetChanged();
                          setEmptyListText(scheduledBean.getScheduledServices());
                          if(service_status.equals(Status.COMPLETED)){
                             //show comment dialog
                            showCommentDialog(serviceId);
                       }
                   }
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


    /**
     * set the empty textview for list
     * @param recordList lsit of schedule records
     */
    private void setEmptyListText(List<Record>recordList){
        if(recordList!=null && recordList.size()>0){
            txtNoRecordsFound.setVisibility(View.GONE);
        }else{
            txtNoRecordsFound.setVisibility(View.VISIBLE);
        }
    }

    private void showCommentDialog(final int serviceId){

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Please add comment.");
        builder.setCancelable(false);
        // Set up the input
        final EditText input = new EditText(mActivity);

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        input.setHint(getString(R.string.str_add_comment));
        builder.setView(input);
        builder.setNegativeButton(getString(R.string.str_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
               // mActivity.finish();
            }
        });

        builder.setPositiveButton(getString(R.string.str_add_comment),
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //Do nothing here because we override this button later to change the close behaviour.
                        //However, we still need this because on older versions of Android unless we
                        //pass a handler the button doesn't get instantiated
                    }
                });
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String comment=input.getText().toString().trim();
                if(!TextUtils.isEmpty(comment)){
                    //add comments
                    dialog.dismiss();
                    addComment(serviceId,comment);
                }else{
                    input.setError("Please add comment");
                }
            }
        });


    }


    /**
     * laod more schedule services
     * @param page
     * @param perpage
     * @param state
     */
    private  void loadmoreItems(String page,String perpage,String state){
        String token=appPreference.getUserDetails().getToken();
        ServiceRequest request=new ServiceRequest();
        request.getUpcomingServices(token, page,perpage,state, new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean body) {
                linearProgress.setVisibility(View.GONE);
                ScheduledBean bean=(ScheduledBean)body;
                if(bean!=null){
                    linearProgress.setVisibility(View.GONE);
                    List<Record> records=bean.getScheduledServices();
                    if(records!=null && records.size()!=0){
                        records_count=bean.getTotal_items();
                        //update the current schedule listing
                        recordList.addAll(bean.getScheduledServices());
                        currentListSize=recordList.size();
                        if(adapter!=null){
                            adapter.setItems(recordList);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
            @Override
            public void onRequestFailed(String message) {
                linearProgress.setVisibility(View.GONE);
                if(message.equals(Constants.AUTH_ERROR)){
                    mActivity.logOutUser();
                }
            }
        });
    }

    /**
     * add comment to recently completed service
     * @param serviceId
     * @param comment
     */
    private void addComment(int serviceId,String comment){
        Progress.showprogress(mActivity,getString(R.string.progress_loading),false);
        String token=appPreference.getUserDetails().getToken();
        UserRequest request=new UserRequest();
        request.addComment(token, serviceId, comment, new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean body) {
                Progress.dismissProgress();
                MessageBean bean=(MessageBean)body;
                Toast.makeText(mActivity,bean.getMessage(),Toast.LENGTH_SHORT).show();
                //finish current activity
               // mActivity.finish();
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
