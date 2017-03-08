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
 * Use the {@link UpcomingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpcomingFragment extends BaseFragment {



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


    }

    @Override
    protected void initListeners() {

    }

}
