package com.hoffmans.rush.ui.activities;

import android.os.Bundle;

import com.hoffmans.rush.R;
import com.hoffmans.rush.ui.fragments.CardListFragment;
import com.hoffmans.rush.utils.Constants;

public class CardListActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_card_list, getParentView());
        initToolBar(" ",false);
        hideToolbar();
        boolean isCardSelectable=getIntent().getBooleanExtra(Constants.KEY_IS_CARD_SELECTABLE,false);
        CardListFragment fragment=CardListFragment.newInstance(isCardSelectable,"");
        replaceFragment(fragment,R.id.content_cardList,false);



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
