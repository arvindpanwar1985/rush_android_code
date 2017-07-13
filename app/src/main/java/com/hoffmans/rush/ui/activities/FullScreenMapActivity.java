package com.hoffmans.rush.ui.activities;

import android.os.Bundle;
import android.util.Log;

import com.hoffmans.rush.R;
import com.hoffmans.rush.model.DateTime;
import com.hoffmans.rush.model.DriverDetail;
import com.hoffmans.rush.model.Estimate;
import com.hoffmans.rush.model.PickDropAddress;
import com.hoffmans.rush.model.VechileDetail;
import com.hoffmans.rush.ui.fragments.FullScreenMapFragment;
import com.hoffmans.rush.utils.AppPreference;
import com.hoffmans.rush.utils.Constants;

import java.util.List;

/**
 * Created by arvind on 15/6/17.
 */

public class FullScreenMapActivity extends BaseActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_fullscreen_map, getParentView());
        AppPreference appPreference=AppPreference.newInstance(this);

        if(getIntent()!=null){

            VechileDetail vechileDetail=getIntent().getParcelableExtra(Constants.KEY_VEHICLE_DETAILS);
            Estimate estimate=getIntent().getParcelableExtra(Constants.KEY_ESTIMATE_DATA);
            DriverDetail driverDetail=getIntent().getParcelableExtra(Constants.KEY_DRIVER_DETAILS);
            PickDropAddress pickDropAddress=getIntent().getParcelableExtra(Constants.KEY_PICK_ADDRESS);
            DateTime dateTime=getIntent().getParcelableExtra(Constants.KEY_DATA_DATE_TIME);
            List<PickDropAddress> drop_down=getIntent().getParcelableArrayListExtra(Constants.KEY_DROP_DOWN);
            String records=getIntent().getStringExtra(Constants.KEY_COMMENT);
            String status=getIntent().getStringExtra(Constants.SERVICE_STATUS);
            String serviceId=getIntent().getStringExtra(Constants.SERVICE_ID);
            double latitude=getIntent().getDoubleExtra(Constants.KEY_LAT,0.0);
            double longitude=getIntent().getDoubleExtra(Constants.KEY_LONG,0.0);
            Log.e("lat...",latitude+"");
            Log.e("longitude...",longitude+"");



            FullScreenMapFragment fragment=FullScreenMapFragment.newInstance(estimate, vechileDetail, driverDetail, pickDropAddress, dateTime,drop_down,records,status,serviceId,latitude,longitude);
            replaceFragment(fragment,R.id.content_full_screenmap,false);

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

