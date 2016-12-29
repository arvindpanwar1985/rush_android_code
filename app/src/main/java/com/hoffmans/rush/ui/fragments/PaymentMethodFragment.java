package com.hoffmans.rush.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.braintreegateway.CreditCard;
import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.models.CardBuilder;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.hoffmans.rush.R;
import com.hoffmans.rush.listners.BrainTreeHandler;
import com.hoffmans.rush.model.Card;
import com.hoffmans.rush.ui.activities.CreateAccountActivity;
import com.hoffmans.rush.utils.Progress;
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
public class PaymentMethodFragment extends BaseFragment implements View.OnClickListener ,BrainTreeHandler {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String nounce;
    private String mParam2;

    private EditText edtCardNumber,edtEdtTitular,edtExpiry,edtCvv,edtCountry,edtCity;
    private Button btnSaveCard;
    private CreditCard creditCard;
    private BraintreeFragment mBraintreeFragment;

    public PaymentMethodFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param btClientToken Parameter 1.
     * @param token Parameter 2.
     * @return A new instance of fragment PaymentMethodFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static PaymentMethodFragment newInstance(String btClientToken, String token) {
        PaymentMethodFragment fragment = new PaymentMethodFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, btClientToken);
        args.putString(ARG_PARAM2, token);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // set the listner for nounce generated and error from braintree
        ((CreateAccountActivity)context).setBrainTreeHandler(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nounce = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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
        initializeBrainTree();
        return paymentMethodView;
    }


    @Override
    protected void initViews(View view) {

        edtCardNumber=(EditText)view.findViewById(R.id.fpEdtCard);
        edtEdtTitular=(EditText)view.findViewById(R.id.fpEdtTitular);
        edtExpiry    =(EditText)view.findViewById(R.id.fpEdtExpiry);
        edtCvv       =(EditText)view.findViewById(R.id.fpEdtCvv);
        edtCountry   =(EditText)view.findViewById(R.id.fpEdtCountry);
        edtCity      =(EditText)view.findViewById(R.id.fpEdtCity);
        btnSaveCard  =(Button) view.findViewById(R.id.fpBtnSaveCard);

    }

    @Override
    protected void initListeners() {
        edtCardNumber.setOnClickListener(this);
        edtExpiry.setOnClickListener(this);
        edtCountry.setOnClickListener(this);
        edtCvv.setOnClickListener(this);
        edtEdtTitular.setOnClickListener(this);
        edtCity.setOnClickListener(this);
        btnSaveCard.setOnClickListener(this);

    }


    private void initializeBrainTree(){
        try {
            mBraintreeFragment = BraintreeFragment.newInstance(mActivity, nounce);

        } catch (InvalidArgumentException e) {
            mActivity.showSnackbar("Error Initializing payment method",0);
        }
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
                        edtExpiry.setText(monthYearPicker.getSelectedMonth()+1+"/"+monthYearPicker.getSelectedYear());
                    }
                },null);
                monthYearPicker.show();

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
        String expiry    =edtExpiry.getText().toString().trim();
        String country   =edtCountry.getText().toString().trim();
        String city      =edtCity.getText().toString().trim();
        String headLine  =edtEdtTitular.getText().toString().trim();
        String cvv       =edtCvv.getText().toString().trim();
        card.setCardNumber(cardnumber);
        card.setCardCvv(cvv);
        card.setCardHeadline(headLine);
        card.setCardExpiry(expiry);
        card.setCityCard(city);
        card.setCountryCard(country);
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
            if (card.getCardExpiry().length()==0) {
                //mUtils.showToast(mActivity, "Please select Month");
                mActivity.showSnackbar(getString(R.string.str_empty_expiry), 0);
                return;
            }

            if (card.getCityCard().length()==0) {
                //Utils.showToast(mActivity, "Cvv is blank");
                mActivity.showSnackbar(getString(R.string.str_empty_city),0);
                return;
            }
            if (card.getCardCvv().length()<3 || card.getCardCvv().length()>4) {
                // Utils.showToast(mActivity, "Invalid Cvv.");
                mActivity.showSnackbar(getString(R.string.str_invalid_cvv), 0);
                return;
            }
            if(card.getCountryCard().length()==0){
                mActivity.showSnackbar(getString(R.string.str_empty_country), 0);
                return;
            }
            if(card.getCardHeadline().length()==0){
                mActivity.showSnackbar(getString(R.string.str_empty_headline), 0);
                return;
            }
            buildCreditCard(card);
        }
    }


    /**
     * token the card at Braintree
     * @param card user input card
     */
    private void buildCreditCard(Card card){
        Progress.showprogress(mActivity,"Validating card..",false);
        CardBuilder cardBuilder = new CardBuilder()

                .cardholderName(card.getCardHeadline())
                .cardNumber(card.getCardNumber())
                .locality(card.getCityCard())
                .expirationDate(card.getCardExpiry()).cvv(card.getCardCvv())
                .countryName(card.getCountryCard());

        com.braintreepayments.api.Card.tokenize(mBraintreeFragment, cardBuilder);
    }


    @Override
    public void onError(Exception error) {
        Progress.dismissProgress();
        mActivity.showSnackbar(error.getMessage(),0);
    }
    @Override
    public void onNonceCreated(PaymentMethodNonce paymentMethodNonce) {
        Progress.dismissProgress();
    }
}
