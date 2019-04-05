package com.sensoro.bootcompleted;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.microsoft.azure.sdk.iot.device.DeviceClient;
import com.microsoft.azure.sdk.iot.device.DeviceTwin.DeviceMethodData;
import com.microsoft.azure.sdk.iot.device.IotHubClientProtocol;
import com.microsoft.azure.sdk.iot.device.IotHubConnectionStatusChangeCallback;
import com.microsoft.azure.sdk.iot.device.IotHubConnectionStatusChangeReason;
import com.microsoft.azure.sdk.iot.device.IotHubEventCallback;
import com.microsoft.azure.sdk.iot.device.IotHubMessageResult;
import com.microsoft.azure.sdk.iot.device.IotHubStatusCode;
import com.microsoft.azure.sdk.iot.device.Message;
import com.microsoft.azure.sdk.iot.device.transport.IotHubConnectionStatus;
import com.sensoro.beacon.kit.Beacon;
import com.sensoro.beacon.kit.BeaconManagerListener;
import com.sensoro.cloud.SensoroManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by Sensoro on 15/3/11.
 */
public class MyApplication extends Application implements BeaconManagerListener{
    private static final String TAG = MyApplication.class.getSimpleName();
    private SensoroManager sensoroManager;

    private final String connString = BuildConfig.DeviceConnectionString;;

    IotHubClientProtocol protocol = IotHubClientProtocol.MQTT;



    private int msgSentCount = 0;
    private int msgReceivedCount = 0;
    private int msgReceived = 0;

    private final Handler handler = new Handler();
    private Thread sendThread;

    private DeviceClient client;

    private String msgStr;
    private Message sendMessage;
    private String lastException;
    private int sendMessagesInterval = 5000;

    private int temperature;
    private String devID;

    private static final int METHOD_SUCCESS = 200;
    private static final int METHOD_THROWS = 403;
    private static final int METHOD_NOT_DEFINED = 404;

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
            Log.d("ERROR","NOT STARTED sensoro sdk");
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
        Log.d("BLUE-IN","IN " + beacon.getSerialNumber());
        //start image display activity
        //MainActivity.displayImage();
            Boolean RECOGNIZED=true;
            // first parameter is the context, second is the class of the activity to launch
            Intent i = new Intent(this, DisplayImageActivity.class);

            Intent azure_intent = new Intent();


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

                temperature = beacon.getTemperature();
                i.putExtra("temp",beacon.getTemperature().toString());

                temperature = beacon.getTemperature();

                azure_intent.putExtra("temp", beacon.getTemperature().toString());
                azure_intent.putExtra("devID", devID = "123341516");


                if(beacon.getTemperature()!=null){
                    //TODO send to iothub temperature
                }
                Log.d("asd", "Starting azure service");
                startService(azure_intent);

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
