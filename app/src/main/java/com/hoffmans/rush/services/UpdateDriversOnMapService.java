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

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class UpdateDriversOnMapService extends Service {
    private MyBinder binder = new MyBinder();
    private int DEFAULT_DISTANCE =1000000;  //distance in meters
    private ServiceCallbacks serviceCallbacksListner;
    private EchoWebSocketListener listener;
    private WebSocket webSocket;
    private boolean misBindedToactivity=false;
    private okhttp3.OkHttpClient client;
    private Location customerLocation;


    public UpdateDriversOnMapService() {
    }


    public class MyBinder extends Binder {
        public UpdateDriversOnMapService getService() {
            return UpdateDriversOnMapService.this;
        }
    }

    public void setServiceCallBack(ServiceCallbacks listener){
        serviceCallbacksListner=listener;
    }
    @Override
    public IBinder onBind(Intent intent) {
        misBindedToactivity=true;
        return binder;
    }


    @Override
    public void onRebind(Intent intent) {
        misBindedToactivity=true;
        super.onRebind(intent);

    }

    @Override
    public boolean onUnbind(Intent intent) {
       misBindedToactivity=false;
        return true;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        customerLocation= AppPreference.newInstance(this).getCustomerLocation();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(client==null && webSocket==null && listener==null){
            initWebsocket();
        }
        return START_STICKY;
    }


    private void initWebsocket() {
        client = new okhttp3.OkHttpClient();
        //Request request = new Request.Builder().url("ws://echo.websocket.org").build();
        //TODO add the IP and port
        okhttp3.Request request= new okhttp3.Request.Builder().url(ApiConfig.URL_SOCKET).build();
        listener = new EchoWebSocketListener();
        webSocket= client.newWebSocket(request, listener);

    }


    public final class EchoWebSocketListener extends WebSocketListener {
        public static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            System.out.println("Socket...: " + "connected : response is"+response.message());
            //webSocket.send("Hello!");
            //webSocket.send(ByteString.decodeHex("deadbeef"));
            //webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye!");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            System.out.println("Receiving: " + text);
            try{
                JSONObject newObject=new JSONObject(text);
                 double lat=Double.valueOf(newObject.getString("lat"));
                 double lon=Double.valueOf(newObject.getString("long"));
                 String driver_id=newObject.getString("driver_id");
                 if(driver_id!=null && !TextUtils.isEmpty(driver_id) && misBindedToactivity){
                     Location driverLocation=new Location("B");
                     driverLocation.setLatitude(lat);
                     driverLocation.setLongitude(lon);
                     new CalculateDistanceBetweenTwoLoation(driverLocation,driver_id).execute();

                 }

            }catch (JSONException e){
                System.out.println("Exception: " + e.getMessage());
            }catch (NumberFormatException e){
                System.out.println("Exception: " + e.getMessage());
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            System.out.println("Receiving: " + bytes.hex());
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            System.out.println("Closing: " + code + " " + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            t.printStackTrace();
        }

    }

    class CalculateDistanceBetweenTwoLoation extends AsyncTask<String,String,String>{
        Location driverLocation;
        String driver_id;
        float distance;
        CalculateDistanceBetweenTwoLoation(Location location2,String driver_id){

            this.driverLocation=location2;
            this.driver_id=driver_id;
        }
        @Override
        protected String doInBackground(String... strings) {
            distance=customerLocation.distanceTo(driverLocation);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(distance!=0.0f && distance<DEFAULT_DISTANCE){
                Log.e("distance",distance+"");
                if(serviceCallbacksListner!=null){
                    serviceCallbacksListner.onDataRecieved(driver_id,driverLocation.getLatitude(),driverLocation.getLongitude());
                    distance=0.0f;
                }
            }else if(driverLocation.getLatitude()==0.0 && driverLocation.getLongitude()==0.0){
                if(serviceCallbacksListner!=null){
                    serviceCallbacksListner.onDataRecieved(driver_id,driverLocation.getLatitude(),driverLocation.getLongitude());
                    distance=0.0f;
                }
            }
        }
    }
}
