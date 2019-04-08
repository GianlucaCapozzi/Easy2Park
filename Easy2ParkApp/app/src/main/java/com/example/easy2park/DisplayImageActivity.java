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
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayImageActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver(mMessageReceiver, new IntentFilter("FinishDisplayImage"));
        setContentView(R.layout.activity_display_image);
        Log.d("asd", "In dispActiv");
        String map_id = getIntent().getStringExtra("map_id");
        if (map_id!= null ) Log.d("map_id_received",map_id);
        ImageView map_img=(ImageView) findViewById(R.id.map_img);
        int imgResource;
        if(map_id.equals("general_map")){
            imgResource = getResources().getIdentifier("@drawable/park_map",null,this.getPackageName());
        }
        else if(map_id.equals("area_one_map")){
            imgResource = getResources().getIdentifier("@drawable/area1",null,this.getPackageName());
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

        if(getIntent().getStringExtra("exit") != null && getIntent().getStringExtra("exit").equals("EXIT")){
            Log.d("asd", "in finish");
            finish();
        }

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
}
