package com.hoffmans.rush.services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.hoffmans.rush.listners.ServiceCallbacks;
import com.hoffmans.rush.utils.ApiConfig;
import com.hoffmans.rush.utils.AppPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class UpdateDriversOnMapService extends Service {
    private MyBinder binder = new MyBinder();
    private double DEFAULT_DISTANCE = 5000;  //distance in meters

    private ServiceCallbacks serviceCallbacksListner;
    private EchoWebSocketListener listener;
    private WebSocket webSocket;
    private boolean misBindedToactivity = false;
    private okhttp3.OkHttpClient client;
    private Location customerLocation;


    public UpdateDriversOnMapService() {
    }


    public class MyBinder extends Binder {
        public UpdateDriversOnMapService getService() {
            return UpdateDriversOnMapService.this;
        }
    }

    public void setServiceCallBack(ServiceCallbacks listener) {
        serviceCallbacksListner = listener;
    }

    @Override
    public IBinder onBind(Intent intent) {
        misBindedToactivity = true;
        return binder;
    }


    @Override
    public void onRebind(Intent intent) {
        misBindedToactivity = true;
        super.onRebind(intent);

    }

    @Override
    public boolean onUnbind(Intent intent) {
        misBindedToactivity = false;
        return true;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Log.e("service created...","####");
        customerLocation = AppPreference.newInstance(this).getCustomerLocation();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (client == null && webSocket == null && listener == null) {
            initWebsocket();
        }
        return START_STICKY;
    }

    private okhttp3.Request request;

    private void initWebsocket() {
        //if (client == null)
            client = new OkHttpClient().newBuilder().pingInterval(30, TimeUnit.SECONDS).build();
        //Request request = new Request.Builder().url("ws://echo.websocket.org").build();
        //TODO add the IP and port
        //if (request == null)
            request = new okhttp3.Request.Builder().url(ApiConfig.URL_SOCKET).build();

        //okhttp3.Request request= new okhttp3.Request.Builder().url(ApiConfig.URL_SOCKET_LOCAL).build();
       // if (listener == null)
            listener = new EchoWebSocketListener();
        webSocket = client.newWebSocket(request, listener);


        // Trigger shutdown of the dispatcher's executor so this process can
        // exit cleanly.
        //client.dispatcher().executorService().shutdown();
    }


    public final class EchoWebSocketListener extends WebSocketListener {
        public static final int NORMAL_CLOSURE_STATUS = 10000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            // Log.e("Socket........:","connected : response is"+response.message());
            // System.out.println("Socket...: " + "connected : response is"+response.message());
            //webSocket.send("Hello!");
            //webSocket.send(ByteString.decodeHex("deadbeef"));
           // webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye!");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            Log.e("Receiving: ", text);
            try {
                JSONObject newObject = new JSONObject(text);
                double lat = Double.valueOf(newObject.getString("lat"));
                double lon = Double.valueOf(newObject.getString("long"));
                String driver_id = newObject.getString("driver_id");
                String vehicle_type_id = newObject.getString("vehicle_type_id");
                String driver_status=newObject.getString("driver_status");
                String service_id=null;

                // check if socket json have service id or not
                if(newObject.has("current_running_service")){
                    service_id=newObject.getString("current_running_service");
                }


                //Log.e("lat.....",lat+"");
                // Log.e("long....",lon+"");

                if (driver_id != null && !TextUtils.isEmpty(driver_id) && misBindedToactivity) {

                    Location driverLocation = new Location("B");
                    driverLocation.setLatitude(lat);
                    driverLocation.setLongitude(lon);
                    Log.e("dataReceived####",driver_id+" "+driverLocation.getLatitude() +" "+driverLocation.getLongitude()+" "+ vehicle_type_id+" "+driver_status+" "+service_id);
                    new CalculateDistanceBetweenTwoLoation(driverLocation, driver_id, vehicle_type_id,driver_status,service_id).execute();

                }

            } catch (JSONException e) {
                //System.out.println("Exception: " + e.getMessage());
                Log.e("Exception....", e.getMessage());

            } catch (NumberFormatException e) {
                //System.out.println("Exception: " + e.getMessage());
                //Log.e("Exception....",e.getMessage());
                e.printStackTrace();
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            //Log.e("Receiving####",bytes.hex());
            // System.out.println("Receiving: " + bytes.hex());
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            //Log.e("Closing...socket..",code+ " "+reason);
            Log.e("Close", "11");
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            // Log.e("Closing...socket..",code+ " "+reason);
            //System.out.println("Closing: " + code + " " + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            Log.e("Failure...socket..", "onFailure.......");

            initWebsocket();
            //webSocket.request().newBuilder().build();
            t.printStackTrace();
            // webSocket.request();
           /* webSocket.close(NORMAL_CLOSURE_STATUS, null);
            initWebsocket(); */// calling again
           /* if (serviceCallbacksListner != null)
                serviceCallbacksListner.stopDriveUpdateService();*/
        }

    }


    private double distanceInMtrs(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist * 1000);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    class CalculateDistanceBetweenTwoLoation extends AsyncTask<String, String, String> {
        Location driverLocation;
        String driver_id;
        String vechile_type_id;
        String driverStaus;
        String serviceId;
        double distance;

        CalculateDistanceBetweenTwoLoation(Location location2, String driver_id, String vechile_type_id, String driver_staus, String service_id) {

            this.driverLocation = location2;
            this.driver_id = driver_id;
            this.vechile_type_id = vechile_type_id;
            this.driverStaus=driver_staus;
            this.serviceId=service_id;
        }

        @Override
        protected String doInBackground(String... strings) {
            distance = distanceInMtrs(customerLocation.getLatitude(), customerLocation.getLongitude(), driverLocation.getLatitude(), driverLocation.getLongitude());

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(driverStaus.equals("active")){

                if (serviceCallbacksListner != null) {
                    Log.e("dataReceived####", driver_id + " " + driverLocation.getLatitude() + " " + driverLocation.getLongitude() + " " + vechile_type_id + " " + driverStaus + " " + serviceId);
                    serviceCallbacksListner.onDataRecieved(driver_id, driverLocation.getLatitude(), driverLocation.getLongitude(), vechile_type_id, driverStaus, serviceId);
                  //  distance = 0.0;
                } else {

                }

            }else {


                if (distance != 0.0f && distance <= DEFAULT_DISTANCE) {
                    Log.e("distance less then default distance.####..", distance + "");
                    if (serviceCallbacksListner != null) {
                        Log.e("dataReceived####", driver_id + " " + driverLocation.getLatitude() + " " + driverLocation.getLongitude() + " " + vechile_type_id + " " + driverStaus + " " + serviceId);
                        serviceCallbacksListner.onDataRecieved(driver_id, driverLocation.getLatitude(), driverLocation.getLongitude(), vechile_type_id, driverStaus, serviceId);
                        distance = 0.0;
                    } else {

                    }
                } else if (driverLocation.getLatitude() == 0.0 && driverLocation.getLongitude() == 0.0) {

                    if (serviceCallbacksListner != null) {
                        serviceCallbacksListner.onDataRecieved(driver_id, driverLocation.getLatitude(), driverLocation.getLongitude(), vechile_type_id, driverStaus, serviceId);
                        distance = 0.0;
                    }
                }else if(driverLocation.getLatitude()!=0.0 && driverLocation.getLongitude()!=0.0 && driverStaus.equals("available")){
                    //Log.e("dataReceived####", driver_id + " " + driverLocation.getLatitude() + " " + driverLocation.getLongitude() + " " + vechile_type_id + " " + driverStaus + " " + serviceId);
                    serviceCallbacksListner.onDataRecieved(driver_id, driverLocation.getLatitude(), driverLocation.getLongitude(), vechile_type_id, driverStaus, serviceId);
                }
            }
        }
    }
}
