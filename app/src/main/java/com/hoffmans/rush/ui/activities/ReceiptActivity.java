package com.hoffmans.rush.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import com.hoffmans.rush.R;
import com.hoffmans.rush.model.DateTime;
import com.hoffmans.rush.model.TransactionDetails;
import com.hoffmans.rush.ui.fragments.ReceiptFragment;
import com.hoffmans.rush.utils.Constants;

public class ReceiptActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_receipt, getParentView());
        initToolBar("Receipt",true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black);
        DateTime dateTime=getIntent().getParcelableExtra(Constants.KEY_DATA_DATE_TIME);
        String street_address=getIntent().getStringExtra(Constants.KEY_PICK_ADDRESS);
        TransactionDetails transactionDetails=getIntent().getParcelableExtra(Constants.KEY_DATA_TRANSACTION);
        ReceiptFragment fragment=ReceiptFragment.newInstance(dateTime,transactionDetails,street_address);
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
