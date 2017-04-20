package com.hoffmans.rush.listners;

/**
 * Created by devesh on 14/4/17.
 */

public interface ServiceCallbacks {
    void onDataRecieved(String driver_id,double latitude,double longitude,String vechile_id);
}
