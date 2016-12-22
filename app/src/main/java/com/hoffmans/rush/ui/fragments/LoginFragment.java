package com.hoffmans.rush.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.hoffmans.rush.http.ApiBuilder;
import com.hoffmans.rush.http.BaseListener;
import com.hoffmans.rush.http.Connection;
import com.hoffmans.rush.http.WebserviceType;
import com.hoffmans.rush.ui.activities.CreateAccountActivity;
import com.hoffmans.rush.ui.activities.ForgotPassActivity;
import com.hoffmans.rush.utils.Validation;

import okhttp3.ResponseBody;
import retrofit2.Call;

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
        loginView=inflater.inflate(R.layout.fragment_login,container,false);
        initViews(loginView);
        initListeners();
        return loginView;
    }

    @Override
    protected void initViews(View view) {
        txtCreateAccount=(TextView) view.findViewById(R.id.flCreateAccount);
        txtForgotPassword=(TextView) view.findViewById(R.id.flForgotPass);
        btnLogin=(Button)view.findViewById(R.id.flBtnLogin);
        edtPassword=(EditText)view.findViewById(R.id.flPassword);
        edtEmail=(EditText)view.findViewById(R.id.flUsername);



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
            case R.id.flCreateAccount:
                Intent registerIntent=new Intent(mActivity, CreateAccountActivity.class);
                registerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(registerIntent);
                break;
            case R.id.flForgotPass:
                Intent forgotPassIntent=new Intent(mActivity, ForgotPassActivity.class);
                forgotPassIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(forgotPassIntent);
                break;
            case R.id.flBtnLogin:
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
            mActivity.showSnackbar(getString(R.string.error_empty_email), Toast.LENGTH_SHORT);
            return;

        } else if (!Validation.isValidEmail(email)) {
            mActivity.showSnackbar(getString(R.string.error_title_invalid_email), Toast.LENGTH_SHORT);
            return;

        }

        if (TextUtils.isEmpty(password.trim())) {
            mActivity.showSnackbar(getString(R.string.error_empty_password), Toast.LENGTH_SHORT);
            return;
        }
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !Validation.isValidPassword(password)) {
            mActivity.showSnackbar(getString(R.string.error_title_invalid_password), Toast.LENGTH_SHORT);

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

        Call<ResponseBody> call = ApiBuilder.getPostRequestInstance().getArticleDetail("");
        Connection connection=new Connection(mActivity, call,WebserviceType.ADDPHONENUMBER, new BaseBean(), new BaseListener.OnWebServiceCompleteListener() {
            @Override
            public void onWebServiceComplete(BaseBean baseObject, int statusCode) {

            }

            @Override
            public void onWebServiceError(Exception exception, int statusCode) {

            }

            @Override
            public void onWebStatusFalse(String message) {

            }
        });
    }


    private void register(){

    }
}
