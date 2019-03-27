package com.example.easy2park;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sensoro.beacon.kit.Beacon;
import com.sensoro.beacon.kit.BeaconManagerListener;
import com.sensoro.cloud.SensoroManager;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainActivity extends FragmentActivity {

    RelativeLayout containLayout;
    BeaconsFragment beaconsFragment;
    DetailFragment detailFragment;
    DistanceFragment distanceFragment;
    RangeFragment rangeFragment;
    TemperatureFragment temperatureFragment;
    LightFragment lightFragment;
    MoveFragment moveFragment;
    NotificationFragment notificationFragment;

    ActionBar actionBar;
    LayoutInflater inflater;
    RelativeLayout actionBarMainLayout;
    RelativeLayout actionBarLayout;
    TTFIcon freshIcon;
    TTFIcon infoIcon;
    TextView actionBarTitle;

    NotificationManager notificationManager;
    public static final int NOTIFICATION_ID = 0;
    SharedPreferences sharedPreferences;

    MyApp app;

    BeaconManagerListener beaconManagerListener;
    /*
     * Sensoro Manager
     */
    SensoroManager sensoroManager;
    /*
     * store beacons in onUpdateBeacon
     */
    CopyOnWriteArrayList<Beacon> beacons;
    String beaconFilter;
    String matchFormat;
    Handler handler = new Handler();
    Runnable runnable;

    BluetoothManager bluetoothManager;
    BluetoothAdapter bluetoothAdapter;
    ArrayList<OnBeaconChangeListener> beaconListeners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void initCtrl() {

        app = (MyApp) getApplication();

        sensoroManager = app.sensoroManager;
        beacons = new CopyOnWriteArrayList<Beacon>();
        beaconListeners = new ArrayList<OnBeaconChangeListener>();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        sharedPreferences = getPreferences(Activity.MODE_PRIVATE);

    }

    private boolean isBlueEnable() {
        bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
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

    private void initSensoroListener() {
        beaconManagerListener = new BeaconManagerListener() {

            @Override
            public void onUpdateBeacon(final ArrayList<Beacon> arg0) {
                /*
                 * beacons has bean scaned in this scanning period.
                 */
                if (beaconsFragment == null) {
                    beaconsFragment = (BeaconsFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAG_BEACONS);
                }
                if (beaconsFragment == null) {
                    return;
                }
                /*
                 * beaconsFragment.isVisible()
                 */
                if (beaconsFragment.isVisible()) {
                    /*
                     * Add the update beacons into the grid.
                     */
                    for (Beacon beacon : arg0) {
                        if (beacons.contains(beacon)) {
                            continue;
                        }
                        /*
                         * filter
                         */

                        if (TextUtils.isEmpty(beaconFilter)) {
                            beacons.add(beacon);
                        } else {
                            String matchString = String.format(matchFormat, beacon.getSerialNumber(), beacon.getMajor(), beacon.getMinor());
                            if (matchString.contains(beaconFilter)) {
                                beacons.add(beacon);
                            }
                        }
                    }
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        for (OnBeaconChangeListener listener : beaconListeners) {
                            if (listener == null) {
                                continue;
                            }
                            listener.onBeaconChange(arg0);
                        }
                    }
                });

            }

            @Override
            public void onNewBeacon(Beacon arg0) {
                /*
                 * A new beacon appears.
                 */
                String key = getKey(arg0);
                boolean state = sharedPreferences.getBoolean(key, false);
                if (state) {
                    /*
                     * show notification
                     */

                    showNotification(arg0, true);
                }

            }

            @Override
            public void onGoneBeacon(Beacon arg0) {
                /*
                 * A beacon disappears.
                 */
                String key = getKey(arg0);
                boolean state = sharedPreferences.getBoolean(key, false);
                if (state) {
                    /*
                     * show notification
                     */

                    showNotification(arg0, false);
                }
            }
        };
    }

}
