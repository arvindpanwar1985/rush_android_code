package com.hoffmans.rush.location;

import android.location.Location;

/**
 * Created by devesh.bhandari on 4/28/2015.
 */
public interface LocationInterface {

     void onLocation(Location location);
     void onLocationFailed();

}
