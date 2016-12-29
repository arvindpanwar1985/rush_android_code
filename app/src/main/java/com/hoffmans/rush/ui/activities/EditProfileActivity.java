package com.hoffmans.rush.ui.activities;

import android.os.Bundle;

import com.hoffmans.rush.R;
import com.hoffmans.rush.ui.fragments.EditProfileFragment;

/**
 * Created by devesh on 28/12/16.
 */

public class EditProfileActivity extends BaseActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_edit_profile,getParentView());
        EditProfileFragment editProfileFragment=EditProfileFragment.newInstance("","");
        replaceFragment(editProfileFragment,R.id.content_editProfile,true);
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
