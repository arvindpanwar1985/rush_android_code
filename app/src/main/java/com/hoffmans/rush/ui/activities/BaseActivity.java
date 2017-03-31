package com.hoffmans.rush.ui.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.hoffmans.rush.R;
import com.hoffmans.rush.bean.BaseBean;
import com.hoffmans.rush.http.request.UserRequest;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.utils.AppPreference;
import com.hoffmans.rush.utils.Constants;
import com.hoffmans.rush.utils.DateUtils;
import com.hoffmans.rush.utils.Progress;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by devesh on 19/12/16.
 */
public abstract  class BaseActivity extends AppCompatActivity {

    protected FrameLayout activityContent = null;
    private Toolbar mToolbar = null;
    public int idGoogleApiclient;
    private int mLayoutId = 0;
    public static Toast toast;
    private int mActionBarSize,statusBarHeight;
    private GoogleApiClient mGoogleApiClient;
    private TextView titleTxt;
    private ImageView imgLogo;
    private ProgressBar progressBar;
    private RelativeLayout progressRelative;

    public boolean userIsInteracting;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide keyboard initially
       // getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Rect rectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        statusBarHeight = rectangle.top;
        setContentView(R.layout.actvity_base);
        initUI();

    }

    private void initUI(){
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        progressRelative=(RelativeLayout)findViewById(R.id.progressRelative);
        titleTxt   =(TextView)findViewById(R.id.tv_toolbar);
        imgLogo=(ImageView)findViewById(R.id.imgLogo);
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


    @Override
    protected void onStart() {
        super.onStart();
        if(toast!=null) {
            toast.cancel();
        }
    }

    /**
     * initialize Toolbar
     *
     * @param title        : title to set to Toolbar
     * @param isHomeEnable : check is Back Button Enable or not
     */
    public void initToolBar(String title, boolean isHomeEnable) {
        mToolbar.setVisibility(View.VISIBLE);
        titleTxt.setText(title);
        setSupportActionBar(mToolbar);
        setHomeEnabled(isHomeEnable);



    }


    /**
     * initialize Toolbar
     *
     * @param title        : title to set to Toolbar
     * @param isHomeEnable : check is Back Button Enable or not
     */
    public void initToolBar(String title, boolean isHomeEnable,boolean showLogo) {
        mToolbar.setVisibility(View.VISIBLE);
        titleTxt.setText(title);
        if(showLogo){
        imgLogo.setVisibility(View.VISIBLE);}else{
            imgLogo.setVisibility(View.INVISIBLE);
        }
        setSupportActionBar(mToolbar);
        setHomeEnabled(isHomeEnable);



    }

    private void setHomeEnabled(boolean isHomeEnable){
        if (isHomeEnable) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
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
        layout.setMinimumHeight(mActionBarSize);
        TextView textTitle = (TextView) layout.findViewById(R.id.text_error_title);
        textTitle.setText(message);
        if(toast!=null) {
            if(mToolbar!=null &&mToolbar.getVisibility()==View.GONE){
                mActionBarSize=0;
            }
            toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
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




    public  void showProgress(){
        progressRelative.setVisibility(View.VISIBLE);
    }

    public void hideProgress(){
        progressRelative.setVisibility(View.GONE);
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

    /**
     * set the google signin options
     * @param value random integer value
     * @return
     */
    public GoogleApiClient  setGoogleSignInOptions(int value){
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        return  setGoogleApiClient(gso,value);
    }


    public GoogleApiClient getGoogleApiClient(){
        return mGoogleApiClient;

    }

    public GoogleApiClient setGoogleApiClient(GoogleSignInOptions gso ,int value){

        return new GoogleApiClient.Builder(this)
                .enableAutoManage(this, value,new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }


    public void signOutGoogle(){
        if(mGoogleApiClient!=null) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {

                        }
                    });
            mGoogleApiClient.disconnect();
        }
    }



    /**
     * format the date String
     * @param year
     * @param month
     * @param day
     * @return
     */
    public String formatDate(int year, int month, int day) {

        GregorianCalendar c = new GregorianCalendar(year, month, day);
//        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_FORMAT);
        return sdf.format(c.getTime());
    }


    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }

    /**
     *
     * logout user from app locally
     */
    public void logOutUser(){
        AppPreference preference =AppPreference.newInstance(BaseActivity.this);
        preference.logoutUser();
        Intent intent =new Intent(BaseActivity.this,MainActivity.class);
        intent.putExtra(Constants.KEY_AUTH_ERROR,false);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /**
     * chanhe the app locale
      * @param languageToLoad name of language to load
     */
   public void changeLanguage(String languageToLoad){
       Locale locale = new Locale(languageToLoad);
       Locale.setDefault(locale);
       Configuration config = new Configuration();
       config.locale = locale;
       getBaseContext().getResources().updateConfiguration(config,
               getBaseContext().getResources().getDisplayMetrics());
   }


    /**
     *
     * @param className frgment to be searched
     * @return is fragment attached
     */
    public boolean isFragmentOpened(String className){
        int backCount=getSupportFragmentManager().getBackStackEntryCount();
        if (!getSupportFragmentManager().getBackStackEntryAt(backCount - 1).getName().equals(className)){
            return false;
        }
        return true;
    }

    /**
     * api call to signout user
     * @param uuid
     */
    public void singOutUser(String uuid){
        Progress.showprogress(this,getString(R.string.progress_loading),false);
        AppPreference preference=AppPreference.newInstance(BaseActivity.this);
        String token=preference.getUserDetails().getToken();
        UserRequest logoutUser=new UserRequest();
        logoutUser.userLogout(token, uuid, new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean body) {
                Progress.dismissProgress();
                logOutUser();
            }
            @Override
            public void onRequestFailed(String message) {
                Progress.dismissProgress();
                showSnackbar(message,0);
                if(message.equals(Constants.AUTH_ERROR)){
                    logOutUser();
                }
            }
        });
    }
}

