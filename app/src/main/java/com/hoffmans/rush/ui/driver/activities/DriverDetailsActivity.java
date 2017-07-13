package com.hoffmans.rush.ui.driver.activities;

import android.os.Bundle;

import com.hoffmans.rush.R;
import com.hoffmans.rush.model.CustomerDetail;
import com.hoffmans.rush.model.DateTime;
import com.hoffmans.rush.model.Estimate;
import com.hoffmans.rush.model.PickDropAddress;
import com.hoffmans.rush.ui.activities.BaseActivity;
import com.hoffmans.rush.ui.driver.fragments.DriverDetailsFragment;
import com.hoffmans.rush.utils.AppPreference;
import com.hoffmans.rush.utils.Constants;

import java.util.List;

/**
 * Created by arvind on 19/5/17.
 */

public class DriverDetailsActivity  extends BaseActivity{


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_user_details, getParentView());
        AppPreference appPreference=AppPreference.newInstance(this);

        if(getIntent()!=null){

           // VechileDetail vechileDetail=getIntent().getParcelableExtra(Constants.KEY_VEHICLE_DETAILS);
            Estimate estimate=getIntent().getParcelableExtra(Constants.KEY_ESTIMATE_DATA);
            CustomerDetail customerDetail=getIntent().getParcelableExtra(Constants.KEY_DRIVER_DETAILS);
            PickDropAddress pickDropAddress=getIntent().getParcelableExtra(Constants.KEY_PICK_ADDRESS);
            DateTime dateTime=getIntent().getParcelableExtra(Constants.KEY_DATA_DATE_TIME);
            List<PickDropAddress> drop_down=getIntent().getParcelableArrayListExtra(Constants.KEY_DROP_DOWN);
            String records=getIntent().getStringExtra(Constants.KEY_COMMENT);
            String status=getIntent().getStringExtra(Constants.SERVICE_STATUS);

            DriverDetailsFragment fragment=DriverDetailsFragment.newInstance(estimate,customerDetail,pickDropAddress,dateTime,drop_down,records,status);
            replaceFragment(fragment,R.id.content_user_details,false);

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
