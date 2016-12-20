package com.hoffmans.rush.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.hoffmans.rush.R;

/**
 * Created by devesh on 19/12/16.
 */

public class RegisterFragment extends BaseFragment implements View.OnClickListener {

    private EditText edtname,edtEmail,edtphone,edtPassword;
    private Button btnRegister,btnFb,btnGoogle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_register,container,false);
        return view;
    }


    @Override
    protected void initViews(View view) {

        edtname=(EditText)view.findViewById(R.id.frEdtname);
        edtEmail=(EditText)view.findViewById(R.id.frEdtEmail);
        edtphone=(EditText)view.findViewById(R.id.frEdtPhone);
        edtPassword=(EditText)view.findViewById(R.id.frEdtPassword);
        btnRegister=(Button)view.findViewById(R.id.frBtnCreateAccount);
        btnFb=(Button)view.findViewById(R.id.frBtnFacebook);
        btnGoogle=(Button)view.findViewById(R.id.frBtnGoogle);
    }

    @Override
    protected void initListeners() {

        btnRegister.setOnClickListener(this);
        btnFb.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.frBtnCreateAccount:
                break;
            case R.id.frBtnFacebook:
                break;
            case R.id.frBtnGoogle:
                break;
        }
    }
}
