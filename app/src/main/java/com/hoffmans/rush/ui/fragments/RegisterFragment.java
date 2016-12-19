package com.hoffmans.rush.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoffmans.rush.R;

/**
 * Created by devesh on 19/12/16.
 */

public class RegisterFragment extends BaseFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_register,container,false);
        return view;
    }

    @Override
    protected void initViews(View view) {

    }

    @Override
    protected void initListeners() {

    }
}
