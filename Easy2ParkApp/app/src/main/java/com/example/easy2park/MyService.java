package com.example.easy2park;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;

import com.microsoft.azure.sdk.iot.device.DeviceClient;
import com.microsoft.azure.sdk.iot.device.IotHubClientProtocol;
import com.microsoft.azure.sdk.iot.device.Message;

/**
 * Created by Sensoro on 15/4/9.
 */
public class MyService extends Service{
    private static final String TAG = MyService.class.getSimpleName();

    private MyApp application;
    private BluetoothBroadcastReceiver bluetoothBroadcastReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        application = (MyApp) getApplication();
        bluetoothBroadcastReceiver = new BluetoothBroadcastReceiver();
        registerReceiver(bluetoothBroadcastReceiver, new IntentFilter(Constant.BLE_STATE_CHANGED_ACTION));
        if (application.isBlueEnabled()){
            application.initSensoro();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bluetoothBroadcastReceiver);
    }

    class BluetoothBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constant.BLE_STATE_CHANGED_ACTION)){
                if (application.isBlueEnabled()){
                    application.startSensoroSDK();
                }
            }
        }
    }
}
