package com.hoffmans.rush.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.hoffmans.rush.R;
import com.hoffmans.rush.bean.BaseBean;
import com.hoffmans.rush.http.request.PaymentRequest;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.model.Card;
import com.hoffmans.rush.model.User;
import com.hoffmans.rush.ui.activities.BookServiceActivity;
import com.hoffmans.rush.utils.AppPreference;
import com.hoffmans.rush.utils.Constants;
import com.hoffmans.rush.utils.Progress;
import com.hoffmans.rush.utils.Utils;
import com.hoffmans.rush.utils.Validation;
import com.hoffmans.rush.widgets.MonthYearPicker;

/**
 * A simple {@link BaseFragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * to handle interaction events.
 * Use the {@link PaymentMethodFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaymentMethodFragment extends BaseFragment implements View.OnClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String CARD_HOLDER_NAME="cardholder_name";
    private static final String CARD_NUMBER="card_number";
    private static final String CARD_TYPE="card_type";
    private static final String EXPIRATION_MONTH="expiration_month";
    private static final String EXPIRATION_YEAR="expiration_year";
    private static final String CVV="cvv";
    private User user;
    private EditText edtCardNumber,edtEdtTitular,edtCvv;
    private TextView txtExpiry;
    private Button btnSaveCard;
    private LinearLayout topLinear;



    public PaymentMethodFragment() {
        // Required empty public constructor
    }


    public static PaymentMethodFragment newInstance(User user) {
        PaymentMethodFragment fragment = new PaymentMethodFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // set the listner for nounce generated and error from braintree

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = getArguments().getParcelable(ARG_PARAM1);
        }
        if(appPreference==null){
            appPreference= AppPreference.newInstance(mActivity);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View paymentMethodView=inflater.inflate(R.layout.fragment_payment_methods,container,false);
        mActivity.initToolBar("",false);
        mActivity.hideToolbar();
        initViews(paymentMethodView);
        initListeners();
        Utils.showAlertDialog(mActivity,"Please link a card to your account.");
        return paymentMethodView;
    }


    @Override
    protected void initViews(View view) {

        edtCardNumber=(EditText)view.findViewById(R.id.fpEdtCard);
        edtEdtTitular=(EditText)view.findViewById(R.id.fpEdtTitular);
        txtExpiry    =(TextView) view.findViewById(R.id.fpEdtExpiry);
        edtCvv       =(EditText)view.findViewById(R.id.fpEdtCvv);
        btnSaveCard  =(Button) view.findViewById(R.id.fpBtnSaveCard);
        topLinear    =(LinearLayout)view.findViewById(R.id.topLinearPayment);

    }

    @Override
    protected void initListeners() {
        edtCardNumber.setOnClickListener(this);
        txtExpiry.setOnClickListener(this);
        edtCvv.setOnClickListener(this);
        edtEdtTitular.setOnClickListener(this);
        btnSaveCard.setOnClickListener(this);
        topLinear.setOnClickListener(this);

    }





    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.fpBtnSaveCard:
                validateCardDetails();
                break;
            case R.id.fpEdtExpiry:
                final MonthYearPicker monthYearPicker=new MonthYearPicker(mActivity);
                monthYearPicker.build(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(monthYearPicker!=null)
                            txtExpiry.setText(monthYearPicker.getSelectedMonth()+1+"/"+monthYearPicker.getSelectedYear());
                    }
                },null);
                monthYearPicker.show();
                break;
            case R.id.topLinearPayment:
                 Utils.hideKeyboard(mActivity);
                 break;
        }
    }


    /**
     *
     * @return the user input card
     */
    private Card getCreditCard(){
        Card card=new Card();
        String cardnumber=edtCardNumber.getText().toString().trim();
        String expiry    =txtExpiry.getText().toString().trim();
        String headLine  =edtEdtTitular.getText().toString().trim();
        String cvv       =edtCvv.getText().toString().trim();

        String cardType=Utils.getCreditCardTypeByNumber(cardnumber);

        String expirationMonth="", expirationYear ="";
        // split date into month and year
        if (!TextUtils.isEmpty(expiry)) {
            String[] dateSplit = expiry.split("/");
            expirationMonth = dateSplit[0];
            expirationYear = dateSplit[1];
        }

        card.setCardNumber(cardnumber);
        card.setCardCvv(cvv);
        card.setCardHeadline(headLine);
        card.setCardExpiry(expiry);
        card.setCardExpirationMonth(expirationMonth);
        card.setCardExpirationYear(expirationYear);
        card.setCardType(cardType);

        return card;

    }



    private void validateCardDetails(){
       Card card=getCreditCard();
        if(card!=null){
            if (!Validation.isValidCreditCard(card.getCardNumber())) {
                //Utils.showToast(mActivity, "Invalid Card Number");
                mActivity.showSnackbar(getString(R.string.error_title_invalid_card),0);
                return;
            }
            if (card.getCardHeadline().length()==0) {
                //mUtils.showToast(mActivity, "Please select Month");
                mActivity.showSnackbar(getString(R.string.error_invalid_card_holdername), 0);
                return;
            }
            if (card.getCardExpiry().length()==0) {
                //mUtils.showToast(mActivity, "Please select Month");
                mActivity.showSnackbar(getString(R.string.str_empty_expiry), 0);
                return;
            }


           /* if (card.getCardCvv().length()<3 || card.getCardCvv().length()>4) {
                // Utils.showToast(mActivity, "Invalid Cvv.");
                mActivity.showSnackbar(getString(R.string.str_invalid_cvv), 0);
                return;
            }*/

            if(card.getCardNumber().length()==15 && card.getCardCvv().length()!=4){
                mActivity.showSnackbar(getString(R.string.str_invalid_cvv), 0);
                return;
            }

            if(card.getCardNumber().length()==16 && card.getCardCvv().length()!=3){
                mActivity.showSnackbar(getString(R.string.str_invalid_cvv), 0);
                return;
            }

            //buildCreditCard(card);
            addPaymentMethod(card);
        }
    }




    /**
     * api call to add new card
     * @param
     */
    private void addPaymentMethod(Card card){
        Progress.showprogress(mActivity,getString(R.string.progress_loading),false);
        PaymentRequest paymentRequest=new PaymentRequest();
       // String token=appPreference.getUserDetails().getToken();

        String token=user.getToken();
        Log.e("token@@@2",token);

        JsonObject jsonObject = new JsonObject();
        try {

            jsonObject.addProperty(CARD_HOLDER_NAME,card.getCardHeadline());
            jsonObject.addProperty(CARD_NUMBER,card.getCardNumber());
            jsonObject.addProperty(CARD_TYPE,card.getCardType());
            jsonObject.addProperty(EXPIRATION_MONTH,card.getCardExpirationMonth());
            jsonObject.addProperty(EXPIRATION_YEAR,card.getCardExpirationYear());
            jsonObject.addProperty(CVV,card.getCardCvv());
        } catch (Exception e) {
            e.printStackTrace();
        }

        paymentRequest.addPayPalCard(token, jsonObject, new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean body) {
               Progress.dismissProgress();
                appPreference.saveUser(user);
                appPreference.setUserLogin(true);
                Intent bookServiceIntent=new Intent(mActivity, BookServiceActivity.class);
                bookServiceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(bookServiceIntent);
            }

            @Override
            public void onRequestFailed(String message) {
                Progress.dismissProgress();
                mActivity.showSnackbar(message,0);
                if(message.equals(Constants.AUTH_ERROR)){
                    mActivity.logOutUser();
                }
            }
        });
    }


}
