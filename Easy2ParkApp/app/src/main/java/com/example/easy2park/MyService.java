package com.example.easy2park;

import android.app.AlertDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

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
        Log.d("asd", "In myService");
        //bluetoothBroadcastReceiver = new BluetoothBroadcastReceiver();
        //registerReceiver(bluetoothBroadcastReceiver, new IntentFilter(Constant.BLE_STATE_CHANGED_ACTION));
        if (isBlueEnabled()){
            application.initSensoro();
        }
    }

    public boolean isBlueEnabled() {
        Log.d("asd", "Checking bluetooth in MyApp");
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        boolean status = bluetoothAdapter.isEnabled();
        Log.d("asd", "Status: " + status);
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
