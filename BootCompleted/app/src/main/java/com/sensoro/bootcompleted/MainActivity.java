package com.sensoro.bootcompleted;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 10000;

    private MyApplication application;
    private BluetoothBroadcastReceiver bluetoothBroadcastReceiver;

    private int request=0, max_request=99;

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 456;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        application = (MyApplication) getApplication();
        bluetoothBroadcastReceiver = new BluetoothBroadcastReceiver();
        registerReceiver(bluetoothBroadcastReceiver,new IntentFilter(Constant.BLE_STATE_CHANGED_ACTION));
        Log.d("asd", "In MainActivity, starting");

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null){
            application.startSensoroSDK();
        } else {
            Log.d("asd", "In MainActivity, activation bluetooth");
            /**
             * Enable bluetooth by user permission.
             */
//            Intent bluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(bluetoothIntent, REQUEST_ENABLE_BT);

            /**
             * Enable bluetooth in background.
             */
            if(!mBluetoothAdapter.isEnabled()) {
                enableBluetoothPermission();
            }
        }
    }

    private void enableBluetoothPermission() {
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            //shouldShowRequestPermissionRationale() = If this function is called on pre-M, it will always return false.
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                return;
            }
            //If this function is called on pre-M, OnRequestPermissionsResultCallback will be suddenly called with correct PERMISSION_GRANTED or PERMISSION_DENIED result.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
            }
            return;
        }
        //once i have got the ACCESS_COARSE_LOCATION i can check if the blt is turns on.
        enableBLT();
    }

    //return from  enableBluetoothPermission()
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Log.d("asd","PERMESSO DATO!!!");
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "PERMISSION_GRANTED Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK){
            application.startSensoroSDK();
        }
    }

    private void enableBLT(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bluetoothBroadcastReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class BluetoothBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constant.BLE_STATE_CHANGED_ACTION)){
                if (application.isBluetoothEnabled()){
                    application.startSensoroSDK();
                }
            }
        }
    }

}
