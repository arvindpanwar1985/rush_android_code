package com.hoffmans.rush.ui.driver.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hoffmans.rush.R;
import com.hoffmans.rush.location.LocationData;
import com.hoffmans.rush.model.User;
import com.hoffmans.rush.ui.activities.BaseActivity;
import com.hoffmans.rush.ui.driver.fragments.HomeFragment;
import com.hoffmans.rush.utils.AppPreference;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by devesh on 13/2/17.
 */

public class DriverNavigationActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {

    private LinearLayout edtprofileLinear;
    private TextView headerTxtName,headerTxtEmail,headerTxtPhone;
    private CircleImageView headerImgProfile;
    private AppPreference appPreference;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private HomeFragment fragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_driver_drawer, getParentView());
        initToolBar("",true,true);
        appPreference=AppPreference.newInstance(this);
        initManagers();
        initViews();
        initListeners();
        // checking intent when we are coming from AcceptOrder activity if we are getting intent with string
        if(getIntent()!=null && getIntent().getExtras()!=null){
            fragment=HomeFragment.newInstance(getIntent().getStringExtra(AcceptOrderActivity.KEY_SERVICE_MESSAGE),"");
        }else {
            fragment = HomeFragment.newInstance("", "");
        }
        replaceFragment(fragment,R.id.driver_navigation_content,true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        User user=appPreference.getUserDetails();
        headerTxtEmail.setText(user.getEmail());
        headerTxtPhone.setText(user.getPhone());
        headerTxtName.setText(user.getName());
        if(user.getPic_url()!=null && !TextUtils.isEmpty(user.getPic_url())){
            Glide.with(getApplicationContext()).load(user.getPic_url()).into(headerImgProfile);
        }

    }

    @Override
    protected void initManagers() {


    }

    @Override
    protected void initViews() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawer,  R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            /** Called when a mDrawerLayout has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a mDrawerLayout has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the mDrawerLayout mDrawerToggle as the DrawerListener
        drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        getToolBar().setNavigationIcon(R.drawable. ic_hamburger_icon);
        mDrawerToggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View navHeader=navigationView.getHeaderView(0);
        initheaderView(navHeader);

        // check if get admin message notification is pending
        if(AppPreference.newInstance(this).getAdminMessage().equals("")){

        }else{
            showAlertDialog(this,AppPreference.newInstance(this).getAdminMessage());
        }
    }

    @Override
    protected void initListeners() {
        edtprofileLinear.setOnClickListener(this);
    }

    private void initheaderView(View navHeader){
        edtprofileLinear=(LinearLayout) navHeader.findViewById(R.id.linearHeaderEdit);
        headerTxtEmail=(TextView)navHeader.findViewById(R.id.headerTxtEmail);
        headerTxtPhone=(TextView)navHeader.findViewById(R.id.headerTxtPhone);
        headerTxtName=(TextView)navHeader.findViewById(R.id.headerTxtName);
        headerImgProfile=(CircleImageView)navHeader.findViewById(R.id.headerImgProfile);
    }
    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_logout:
                //logout user
                String uuid =appPreference.getNoticficationToken();
                if(TextUtils.isEmpty(uuid)){
                    uuid= FirebaseInstanceId.getInstance().getToken();
                    appPreference.setNotificationToken(uuid);
                    }
                 singOutUser(uuid);
                break;
            case R.id.nav_home:
                if (!isFragmentOpened(HomeFragment.class.getCanonicalName())) {
                    HomeFragment fragment = HomeFragment.newInstance("", "");
                    replaceFragment(fragment, R.id.driver_navigation_content, true);
                } else {
                    drawer.closeDrawer(GravityCompat.START);
                }
                break;

            case R.id.nav_editProfile:
                Intent edtProfileIntent=new Intent(DriverNavigationActivity.this,DriverEditprofileActivity.class);
                edtProfileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(edtProfileIntent);
                break;

            case R.id.nav_Upcoming:
                Intent upcomingIntent = new Intent(DriverNavigationActivity.this, UpcomingActivity.class);
                startActivity(upcomingIntent);
                break;
            case R.id.nav_record:
                Intent recordIntent = new Intent(DriverNavigationActivity.this, CompletedRecords.class);
                recordIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(recordIntent);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.linearHeaderEdit:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                Intent edtProfileIntent=new Intent(DriverNavigationActivity.this,DriverEditprofileActivity.class);
                edtProfileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(edtProfileIntent);
              break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LocationData.REQUEST_CHECK_SETTINGS){
            fragment.onActivityResult(requestCode, resultCode, data);
        }else {
         super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public  void showAlertDialog(Context context, String message) {
        try {
            AppPreference.newInstance(this).setAdminMessage("");
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
            builder.setTitle(R.string.app_name)
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.str_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();

                        }
                    }).create().show();
        }catch (Exception e){

        }
    }

}
