package com.hoffmans.rush.ui.driver.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hoffmans.rush.bean.BaseBean;
import com.hoffmans.rush.bean.UserBean;
import com.hoffmans.rush.http.request.LoginRequest;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.model.User;
import com.hoffmans.rush.ui.activities.ForgotPassActivity;
import com.hoffmans.rush.ui.driver.activities.DriverNavigationActivity;
import com.hoffmans.rush.ui.fragments.BaseFragment;
import com.hoffmans.rush.utils.Utils;
import com.hoffmans.rush.utils.Validation;

/**
 * A simple {@link BaseFragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener {

    private String TAG=LoginFragment.class.getCanonicalName();
    private TextView txtForgotPassword;
    private Button btnLogin;
    private EditText edtEmail,edtPassword;
    public static final String ROLE_CUST   ="Customer";
    public static final String ROLE_DRIVER ="Driver";
    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mActivity.initToolBar("",true,false);
        View view= inflater.inflate(com.hoffmans.rush.R.layout.fragment_login_driver, container, false);
        initViews(view);
        initListeners();


        return view;
    }



    @Override
    protected void initViews(View view) {
        txtForgotPassword=(TextView) view.findViewById(com.hoffmans.rush.R.id.flForgotPass);
        btnLogin=(Button)view.findViewById(com.hoffmans.rush.R.id.flBtnLogin);
        edtPassword=(EditText)view.findViewById(com.hoffmans.rush.R.id.flPassword);
        edtEmail=(EditText)view.findViewById(com.hoffmans.rush.R.id.flUsername);
    }

    @Override
    protected void initListeners() {
        btnLogin.setOnClickListener(this);
        txtForgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case com.hoffmans.rush.R.id.flBtnLogin:
                validateFields();
                break;
            case com.hoffmans.rush.R.id.flForgotPass:
                Intent forgotPassIntent=new Intent(mActivity, ForgotPassActivity.class);
                forgotPassIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(forgotPassIntent);
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
                mActivity.hideProgress();
                UserBean bean=(UserBean)body;
                User user=bean.getUser();
                if(user!=null && user.getRole()!=null){
                    if(user.getRole().equals(ROLE_DRIVER)){
                        handleLoginResult(user);
                    }else{
                        mActivity.showSnackbar("Invalid Credentials",0);
                    }
                }
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
            appPreference.saveUser(user);
            appPreference.setUserLogin(true);
            Intent driverMainIntent=new Intent(mActivity, DriverNavigationActivity.class);
            driverMainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(driverMainIntent);
      }

}
