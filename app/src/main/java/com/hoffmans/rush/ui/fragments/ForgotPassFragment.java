package com.hoffmans.rush.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoffmans.rush.R;

/**
 * Created by devesh on 20/12/16.
 */

public class ForgotPassFragment extends BaseFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View View=inflater.inflate(R.layout.fragment_forgotpass,container,false);
        initViews(View);
        initListeners();
        return View;
    }

    @Override
    protected void initViews(View view) {

    }

    @Override
    protected void initListeners() {

    }
}
