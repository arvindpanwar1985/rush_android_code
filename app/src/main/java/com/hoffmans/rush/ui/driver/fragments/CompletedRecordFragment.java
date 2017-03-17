package com.hoffmans.rush.ui.driver.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hoffmans.rush.R;
import com.hoffmans.rush.bean.BaseBean;
import com.hoffmans.rush.bean.RecordBean;
import com.hoffmans.rush.http.request.ServiceRequest;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.model.Record;
import com.hoffmans.rush.ui.fragments.BaseFragment;
import com.hoffmans.rush.utils.Constants;
import com.hoffmans.rush.utils.Progress;
import com.hoffmans.rush.widgets.EndlessRecyclerViewScrollListener;

import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompletedRecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompletedRecordFragment extends BaseFragment {

    private static final String KEY_IS_RECORD      ="isRecord";
    private static final String KEY_PAGE           ="page";
    private static final String KEY_STATE          ="state";
    private static final String KEY_PER_PAGE       ="perpage";

    private static final String DEFAULT_ITEMS      ="5";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EndlessRecyclerViewScrollListener scrollListener;
    private int records_count,currentListSize;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private String mParam1;
    private String mParam2;
    private List<Record> recordList;
    private TextView  txtNorecords;
    private LinearLayout linearProgress;
    private com.hoffmans.rush.ui.driver.adapters.RecordAdapter mAdapter;


    public CompletedRecordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CompletedRecordFragment.
     */

    public static CompletedRecordFragment newInstance(String param1, String param2) {
        CompletedRecordFragment fragment = new CompletedRecordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mActivity.initToolBar(getString(R.string.str_record),true);
        View view= inflater.inflate(R.layout.fragment_completed_record, container, false);
        initViews(view);
        initListeners();
        return view;

    }

    @Override
    protected void initViews(View view) {

        recyclerView=(RecyclerView)view.findViewById(R.id.recordList);
        linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        txtNorecords =(TextView)view.findViewById(R.id.txtNorecords);
        linearProgress=(LinearLayout)view.findViewById(R.id.linearLoadMore);
        getRecordData(buildParams(String.valueOf(1),DEFAULT_ITEMS));
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
    private HashMap<String ,String> buildParams(String page, String perpage){
        HashMap<String,String> params=new HashMap<>();
        params.put(KEY_PAGE,page);
        params.put(KEY_PER_PAGE,perpage);
        params.put(KEY_STATE,Constants.STATUS_COMPLETED);

        return params;
    }

    private void getRecordData(HashMap<String,String> params){


        Progress.showprogress(mActivity,getString(R.string.progress_loading),false);
        ServiceRequest request=new ServiceRequest();
        request.getRecords(appPreference.getUserDetails().getToken(), params, new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean body) {
                Progress.dismissProgress();
                RecordBean recordBean=(RecordBean)body;
                records_count=recordBean.getTotal_items();
                if(records_count!=0 &&recordBean.getRecords().size()!=0){
                    recordList=recordBean.getRecords();
                    mAdapter=new com.hoffmans.rush.ui.driver.adapters.RecordAdapter(mActivity,recordList);
                    recyclerView.setAdapter(mAdapter);
                    currentListSize=recordList.size();

                }else{
                    txtNorecords.setVisibility(View.VISIBLE);
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
