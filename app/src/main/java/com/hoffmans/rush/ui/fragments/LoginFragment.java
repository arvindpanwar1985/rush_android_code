package com.hoffmans.rush.ui.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hoffmans.rush.R;
import com.hoffmans.rush.bean.BaseBean;
import com.hoffmans.rush.bean.UserBean;
import com.hoffmans.rush.http.request.LoginRequest;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.model.User;
import com.hoffmans.rush.ui.activities.BookServiceActivity;
import com.hoffmans.rush.ui.activities.ForgotPassActivity;
import com.hoffmans.rush.utils.Utils;
import com.hoffmans.rush.utils.Validation;

/**
 * Created by devesh on 19/12/16.
 */

public class LoginFragment extends BaseFragment implements View.OnClickListener {

    private View loginView;
    private TextView txtCreateAccount,txtForgotPassword;
    private Button btnLogin;
    private EditText edtEmail,edtPassword;
    public LoginFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        loginView=inflater.inflate(com.hoffmans.rush.R.layout.fragment_login,container,false);
        mActivity.initToolBar("",false);
        mActivity.hideToolbar();
        initViews(loginView);
        initListeners();
        return loginView;
    }

    @Override
    protected void initViews(View view) {
        txtCreateAccount=(TextView) view.findViewById(com.hoffmans.rush.R.id.flCreateAccount);
        txtForgotPassword=(TextView) view.findViewById(com.hoffmans.rush.R.id.flForgotPass);
        btnLogin=(Button)view.findViewById(com.hoffmans.rush.R.id.flBtnLogin);
        edtPassword=(EditText)view.findViewById(com.hoffmans.rush.R.id.flPassword);
        edtEmail=(EditText)view.findViewById(com.hoffmans.rush.R.id.flUsername);



    }

    @Override
    protected void initListeners() {
     txtCreateAccount.setOnClickListener(this);
     txtForgotPassword.setOnClickListener(this);
     btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case com.hoffmans.rush.R.id.flCreateAccount:
               // Intent registerIntent=new Intent(mActivity, CreateAccountActivity.class);
                //registerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               // startActivity(registerIntent);
                Fragment fragment=new RegisterFragment();
                replaceFragment(fragment,true);
                break;
            case com.hoffmans.rush.R.id.flForgotPass:
                Intent forgotPassIntent=new Intent(mActivity, ForgotPassActivity.class);
                forgotPassIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(forgotPassIntent);
                break;
            case com.hoffmans.rush.R.id.flBtnLogin:
                validateFields();
                break;
        }
    }

    /**
     * Validate the login page
     */
    private void validateFields(){
        // Store values at the time of the login attempt.
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mActivity.showSnackbar(getString(com.hoffmans.rush.R.string.error_empty_email), Toast.LENGTH_SHORT);
            return;

        } else if (!Validation.isValidEmail(email)) {
            mActivity.showSnackbar(getString(com.hoffmans.rush.R.string.error_title_invalid_email), Toast.LENGTH_SHORT);
            return;

        }

        if (TextUtils.isEmpty(password.trim())) {
            mActivity.showSnackbar(getString(com.hoffmans.rush.R.string.error_empty_password), Toast.LENGTH_SHORT);
            return;
        }
        // Check for a valid password, if the user entered one.
      /*  if (TextUtils.isEmpty(password) || !Validation.isValidPassword(password)) {
            mActivity.showSnackbar(getString(com.hoffmans.rush.R.string.error_title_invalid_password), Toast.LENGTH_SHORT);

            return;
        }*/

        if (TextUtils.isEmpty(password) ) {
            mActivity.showSnackbar(getString(com.hoffmans.rush.R.string.error_title_invalid_password), Toast.LENGTH_SHORT);

            return;
        }
        proceedToLogin(email,password);

    }

    /**
     *
     * @param email email of user
     * @param password password of user
     */
    private void proceedToLogin(String email,String password){

        //Progress.showprogress(mActivity,"Loading..",false);
        mActivity.showProgress();
        LoginRequest loginRequest =new LoginRequest();
        loginRequest.loginUser(email, password, new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean body) {
                //Progress.dismissProgress();
                UserBean bean=(UserBean)body;
                User user=bean.getUser();
                handleLoginResult(user);
                mActivity.hideProgress();
            }

            @Override
            public void onRequestFailed(String message) {
                mActivity.hideProgress();
                //Progress.dismissProgress();
                Utils.showAlertDialog(mActivity,message);
            }
        });

    }

    private void handleLoginResult(User user){

       // PaymentMethodFragment paymentMethodFragment=PaymentMethodFragment.newInstance(user.getBt_token(),"");
        //replaceFragment(paymentMethodFragment,true);
       if(!user.is_email_verified()){
          showAlertDialog(getString(R.string.str_verify_text));
        }else if(!user.is_card_verfied()){
            PaymentMethodFragment paymentMethodFragment=PaymentMethodFragment.newInstance(user);
            replaceFragment(paymentMethodFragment,true);

        }else{
            appPreference.saveUser(user);
            appPreference.setUserLogin(true);
            Intent bookServiceIntent=new Intent(mActivity, BookServiceActivity.class);
            bookServiceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(bookServiceIntent);
        }

    }


    public void showAlertDialog(String message) {
        try {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity);
            builder.setTitle(R.string.app_name)
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    }).create().show();
        }catch (Exception e){

        }
    }


}
