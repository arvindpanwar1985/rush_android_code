package com.hoffmans.rush.ui.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.hoffmans.rush.R;
import com.hoffmans.rush.bean.BaseBean;
import com.hoffmans.rush.bean.UserBean;
import com.hoffmans.rush.http.request.UserRequest;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.model.User;
import com.hoffmans.rush.utils.Validation;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link UpdateAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateAccountFragment extends BaseFragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";

    private String mEmail,mPhone,token;
    private boolean isEmailVerified;
    private EditText edtEmail,edtPhone;
    private Button btnSave;
    private static  final String KEY_EMAIL="email";
    private static  final String KEY_NAME="name";
    private static  final  String KEY_PHONE="phone";



    public UpdateAccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param email Parameter 1.
     * @param phone Parameter 1.
     * @return A new instance of fragment UpdateAccountFragment.
     */

    public static UpdateAccountFragment newInstance(String email,String phone,String token,boolean isEmailVerified) {
        UpdateAccountFragment fragment = new UpdateAccountFragment();
        Bundle args = new Bundle();
        args.putString( ARG_PARAM1, email);
        args.putString( ARG_PARAM2, phone);
        args.putString( ARG_PARAM3, token);
        args.putBoolean(ARG_PARAM4,isEmailVerified);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mEmail = getArguments().getString(ARG_PARAM1);
            mPhone = getArguments().getString(ARG_PARAM2);
            token  = getArguments().getString(ARG_PARAM3);
            isEmailVerified=getArguments().getBoolean(ARG_PARAM4);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_update_account, container, false);
        mActivity.initToolBar("Update Account",false);
        initViews(view);
        initListeners();
        return view;
    }


    @Override
    protected void initViews(View view) {

        edtEmail=(EditText)view.findViewById(R.id.fuEdtEmail);
        edtPhone=(EditText)view.findViewById(R.id.fuEdtPhone);
        btnSave =(Button)view.findViewById(R.id.fuSaveDetails);

        if(isEmailVerified){
            edtEmail.setText(mEmail);
            edtEmail.setEnabled(false);
        }

    }

    @Override
    protected void initListeners() {

        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fuSaveDetails:
                validateFields();
                break;
        }
    }



    private void validateFields(){
        // Store values at the time of the login attempt.
        String email = edtEmail.getText().toString().trim();
        String phoneNo=edtPhone.getText().toString().trim();
        // Check for a valid email address.


        if (TextUtils.isEmpty(email)) {
            mActivity.showSnackbar(getString(R.string.error_empty_email), Toast.LENGTH_SHORT);
            return;

        } else if (!Validation.isValidEmail(email)) {
            mActivity.showSnackbar(getString(R.string.error_title_invalid_email), Toast.LENGTH_SHORT);
            return;

        }

        if(TextUtils.isEmpty(phoneNo)){
            mActivity.showSnackbar(getString(R.string.error_empty_Mobile), Toast.LENGTH_SHORT);
            return;
        }
        if(!Validation.isValidMobile(phoneNo)){
            mActivity.showSnackbar(getString(R.string.error_title_invalid_Mobile), Toast.LENGTH_SHORT);
            return;
        }

        try {
            JsonObject object = new JsonObject();
            object.addProperty(KEY_EMAIL, email);
            object.addProperty(KEY_PHONE,phoneNo);
            updateUser(object,token);
        }catch (Exception e){

        }

    }

    private  void updateUser(JsonObject object,String token){
        mActivity.showProgress();
        UserRequest userRequest=new UserRequest();
        userRequest.updateUser(object,token, new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean body) {
                mActivity.hideProgress();
                UserBean bean=(UserBean)body;
                User user=bean.getUser();
                if(!user.is_email_verified()){
                    showAlertDialog(getString(R.string.str_verify_text));
                }else if(user.getPhone()!=null && !user.is_card_verfied()){
                    //TODO open payment method fragment
                }else{
                    //login user.
                }
            }

            @Override
            public void onRequestFailed(String message) {
                mActivity.hideProgress();
                mActivity.showSnackbar(message,Toast.LENGTH_LONG);
            }
        });
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
                            mActivity.getSupportFragmentManager().popBackStackImmediate();

                        }
                    }).create().show();
        }catch (Exception e){

        }
    }


}
