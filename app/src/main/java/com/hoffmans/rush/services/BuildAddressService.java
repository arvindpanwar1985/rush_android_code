package com.hoffmans.rush.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.hoffmans.rush.model.FetchAddressEvent;
import com.hoffmans.rush.model.PickDropAddress;
import com.hoffmans.rush.model.Service;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class BuildAddressService extends IntentService {

    private Geocoder mGeocoder;
    public static final String ACTION_SEND_BROADCAST = "com.hoffmans.rush.services.action.broadcast";
    private static final String ACTION_BUILD_ADDRESS = "com.hoffmans.rush.services.action.BUILD_ADDRESS";
    private static final int    POS_PICK = 0;
    public static  String KEY_SERVICE_DATA="service_data";
    public static  String KEY_SUCCESS="service_success";



    private static final String EXTRA_DATA = "com.hoffmans.rush.services.extra.PARAM1";


    public BuildAddressService() {
        super("BuildAddressService");
    }


    public static void buildAddresses(Context context, ArrayList<PickDropAddress> data) {
        Intent intent = new Intent(context, BuildAddressService.class);
        intent.setAction(ACTION_BUILD_ADDRESS);
        intent.putParcelableArrayListExtra(EXTRA_DATA, data);

        context.startService(intent);
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            mGeocoder = new Geocoder(this, Locale.getDefault());
            final String action = intent.getAction();
            if (ACTION_BUILD_ADDRESS.equals(action)) {
                final ArrayList<PickDropAddress> paramListData = intent.getParcelableArrayListExtra(EXTRA_DATA);
                handleActionFoo(paramListData);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(ArrayList<PickDropAddress> data) {
        Service service =new Service();
        List<PickDropAddress> multipleDrops=new ArrayList<>();
        if(data!=null && data.size()!=0){

              for(int i=0;i<data.size();i++) {
                  PickDropAddress pickDropAddress=data.get(i);
                  try {
                      List<Address> addresses = mGeocoder.getFromLocation(pickDropAddress.getLatitude(), pickDropAddress.getLongitude(), 3);

                      if (addresses != null && addresses.size() > 0) {
                          Address address = addresses.get(1);
                          Address address_2 = addresses.get(2);
                          String country = address.getCountryName();
                          if(country==null){
                              country=address_2.getCountryName();
                          }
                          String state = address.getAdminArea();
                          if(state==null){
                              state=address_2.getAdminArea();
                          }
                          String city = address.getLocality();
                          if(city==null){
                              city=address_2.getLocality();
                          }
                          pickDropAddress.setCity(city);
                          pickDropAddress.setCountry(country);
                          pickDropAddress.setState(state);
                          if(i==POS_PICK){
                              service.setPick_address(pickDropAddress);
                          }else{
                              multipleDrops.add(pickDropAddress);
                          }
                          Log.e("data", "city :"+city + " country: " + country + " state: " + state);
                      }
                  }catch (Exception e){

                  }
              }
          }
          if(multipleDrops!=null){
              service.setDrop_addresses(multipleDrops);
              FetchAddressEvent fetchAddressEvent=new FetchAddressEvent();
              fetchAddressEvent.setService(service);
              fetchAddressEvent.setSucess(true);
              EventBus.getDefault().post(fetchAddressEvent);
          }else{
              FetchAddressEvent fetchAddressEvent=new FetchAddressEvent();
              fetchAddressEvent.setService(null);
              fetchAddressEvent.setSucess(false);
              EventBus.getDefault().post(fetchAddressEvent);
          }
    }



    private void sendBroadCast(Service service,boolean success){
        Intent intent = new Intent(ACTION_SEND_BROADCAST);
         if(success) {
             intent.putExtra(KEY_SERVICE_DATA, service);
         }
         intent.putExtra(KEY_SUCCESS,success);
        this.sendBroadcast(intent);
    }

}
