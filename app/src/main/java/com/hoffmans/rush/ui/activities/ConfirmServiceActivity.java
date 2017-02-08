package com.hoffmans.rush.ui.activities;

import android.os.Bundle;

import com.hoffmans.rush.R;
import com.hoffmans.rush.model.CardData;
import com.hoffmans.rush.model.Estimate;
import com.hoffmans.rush.model.Service;
import com.hoffmans.rush.ui.fragments.ConfirmServiceFragment;
import com.hoffmans.rush.utils.Constants;

/**
 * Created by devesh on 11/1/17.
 */

public class ConfirmServiceActivity extends BaseActivity {

    public static final String KEY_CARD_DATA="key_card_data";
    private ConfirmServiceFragment fragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_confirm_service, getParentView());
        initToolBar(getString(R.string.str_menu_order),true);
        if(getIntent()!=null) {
            try {
                Estimate estimate = getIntent().getParcelableExtra(Constants.KEY_ESTIMATE_DATA);
                CardData defaultCard = getIntent().getParcelableExtra(Constants.KEY_CARD_DATA);
                Service service = getIntent().getParcelableExtra(Constants.KEY_PARAM_DATA);
                int transID = getIntent().getIntExtra(Constants.KEY_TRANSACTION_ID,0);
                fragment = ConfirmServiceFragment.newInstance(estimate, defaultCard, service, transID);
                replaceFragment(fragment, R.id.content_confirm_servicer, false);
            }catch (NullPointerException e){

            }
        }

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
