package com.hoffmans.rush.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import com.hoffmans.rush.R;
import com.hoffmans.rush.ui.fragments.AddCardFragment;
import com.hoffmans.rush.utils.AppPreference;

public class AddCardActivity extends BaseActivity   {

    public static final String KEY_CARD_DATA="key_card_data";
    public  static final  int REQUEST_ADD_CARD=190;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_add_card, getParentView());
        AppPreference appPreference=AppPreference.newInstance(this);
        AddCardFragment fragment=AddCardFragment.newInstance(appPreference.getUserDetails().getBt_token());
        replaceFragment(fragment,R.id.content_add_card,false);
    }

    @Override
    protected void initViews() {
      // init view here
    }

    @Override
    protected void initListeners() {
      //init listener here
    }

    @Override
    protected void initManagers() {
       //init manager
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
