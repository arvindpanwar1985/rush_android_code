package com.hoffmans.rush.ui.driver.activities;

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
import com.hoffmans.rush.R;
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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_driver_drawer, getParentView());
        initToolBar("",true,true);
        appPreference=AppPreference.newInstance(this);
        initManagers();
        initViews();
        initListeners();
        HomeFragment fragment=HomeFragment.newInstance("","");
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

        /*int width = getResources().getDisplayMetrics().widthPixels*60/100;
        DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) navigationView.getLayoutParams();
        params.width = width;
        navigationView.setLayoutParams(params);*/
        View navHeader=navigationView.getHeaderView(0);
        initheaderView(navHeader);

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
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
              break;
        }
    }
}
