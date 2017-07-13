package com.hoffmans.rush.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.hoffmans.rush.R;
import com.hoffmans.rush.ui.fragments.FragmentAddComment;
import com.hoffmans.rush.utils.Constants;

/**
 * Created by arvind on 22/5/17.
 */

public class AddCommentActivity extends BaseActivity{


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_add_comment, getParentView());
        initToolBar(getString(R.string.str_comment),true);
        if(getIntent()!=null){
            Fragment fragment=FragmentAddComment.newInstance(getIntent().getStringExtra(Constants.KEY_COMMENT));
            replaceFragment(fragment,R.id.content_add_comment);
        }else{
            Fragment fragment=new FragmentAddComment();
            replaceFragment(fragment,R.id.content_add_comment);
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
