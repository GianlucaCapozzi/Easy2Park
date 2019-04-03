package com.sensoro.bootcompleted;

import android.app.Application;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ImageView;

import com.sensoro.beacon.kit.Beacon;
import com.sensoro.beacon.kit.BeaconManagerListener;
import com.sensoro.beacon.kit.SensoroBeaconManager;
import com.sensoro.cloud.SensoroManager;

import java.util.ArrayList;

/**
 * Created by Sensoro on 15/3/11.
 */
public class MyApplication extends Application implements BeaconManagerListener{
    private static final String TAG = MyApplication.class.getSimpleName();
    private SensoroManager sensoroManager;

    @Override
    public void onCreate() {
        super.onCreate();

        initSensoroSDK();

        /**
         * Start SDK in Service.
         */
        Intent intent = new Intent();
        intent.setClass(this,MyService.class);
        startService(intent);

    }

    /**
     * Initial Sensoro SDK.
     */
    private void initSensoroSDK() {
        sensoroManager = SensoroManager.getInstance(getApplicationContext());
        sensoroManager.setCloudServiceEnable(false);
        sensoroManager.addBroadcastKey("7b4b5ff594fdaf8f9fc7f2b494e400016f461205");
        sensoroManager.setBeaconManagerListener(this);
    }

    /**
     * Start Sensoro SDK.
     */
    public void startSensoroSDK() {
        try {
            sensoroManager.startService();
        } catch (Exception e) {
            Log.v("ERROR","NOT STARTED sensoro sdkß");
            e.printStackTrace();
        }
    }

    /**
     * Check whether bluetooth enabled.
     * @return
     */
    public boolean isBluetoothEnabled(){
        return sensoroManager.isBluetoothEnabled();
    }

    @Override
    public void onNewBeacon(Beacon beacon) {
        /**
         * Check whether SDK started in logs.
         */
        Log.v("BLUE-IN","IN " + beacon.getSerialNumber());
        //start image display activity
        //MainActivity.displayImage();
            Boolean RECOGNIZED=true;
            // first parameter is the context, second is the class of the activity to launch
            Intent i = new Intent(this, DisplayImageActivity.class);
            // put "extras" into the bundle for access in the second activity
            if(beacon.getSerialNumber().equals("0117C59B4EC7") ){
                i.putExtra("map_id", "general_map");
            }
            else if(beacon.getSerialNumber().equals("0117C582CAD7")){
                i.putExtra("map_id", "area_one_map");
            }
            else if(beacon.getSerialNumber().equals("0117C5578442")){
                i.putExtra("map_id", "area_two_map");
            }
            else{
                RECOGNIZED=false;
            }
            // brings up the second activity
            if(RECOGNIZED) {
                i.putExtra("temp",beacon.getTemperature().toString());
                startActivity(i);
            }


    }

    @Override
    public void onGoneBeacon(Beacon beacon) {
        Log.v("BLUE-OUT","OUT"+ beacon.getSerialNumber());

    }

    @Override
    public void onUpdateBeacon(ArrayList<Beacon> arrayList) {

        for(Beacon b: arrayList) {

            Log.v("BLUE-UPDATE", "UPDATE " + b.getSerialNumber() + "----"+ b);
        }
    }
}
