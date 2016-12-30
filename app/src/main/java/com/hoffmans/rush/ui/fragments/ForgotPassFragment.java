package com.hoffmans.rush.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hoffmans.rush.R;
import com.hoffmans.rush.bean.BaseBean;
import com.hoffmans.rush.http.request.UserRequest;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.utils.Utils;
import com.hoffmans.rush.utils.Validation;

/**
 * Created by devesh on 20/12/16.
 */

public class ForgotPassFragment extends BaseFragment implements View.OnClickListener {


    private EditText edtEmail;
    private Button   btnSubmit;
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

        edtEmail=(EditText)view.findViewById(R.id.fPEdtEmail);
        btnSubmit=(Button)view.findViewById(R.id.fPBtnSubmit);
    }

    @Override
    protected void initListeners() {
      btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fPBtnSubmit:
                 validateFields();
                break;
        }
    }

    private  void validateFields(){
        String email =edtEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            mActivity.showSnackbar(getString(R.string.error_empty_email), Toast.LENGTH_SHORT);
            return;

        } else if (!Validation.isValidEmail(email)) {
            mActivity.showSnackbar(getString(R.string.error_title_invalid_email), Toast.LENGTH_SHORT);
            return;

        }
        forgotPassRequest(email);
    }


    private void forgotPassRequest(String email){
        mActivity.showProgress();
        UserRequest request=new UserRequest();
        request.forgotPass(email, new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean body) {
                mActivity.hideProgress();
                Utils.showAlertDialog(mActivity,body.getMessage());

            }

            @Override
            public void onRequestFailed(String message) {
                mActivity.hideProgress();
                mActivity.showSnackbar(message,0);
            }
        });
    }
}
