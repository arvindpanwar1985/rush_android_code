package com.hoffmans.rush.ui.driver.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoffmans.rush.R;
import com.hoffmans.rush.ui.fragments.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompletedRecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompletedRecordFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


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

    }

    @Override
    protected void initListeners() {

    }


}
