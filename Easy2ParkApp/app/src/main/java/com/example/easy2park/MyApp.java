package com.example.easy2park;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import com.sensoro.cloud.SensoroManager;

public class MyApp extends Application {

    private static final String TAG = MyApp.class.getSimpleName();
    SensoroManager sensoroManager;

    @Override
    public void onCreate() {
        initSensoro();
        super.onCreate();
    }

    private void initSensoro() {
        sensoroManager = SensoroManager.getInstance(getApplicationContext());
        try {
            sensoroManager.startService();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                sensoroManager.setForegroundScanPeriod(7000);
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void onTerminate() {
        if (sensoroManager != null) {
            sensoroManager.stopService();
        }
        super.onTerminate();
    }

}
