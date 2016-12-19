package com.hoffmans.rush.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hoffmans.rush.R;
import com.hoffmans.rush.ui.activities.CreateAccountActivity;

/**
 * Created by devesh on 19/12/16.
 */

public class LoginFragment extends BaseFragment implements View.OnClickListener {

    private View loginView;
    private TextView txtCreateAccount,txtForgotFassword;
    public LoginFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        loginView=inflater.inflate(R.layout.fragment_login,container,false);
        initViews(loginView);
        initListeners();
        return loginView;
    }

    @Override
    protected void initViews(View view) {
        txtCreateAccount=(TextView) view.findViewById(R.id.flCreateAccount);
        txtForgotFassword=(TextView) view.findViewById(R.id.flForgotPass);
    }

    @Override
    protected void initListeners() {
     txtCreateAccount.setOnClickListener(this);
     txtForgotFassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.flCreateAccount:
                Intent registerIntent=new Intent(mActivity, CreateAccountActivity.class);
                registerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(registerIntent);
                break;
            case R.id.flForgotPass:
               /* Intent forgotPassIntent=new Intent(mActivity, CreateAccountActivity.class);
                registerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(registerIntent);*/
                break;
        }
    }
}
