package com.hoffmans.rush.ui.activities;

import android.os.Bundle;

import com.hoffmans.rush.R;
import com.hoffmans.rush.ui.fragments.CardListFragment;

public class CardListActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_card_list, getParentView());
        initToolBar(" ",false);
        hideToolbar();
        CardListFragment fragment=CardListFragment.newInstance("","");
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
