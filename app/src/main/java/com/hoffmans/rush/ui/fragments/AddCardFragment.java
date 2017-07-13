package com.hoffmans.rush.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.hoffmans.rush.bean.CardListBean;
import com.hoffmans.rush.http.request.PaymentRequest;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.model.Card;
import com.hoffmans.rush.model.CardData;
import com.hoffmans.rush.ui.activities.AddCardActivity;
import com.hoffmans.rush.utils.Constants;
import com.hoffmans.rush.utils.Progress;
import com.hoffmans.rush.utils.Utils;
import com.hoffmans.rush.utils.Validation;
import com.hoffmans.rush.widgets.MonthYearPicker;

import java.util.ArrayList;


public class AddCardFragment extends BaseFragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "bt_token";
    private static final String CARD_HOLDER_NAME="cardholder_name";
    private static final String CARD_NUMBER="card_number";
    private static final String CARD_TYPE="card_type";
    private static final String EXPIRATION_MONTH="expiration_month";
    private static final String EXPIRATION_YEAR="expiration_year";
    private static final String CVV="cvv";


    private String bt_token;
    private EditText edtCardNumber,edtEdtTitular,edtCvv;
    private TextView txtExpiry;
    private Button btnSaveCard;

    private LinearLayout topLinear;



    public AddCardFragment() {
        // Required empty public constructor
    }
    public static AddCardFragment newInstance(String btToken) {

        AddCardFragment fragment = new AddCardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, btToken);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // set the listner for nounce generated and error from braintree
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bt_token = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity.initToolBar(getString(R.string.str_add_card),true,false);
        View view =inflater.inflate(R.layout.fragment_payment_methods, container, false);
        initViews(view);
        initListeners();

        return view;
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
    private Card getUserInputCreditCard(){
        Card card=new Card();
        String cardnumber=edtCardNumber.getText().toString().trim();
        String expiry    =txtExpiry.getText().toString().trim();
        String headLine  =edtEdtTitular.getText().toString().trim();
        String cvv       =edtCvv.getText().toString().trim();

        if (!Validation.isValidCreditCard(cardnumber)) {
                //Utils.showToast(mActivity, "Invalid Card Number");
                mActivity.showSnackbar(getString(R.string.error_title_invalid_card),0);
                return null;

            } else if (headLine.length()==0) {
                //mUtils.showToast(mActivity, "Please select Month");
                mActivity.showSnackbar(getString(R.string.error_invalid_card_holdername), 0);
                return null;

            } else if (expiry.length()==0) {
                //mUtils.showToast(mActivity, "Please select Month");
                mActivity.showSnackbar(getString(R.string.str_empty_expiry), 0);
                return null;

            } else if(cardnumber.length()==15 && cvv.length()!=4){
                mActivity.showSnackbar(getString(R.string.str_invalid_cvv), 0);
                return null;

            } else   if(cardnumber.length()==16 && cvv.length()!=3) {
                mActivity.showSnackbar(getString(R.string.str_invalid_cvv), 0);
                return null;
            }


        String cardType=Utils.getCreditCardTypeByNumber(cardnumber);

        // split date into month and year
        String[] dateSplit=expiry.split("/");
        String expirationMonth= dateSplit[0];
        String expirationYear=dateSplit[1];

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

            Card card=getUserInputCreditCard();
            if(card!=null){
                //  buildCreditCard(card);
                addCard(card);
            }




    }



    private void addCard(Card card){
        Progress.showprogress(mActivity,getString(R.string.progress_loading),false);
        PaymentRequest paymentRequest=new PaymentRequest();
        String token=appPreference.getUserDetails().getToken();

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
                       CardListBean cardListBean=(CardListBean)body;
                       if(cardListBean.getCards().size()!=0){
                           ArrayList<CardData> cardDataList=cardListBean.getCards();
                           //CardData newCardData=cardListBean.getCards().get(0);
                           Intent intent=new Intent();
                           intent.putParcelableArrayListExtra(AddCardActivity.KEY_CARD_DATA,cardDataList);
                           getActivity().setResult(Activity.RESULT_OK,intent);
                           mActivity.finish();
                       }

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
