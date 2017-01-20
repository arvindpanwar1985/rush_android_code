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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_confirm_service, getParentView());
        initToolBar(" ",true);
        if(getIntent()!=null) {
            Estimate estimate    =getIntent().getParcelableExtra(Constants.KEY_ESTIMATE_DATA);
            CardData defaultCard =getIntent().getParcelableExtra(Constants.KEY_CARD_DATA);
            Service service      =getIntent().getParcelableExtra(Constants.KEY_PARAM_DATA);
            ConfirmServiceFragment fragment =ConfirmServiceFragment.newInstance(estimate,defaultCard,service);
            replaceFragment(fragment, R.id.content_confirm_servicer, false);
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
