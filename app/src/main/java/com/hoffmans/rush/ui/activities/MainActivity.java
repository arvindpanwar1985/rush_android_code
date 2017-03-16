package com.hoffmans.rush.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hoffmans.rush.R;

/**
 * Created by devesh on 13/2/17.
 */

public class MainActivity extends BaseActivity implements View.OnClickListener {


    private Button btnLoginAsDriver,btnLoginAsUser;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        initViews();
        initListeners();
    }

    @Override
    protected void initViews() {

        btnLoginAsDriver=(Button)findViewById(R.id.btnDriver);
        btnLoginAsUser=(Button)findViewById(R.id.btnCustomer);
    }

    @Override
    protected void initListeners() {

        btnLoginAsDriver.setOnClickListener(this);
        btnLoginAsUser.setOnClickListener(this);
    }

    @Override
    protected void initManagers() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnDriver:
                Toast.makeText(getApplicationContext(),"Under Development", Toast.LENGTH_SHORT).show();
               /* Intent driverIntent=new Intent(getApplicationContext(), DriverLoginActivity.class);
                driverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(driverIntent);*/
                break;

            case R.id.btnCustomer:
                Intent userIntent=new Intent(getApplicationContext(), LoginActivity.class);
                userIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(userIntent);
                break;
        }
    }
}
