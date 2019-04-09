package com.example.easy2park;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import mehdi.sakout.fancybuttons.FancyButton;

public class DisplayImageActivity extends AppCompatActivity {

    FancyButton btnStart;
    FancyButton btnStop;

    Intent azure_intent;
    String temperature;
    String devID;
    String beaconID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        registerReceiver(mMessageReceiver, new IntentFilter("FinishDisplayImage"));
        setContentView(R.layout.activity_display_image);

        btnStart = findViewById(R.id.sendBtn);
        btnStop = findViewById(R.id.stopBtn);


        Log.d("asd", "In dispActiv");
        String map_id = getIntent().getStringExtra("map_id");

        if (map_id!= null ) Log.d("map_id_received",map_id);

        ImageView map_img = (ImageView) findViewById(R.id.map_img);
        int imgResource;


        if(map_id.equals("general_map")){
            imgResource = getResources().getIdentifier("@drawable/park_map",null, this.getPackageName());
        }
        else if(map_id.equals("area_one_map")){
            imgResource = getResources().getIdentifier("@drawable/area1",null, this.getPackageName());
        }
        else if(map_id.equals("area_two_map")){
            imgResource = getResources().getIdentifier("@drawable/area2",null,this.getPackageName());
        }
        else{
            imgResource = getResources().getIdentifier("@drawable/error",null,this.getPackageName());

            Log.d("received", map_id == null ? "null" : map_id);
        }
        map_img.setImageResource(imgResource);

        String temp = getIntent().getStringExtra("temp");
        TextView tempText = (TextView) findViewById(R.id.displayTempValue);
        tempText.setText(temp == null ? "None" : temp+" °C");

        temperature = getIntent().getStringExtra("temp");
        devID = getIntent().getStringExtra("devID");
        beaconID = getIntent().getStringExtra("beaconID");

        azure_intent = new Intent();
        azure_intent.setClass(this, AzureService.class);

        azure_intent.putExtra("temp", temperature);
        azure_intent.putExtra("devID", devID);
        azure_intent.putExtra("beaconID", beaconID);


        if(getIntent().getStringExtra("exit") != null && getIntent().getStringExtra("exit").equals("EXIT")){
            Log.d("asd", "in finish");
            finish();
        }
        btnStop.setEnabled(false);

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMessageReceiver);
    }

    public void btnStartOnClick(View v){
        /**
         * TODO
         * Start Azure
         */

        btnStart.setEnabled(false);
        btnStop.setEnabled(true);
        startService(azure_intent);
    }

    public void btnStopOnClick(View v){
        /**
         * TODO
         * Stop Azure
         */

        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        stopService(azure_intent);
    }

}
