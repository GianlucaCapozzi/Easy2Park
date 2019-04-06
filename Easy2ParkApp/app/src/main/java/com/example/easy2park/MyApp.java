package com.example.easy2park;

import android.app.AlertDialog;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.sensoro.cloud.SensoroManager;
import com.sensoro.beacon.kit.*;

import java.util.ArrayList;

public class MyApp extends Application implements BeaconManagerListener{

    private static final String TAG = MyApp.class.getSimpleName();
    SensoroManager sensoroManager;

    private int temperature;
    private String devID;

    @Override
    public void onCreate() {
        super.onCreate();
        initSensoro();
        /**
         * Start SDK in Service
         */
        Intent intent = new Intent();
        intent.setClass(this,MyService.class);
        //startService(intent);
    }

    private void initSensoro() {
        sensoroManager = SensoroManager.getInstance(getApplicationContext());
        sensoroManager.addBroadcastKey("7b4b5ff594fdaf8f9fc7f2b494e400016f461205");
        sensoroManager.setBeaconManagerListener(this);
        try {
            sensoroManager.startService();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                sensoroManager.setForegroundScanPeriod(7000);
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public boolean isBlueEnabled() {
        Log.d("asd", "Checking bluetooth in MyApp");
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        boolean status = bluetoothAdapter.isEnabled();
        if (!status) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE); // request to active blue if is off
                    startActivity(intent);
                }
            }).setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setTitle(R.string.ask_bt_open);
            builder.show();
        }
        return status;
    }


    /**
     * Start Sensoro SDK.
     */
    public void startSensoroSDK() {
        try {
            sensoroManager.startService();
        } catch (Exception e) {
            Log.d("ERROR","NOT STARTED sensoro sdk");
            e.printStackTrace();
        }
    }

    @Override
    public void onTerminate() {
        if (sensoroManager != null) {
            sensoroManager.stopService();
        }
        super.onTerminate();
    }

    @Override
    public void onNewBeacon(Beacon beacon) {
        /**
         * Check whether SDK started in logs.
         */
        Log.d("BLUE-IN","IN " + beacon.getSerialNumber());
        //start image display activity
        //MainActivity.displayImage();
        Boolean RECOGNIZED=true;
        // first parameter is the context, second is the class of the activity to launch
        Intent i = new Intent(this, DisplayImageActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Intent azure_intent = new Intent();
        azure_intent.setClass(this, AzureService.class);

        // put "extras" into the bundle for access in the second activity
        if(beacon.getSerialNumber().equals("0117C59B4EC7") ){
            Log.d("asd", "BEACON PRO");
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

            temperature = beacon.getTemperature();
            i.putExtra("temp",beacon.getTemperature().toString());
            i.putExtra("devID", devID = "123341516");

            temperature = beacon.getTemperature();

            azure_intent.putExtra("temp", beacon.getTemperature().toString());
            azure_intent.putExtra("devID", devID = "123341516");


            if(beacon.getTemperature()!=null){
                //TODO send to iothub temperature
            }

            //Log.d("asd", "Starting azure service");
            //startService(azure_intent);

            Log.d("asd", "Starting map activity");
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
