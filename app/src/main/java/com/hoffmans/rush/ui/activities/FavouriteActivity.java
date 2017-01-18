package com.hoffmans.rush.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.hoffmans.rush.R;
import com.hoffmans.rush.ui.fragments.FavouriteFragment;
import com.hoffmans.rush.utils.Constants;

/**
 * Created by devesh on 18/1/17.
 */

public class FavouriteActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_favourite, getParentView());
        initToolBar(" ",false);
        hideToolbar();
        if(getIntent()!=null){
            boolean showIcons=getIntent().getBooleanExtra(Constants.KEY_IS_FAVOURITE_SELECTABLE,false);
            Fragment fragment= FavouriteFragment.newInstance(showIcons);
            replaceFragment(fragment,R.id.content_fav,false);
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
