package com.hoffmans.rush.ui.driver.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.hoffmans.rush.R;
import com.hoffmans.rush.ui.activities.BaseActivity;

/**
 * Created by devesh on 21/3/17.
 */

public class RatingActivity extends BaseActivity implements View.OnClickListener{

    private ImageView imgClose;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolBar("",false);
        hideToolbar();
        getLayoutInflater().inflate(R.layout.activity_accept_order,getParentView());
        initManagers();
        initViews();
        initListeners();
    }

    @Override
    protected void initViews() {
        imgClose   =(ImageView)findViewById(R.id.imgARClose);
    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initManagers() {

    }

    @Override
    public void onClick(View view) {

        int id =view.getId();
        switch (id){
              case R.id.imgARClose:
                finish();
                break;
        }
    }
}
