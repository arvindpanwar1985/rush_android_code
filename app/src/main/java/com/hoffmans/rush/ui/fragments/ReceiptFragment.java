package com.hoffmans.rush.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hoffmans.rush.R;
import com.hoffmans.rush.model.DateTime;
import com.hoffmans.rush.model.PickDropAddress;
import com.hoffmans.rush.model.TransactionDetails;
import com.hoffmans.rush.utils.Constants;

import java.util.ArrayList;


/**
 * The type Receipt fragment.
 */
public class ReceiptFragment extends BaseFragment {
    private DateTime date_time;
    private TransactionDetails transactionDetails;
    private String mStreetAddress;
    private ArrayList<PickDropAddress> dropAddressArrayList;
    private TextView txtDate,txtTime,txtAmount,txtTransactionId,txtAuthorized,txtPickAddress,txtDropAddress;
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
    public static ReceiptFragment newInstance(DateTime dateTime, TransactionDetails details,String street_address,ArrayList<PickDropAddress> dropAddressArrayList) {
        ReceiptFragment fragment = new ReceiptFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.KEY_DATA_DATE_TIME, dateTime);
        args.putParcelable(Constants.KEY_DATA_TRANSACTION,details);
        args.putString(Constants.KEY_PICK_ADDRESS,street_address);
        args.putParcelableArrayList(Constants.KEY_DROP_ADDRESS,dropAddressArrayList);

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
            dropAddressArrayList   =getArguments().getParcelableArrayList(Constants.KEY_DROP_ADDRESS);
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
        txtDropAddress   =(TextView)view.findViewById(R.id.reTxtDropAddress);
        txtTransactionId =(TextView)view.findViewById(R.id.reTxtTransactionId);
        txtAuthorized    =(TextView)view.findViewById(R.id.reTxtAuthorised);
        txtTime          =(TextView)view.findViewById(R.id.reTxtTime);
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
        if(dropAddressArrayList.size()>1) {
            setMultipleAddresses();
        }else{
            txtDropAddress.setText(dropAddressArrayList.get(0).getStreetAddress());
        }

    }
    /**
     * set multiple drop addresses
     */
    private void setMultipleAddresses(){
        if(dropAddressArrayList!=null && dropAddressArrayList.size()>0){
            StringBuilder dropAddressBuilder=null;
            for(int i=0;i<dropAddressArrayList.size();i++){
                PickDropAddress dropAddress=dropAddressArrayList.get(i);
                int serialNumber=i+1;
                if(i==0) {
                    dropAddressBuilder = new StringBuilder().append(serialNumber).append(". ");
                    dropAddressBuilder.append(dropAddress.getStreetAddress());
                }else{
                    dropAddressBuilder.append("\n").append(serialNumber).append(". ").append(dropAddress.getStreetAddress());
                }
            }
            txtDropAddress.setText(dropAddressBuilder.toString());
        }
    }


}
