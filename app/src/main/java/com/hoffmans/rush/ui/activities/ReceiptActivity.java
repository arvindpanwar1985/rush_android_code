package com.hoffmans.rush.ui.activities;

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
        TransactionDetails transactionDetails=getIntent().getParcelableExtra(Constants.KEY_DATA_TRANSACTION);
        ReceiptFragment fragment=ReceiptFragment.newInstance(dateTime,transactionDetails);
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
}
