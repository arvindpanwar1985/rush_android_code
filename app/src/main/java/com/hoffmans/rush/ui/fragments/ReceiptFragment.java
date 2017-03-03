package com.hoffmans.rush.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hoffmans.rush.R;
import com.hoffmans.rush.model.DateTime;
import com.hoffmans.rush.model.TransactionDetails;
import com.hoffmans.rush.utils.Constants;


/**
 * The type Receipt fragment.
 */
public class ReceiptFragment extends BaseFragment {

    private DateTime date_time;
    private TransactionDetails transactionDetails;
    private String mStreetAddress;
    private TextView txtDate,txtTime,txtAmount,txtTransactionId,txtAuthorized,txtPickAddress;

    // TODO: Rename and change types of parameters
    private String mParam1;


    /**
     * Instantiates a new Receipt fragment.
     */
    public ReceiptFragment() {
        // Required empty public constructor
    }


    /**
     * New instance receipt fragment.
     *
     * @param dateTime       the date time
     * @param details        the details
     * @param street_address the street address
     * @return the receipt fragment
     */
    public static ReceiptFragment newInstance(DateTime dateTime, TransactionDetails details,String street_address) {
        ReceiptFragment fragment = new ReceiptFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.KEY_DATA_DATE_TIME, dateTime);
        args.putParcelable(Constants.KEY_DATA_TRANSACTION,details);
        args.putString(Constants.KEY_PICK_ADDRESS,street_address);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            date_time=getArguments().getParcelable(Constants.KEY_DATA_DATE_TIME);
            transactionDetails=getArguments().getParcelable(Constants.KEY_DATA_TRANSACTION);
            mStreetAddress=getArguments().getString(Constants.KEY_PICK_ADDRESS);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_receipt, container, false);
        initViews(view);
        initListeners();
        return view;
    }



    @Override
    protected void initViews(View view) {

        txtDate          =(TextView)view.findViewById(R.id.reTxtDate);
        txtTime          =(TextView)view.findViewById(R.id.reTxtTime);
        txtAmount        =(TextView)view.findViewById(R.id.reTxtAmount);
        txtPickAddress   =(TextView)view.findViewById(R.id.reTxtPickAddress);
        txtTransactionId =(TextView)view.findViewById(R.id.reTxtTransactionId);
        txtAuthorized    =(TextView)view.findViewById(R.id.reTxtAuthorised);
        txtTime    =(TextView)view.findViewById(R.id.reTxtTime);

        try{setData();}catch (NullPointerException e){};


    }

    @Override
    protected void initListeners() {

    }


    private void setData() throws NullPointerException{
        if(transactionDetails!=null){
            txtTransactionId.setText(transactionDetails.getPaymentTransactionId());
            txtAuthorized.setText(transactionDetails.getStatus());
            txtAmount.setText( transactionDetails.getSymbol()+" "+transactionDetails.getConverted_amount());

        }
        if(date_time!=null){
            txtDate.setText(date_time.getDate());
            txtTime.setText(date_time.getTime());
        }
        txtPickAddress.setText(mStreetAddress);
    }


}
