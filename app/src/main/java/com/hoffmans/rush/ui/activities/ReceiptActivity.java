package com.hoffmans.rush.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import com.hoffmans.rush.R;
import com.hoffmans.rush.model.DateTime;
import com.hoffmans.rush.model.PickDropAddress;
import com.hoffmans.rush.model.TransactionDetails;
import com.hoffmans.rush.ui.fragments.ReceiptFragment;
import com.hoffmans.rush.utils.Constants;

import java.util.ArrayList;

public class ReceiptActivity extends BaseActivity {
    private ArrayList<PickDropAddress> drop_address;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_receipt, getParentView());
        initToolBar(getString(R.string.str_receipt),true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black);
        DateTime dateTime=getIntent().getParcelableExtra(Constants.KEY_DATA_DATE_TIME);
        String street_address=getIntent().getStringExtra(Constants.KEY_PICK_ADDRESS);
        try {
             drop_address = getIntent().getParcelableArrayListExtra(Constants.KEY_DROP_ADDRESS);
        }catch (NullPointerException e){

        }
        TransactionDetails transactionDetails=getIntent().getParcelableExtra(Constants.KEY_DATA_TRANSACTION);
        ReceiptFragment fragment=ReceiptFragment.newInstance(dateTime,transactionDetails,street_address,drop_address);
        replaceFragment(fragment,R.id.contentReceipt,true);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initManagers() {

    }

    @Override
    public void onBackPressed() {
        Intent newEstimateIntent=new Intent(ReceiptActivity.this,BookServiceActivity.class);
        newEstimateIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(newEstimateIntent);
    }
}
