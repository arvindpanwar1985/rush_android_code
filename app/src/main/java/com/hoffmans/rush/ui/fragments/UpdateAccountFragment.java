package com.hoffmans.rush.ui.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.hoffmans.rush.R;
import com.hoffmans.rush.bean.BaseBean;
import com.hoffmans.rush.bean.CurrencyBean;
import com.hoffmans.rush.bean.UserBean;
import com.hoffmans.rush.http.request.AppCurrencyRequest;
import com.hoffmans.rush.http.request.UserRequest;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.model.Currency;
import com.hoffmans.rush.model.User;
import com.hoffmans.rush.ui.activities.BookServiceActivity;
import com.hoffmans.rush.utils.Constants;
import com.hoffmans.rush.utils.Progress;
import com.hoffmans.rush.utils.Validation;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link UpdateAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateAccountFragment extends BaseFragment implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";

    private String mEmail,mPhone,token;

    private EditText edtEmail,edtPhone,edtcc;
    private Button btnSave;
    private static  final String KEY_EMAIL="email";
    private static  final String KEY_PHONE="phone";
    private static  final String KEY_CURRENCY ="currency_symbol_id";


    private Spinner spinnerCurrency;
    private Currency selectedCurrency;
    private List<Currency> currencyList =new ArrayList<>();



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

    public static UpdateAccountFragment newInstance(String email,String phone,String token) {
        UpdateAccountFragment fragment = new UpdateAccountFragment();
        Bundle args = new Bundle();
        args.putString( ARG_PARAM1, email);
        args.putString( ARG_PARAM2, phone);
        args.putString( ARG_PARAM3, token);

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


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_update_account, container, false);
        mActivity.initToolBar("",false);
        mActivity.hideToolbar();
        initViews(view);
        initListeners();
        getAllCurrency();
        return view;
    }


    @Override
    protected void initViews(View view) {

        edtEmail=(EditText)view.findViewById(R.id.fuEdtEmail);
        edtPhone=(EditText)view.findViewById(R.id.fuEdtPhone);
        edtcc=(EditText)view.findViewById(R.id.fuEdtCC);
        btnSave =(Button)view.findViewById(R.id.fuSaveDetails);
        spinnerCurrency=(Spinner)view.findViewById(R.id.spinnerCurrency);
        if(mEmail!=null &&!TextUtils.isEmpty(mEmail)){
            edtEmail.setText(mEmail);
           //user will not able to change the email
           edtEmail.setEnabled(false);
        }else{
            edtEmail.setEnabled(true);
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
        String number=edtPhone.getText().toString().trim();
        String cc=edtcc.getText().toString().trim();
        String phoneNo=cc+number;
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

        if(selectedCurrency==null){
            //mActivity.showSnackbar(getString(R.string.str_select_currency), Toast.LENGTH_SHORT);
            Snackbar.make(getView(),"Currency data not found!",Snackbar.LENGTH_LONG)
                    .setAction("Try again", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getAllCurrency();
                        }
                    }).show();
            return;
        }
        try {
            JsonObject object = new JsonObject();
            object.addProperty(KEY_EMAIL, email);
            object.addProperty(KEY_PHONE,phoneNo);
            object.addProperty(KEY_CURRENCY,selectedCurrency.getId().toString());
            updateUser(object,token);
        }catch (Exception e){

        }

    }

    private  void updateUser(JsonObject object, final String token){
        Progress.showprogress(mActivity,"Updating Account..",false);
        UserRequest userRequest=new UserRequest();
        userRequest.updateUser(object,token, new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean body) {
                Progress.dismissProgress();
                UserBean bean=(UserBean)body;
                User user=bean.getUser();
                user.setToken(token);
                if(!user.is_email_verified()){
                    showAlertDialog(getString(R.string.str_verify_text));
                }else if(user.getPhone()!=null && !user.is_card_verfied()){
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

            @Override
            public void onRequestFailed(String message) {
                Progress.dismissProgress();
                if(message.equals(Constants.AUTH_ERROR)){
                    mActivity.logOutUser();
                }
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
                           // mActivity.finish();

                        }
                    }).create().show();
        }catch (Exception e){

        }
    }


    private void getAllCurrency(){

        Progress.showprogress(mActivity,getString(R.string.progress_loading),false);
        AppCurrencyRequest appCurrencyRequest=new AppCurrencyRequest();
        appCurrencyRequest.getCurrency(new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean body) {

                Progress.dismissProgress();
                CurrencyBean currencyBean=(CurrencyBean)body;
                currencyList.clear();
                currencyList =currencyBean.getCurrencies();
                List<String> currencyListString =new ArrayList<String>();
                currencyListString.add(getString(R.string.str_select_currency));
                if(currencyList.size()!=0){
                    for(Currency currency:currencyList){
                        currencyListString.add(currency.getCurrencyName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(mActivity, R.layout.texview_spinner,currencyListString);
                    spinnerCurrency.setAdapter(adapter);
                    spinnerCurrency.setSelection(0,false);
                    setSpinnerListner();
                }
            }

            @Override
            public void onRequestFailed(String message) {
                mActivity.showSnackbar(message,0);
                Progress.dismissProgress();
                if(message.equals(Constants.AUTH_ERROR)){
                    mActivity.logOutUser();
                }
            }
        });
    }
    private  void setSpinnerListner(){
        spinnerCurrency.setOnItemSelectedListener(this);
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int selectedPosition, long l) {
        Log.e("pos",selectedPosition+"");
        if(currencyList!=null && selectedPosition!=0) {
            selectedPosition=selectedPosition-1;
            selectedCurrency = currencyList.get(selectedPosition);
        }else{
            selectedCurrency=null;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
