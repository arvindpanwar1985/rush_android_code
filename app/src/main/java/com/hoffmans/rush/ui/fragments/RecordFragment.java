package com.hoffmans.rush.ui.fragments;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hoffmans.rush.R;
import com.hoffmans.rush.bean.BaseBean;
import com.hoffmans.rush.bean.RecordBean;
import com.hoffmans.rush.http.request.ServiceRequest;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.model.Record;
import com.hoffmans.rush.ui.adapters.RecordAdapter;
import com.hoffmans.rush.utils.Constants;
import com.hoffmans.rush.utils.Progress;
import com.hoffmans.rush.widgets.EndlessRecyclerViewScrollListener;

import java.util.HashMap;
import java.util.List;


public class RecordFragment extends BaseFragment {
   private static final String KEY_IS_RECORD      ="isRecord";
   private static final String KEY_PAGE           ="page";
   private static final String KEY_STATE          ="state";
   private static final String KEY_PER_PAGE       ="perpage";
   private static final String STATE_PENDING      ="pending";
   private static final String STATE_COMPLETED    ="completed";
   private static final String DEFAULT_ITEMS      ="5";
   private boolean isRecord;
   private RecyclerView recyclerView;
   private LinearLayout linearProgress;
   private RecordAdapter mAdapter;
   private EndlessRecyclerViewScrollListener scrollListener;
   private LinearLayoutManager linearLayoutManager;
   private int records_count,currentListSize;
   private List<Record> recordList;
   private ProgressBar progressBar;
   private TextView txtNoRecords;
    public RecordFragment() {
        // Required empty public constructor
    }

    public static RecordFragment newInstance(boolean isRecord) {
        RecordFragment fragment = new RecordFragment();
        Bundle args = new Bundle();
        args.putBoolean(KEY_IS_RECORD,isRecord);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            isRecord=getArguments().getBoolean(KEY_IS_RECORD);
            if(isRecord){
                mActivity.initToolBar(getString(R.string.str_record),true,true);
            }else{
                mActivity.initToolBar(getString(R.string.str_scheduled),true,true);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_record, container, false);
        initViews(view);
        initListeners();
        getRecordData(buildParams(String.valueOf(1),DEFAULT_ITEMS));
        return view;
    }

    @Override
    protected void initViews(View view) {
        recyclerView=(RecyclerView)view.findViewById(R.id.listRecords);
        linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        linearProgress=(LinearLayout)view.findViewById(R.id.linearLoadMore);
        progressBar =(ProgressBar)view.findViewById(R.id.progressLoadMore);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        txtNoRecords=(TextView)view.findViewById(R.id.txtNoRecords);
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
                    loadmoreItems(buildParams(String.valueOf(page+1),DEFAULT_ITEMS));
                }
            }
        };
        // Adds the scroll listener to RecyclerView
        recyclerView.addOnScrollListener(scrollListener);
    }


    /**
     *
     * @param page page number to laod
     * @param perpage items per page
     * @return Hashmap params for api call
     */
    private HashMap<String ,String> buildParams(String page,String perpage){
        HashMap<String,String> params=new HashMap<>();
        params.put(KEY_PAGE,page);
        params.put(KEY_PER_PAGE,perpage);
        params.put(KEY_STATE,STATE_COMPLETED);
        if(!isRecord){
            params.put(KEY_STATE,STATE_PENDING);
        }
        return params;
    }

    private void getRecordData(HashMap<String,String> params){
        Progress.showprogress(mActivity,getString(R.string.progress_loading),false);
        txtNoRecords.setVisibility(View.GONE);
        ServiceRequest request=new ServiceRequest();
        request.getRecords(appPreference.getUserDetails().getToken(), params, new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean body) {
                 Progress.dismissProgress();
                 RecordBean recordBean=(RecordBean)body;
                 records_count=recordBean.getTotal_items();
                 if(records_count!=0 &&recordBean.getRecords().size()!=0){
                    recordList=recordBean.getRecords();
                    mAdapter=new RecordAdapter(mActivity,recordList,isRecord);
                    recyclerView.setAdapter(mAdapter);
                    currentListSize=recordList.size();

                  }else{
                     txtNoRecords.setVisibility(View.VISIBLE);
                 }
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

    /**
     * load more data on endless scrolling
     * @param params
     */
    private  void loadmoreItems(HashMap<String,String> params){
        ServiceRequest request=new ServiceRequest();
        request.getRecords(appPreference.getUserDetails().getToken(), params, new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean body) {
                linearProgress.setVisibility(View.GONE);
                RecordBean recordBean=(RecordBean)body;
                records_count=recordBean.getTotal_items();
                if(recordBean.getRecords().size()!=0){
                        recordList.addAll(recordBean.getRecords());
                        currentListSize=recordList.size();
                        if(mAdapter!=null){
                            mAdapter.notifyDataSetChanged();
                        }
                  }
            }

            @Override
            public void onRequestFailed(String message) {
                 mActivity.showSnackbar(message,0);
                 linearProgress.setVisibility(View.GONE);
                 Progress.dismissProgress();
                 if(message.equals(Constants.AUTH_ERROR)){
                    mActivity.logOutUser();
                 }
            }
        });
    }

}
