package com.hoffmans.rush.ui.activities;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.hoffmans.rush.R;

/**
 * Created by devesh on 19/12/16.
 */
public abstract  class BaseActivity extends AppCompatActivity {
    private FrameLayout activityContent = null;
    private Toolbar mToolbar = null;
    private int mLayoutId = 0;
    public static Toast toast;
    private int mActionBarSize;
    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide keyboard initially
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.actvity_base);
        if(toast==null){
            toast=new Toast(this);
        }
        // find the actionbarSize
        final TypedArray styledAttributes = getApplicationContext().getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize });
        mActionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
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
        if(toast!=null) {
            toast.cancel();
        }
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
            return;
        }
        super.onBackPressed();
    }



    /**
     *
     * @param message
     * @param lenth TSnackbar.LengthShort ,TSnackbar.LengthLong
     */
    public void showSnackbar(String message,int lenth) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.toast_layout_root));
        TextView textTitle = (TextView) layout.findViewById(R.id.text_error_title);
        textTitle.setText(message);
        if(toast!=null) {
            if(mToolbar!=null &&mToolbar.getVisibility()==View.GONE){
                mActionBarSize=0;
            }
            toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, mActionBarSize);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }
    }
    public  Toast getToast(){
        if(toast!=null){
            return  toast;
        }
        return null;
    }



    public Toolbar getToolBar(){
        return mToolbar;
    }
    /**
     * check for permission granted
     * @param arrPermission array of permission
     * @return all self permission granted
     */
    public boolean isPermissionGranted(String[]arrPermission){
        for(String permission:arrPermission){
            int result = ContextCompat.checkSelfPermission(BaseActivity.this, permission);
            if (result != PackageManager.PERMISSION_GRANTED){
                return false;
            }

        }
        return true;
    }

    /**
     *
     * @param arrGrantedPermission array of permission granted
     * @return all permission granted
     */
    public boolean isPermissionGranted(int[]arrGrantedPermission){
        for(int grantPermission:arrGrantedPermission){
            if(grantPermission!=PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }

        return true;
    }

    public GoogleApiClient  setGoogleSignInOptions(){
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        return  setGoogleApiClient(gso);
    }

    public GoogleApiClient setGoogleApiClient(GoogleSignInOptions gso){
        return new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

}

