package com.hoffmans.rush.ui.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.hoffmans.rush.R;

/**
 * Created by devesh on 19/12/16.
 */
public abstract  class BaseActivity extends AppCompatActivity {
    private FrameLayout activityContent = null;
    private Toolbar mToolbar = null;
    private int mLayoutId = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide keyboard initially
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.actvity_base);
        //set orientation to portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        activityContent = (FrameLayout) this.findViewById(R.id.activity_content);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

        }
        return super.onOptionsItemSelected(item);
    }




    /**
     * initialize Toolbar
     *
     * @param title        : title to set to Toolbar
     * @param isHomeEnable : check is Back Button Enable or not
     */
    public void initToolBar(String title, boolean isHomeEnable) {
        mToolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(mToolbar);
        if (isHomeEnable) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setTitle(null);

        }else{
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setTitle(null);
        }


    }

    public  void hideToolbar(){
        if(mToolbar!=null){
            mToolbar.setVisibility(View.GONE);
        }
    }

    protected FrameLayout getParentView() {
        return activityContent;
    }

    /**
     * replace given fragment to layout
     *
     * @param fragment        fragment to replace
     * @param layoutToReplace layout which hold fragment/
     */
    public void replaceFragment(Fragment fragment, int layoutToReplace) {
        replaceFragment(fragment, layoutToReplace, true);
    }


    /**
     *
     * @param fragment fragment to replace
     * @param layoutToReplace layout to replace
     * @param storeInStack boolean store in back stack
     */
    public void replaceFragment(Fragment fragment, int layoutToReplace, Boolean storeInStack) {

        try {
            View view = findViewById(layoutToReplace);
            if (mLayoutId == 0) {
                if (view != null) {
                    mLayoutId = layoutToReplace;
                }
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(mLayoutId, fragment,fragment.getClass().getCanonicalName());
            if (storeInStack) {
                transaction.addToBackStack(fragment.getClass().getCanonicalName());
            }

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * @param fragment fragment to replace
     * @param layoutToReplace layout to replace
     * @param storeInStack boolean store in back stack
     * @param storeInStack boolean add or replace
     */
    public void replaceFragment(Fragment fragment, int layoutToReplace, boolean storeInStack, boolean isAdd) {

        try {
            View view = findViewById(layoutToReplace);
            if (mLayoutId == 0) {
                if (view != null) {
                    mLayoutId = layoutToReplace;
                }
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (isAdd) {
                //TODO add anim files
                //transaction.setCustomAnimations(R.anim.slide_out_up, R.anim.slide_in_up, 0, R.anim.slide_in_up);
                transaction.add(mLayoutId, fragment);
            } else {
                transaction.replace(mLayoutId, fragment);
            }
            if (storeInStack) {
                transaction.addToBackStack(fragment.getClass().getCanonicalName());
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * initialize common methods and utilities
     */
    /**
     * initialize all views of activity under this method
     */
    protected abstract void initViews();

    /**
     * initialize all Listeners under this method
     */
    protected abstract void initListeners();

    /**
     * initialize manager classes and user program under this method
     */
    protected abstract void initManagers();


    /**
     * override onBackPressed to maintain Fragment Stack OnBackPress
     */
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
            return;
        }
        super.onBackPressed();
    }




}

