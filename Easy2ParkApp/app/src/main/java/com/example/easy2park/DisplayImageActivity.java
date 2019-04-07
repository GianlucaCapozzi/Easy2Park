package com.example.easy2park;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

public class DisplayImageActivity extends AppCompatActivity {

    /* AZURE VARIABLES */

    private final String connString = BuildConfig.DeviceConnectionString;


    IotHubClientProtocol protocol = IotHubClientProtocol.MQTT;

    private int msgSentCount = 0;
    private int msgReceivedCount = 0;
    private int msgReceived = 0;
    private int receiptsConfirmedCount = 0;
    private int sendFailuresCount = 0;

    private final Handler handler = new Handler();
    private Thread sendThread;

    private DeviceClient client;

    private String msgStr;
    private Message sendMessage;
    private String lastException;
    private int sendMessagesInterval = 5000;

    private String temperature;
    private String devID;

    private static final int METHOD_SUCCESS = 200;
    private static final int METHOD_THROWS = 403;
    private static final int METHOD_NOT_DEFINED = 404;

    /* END AZURE VARIABLES */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        Log.d("asd", "In dispActiv");
        String map_id= getIntent().getStringExtra("map_id");
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

            Log.d("received",map_id==null?"null":map_id);
        }
        map_img.setImageResource(imgResource);

        String temp = getIntent().getStringExtra("temp");
        TextView tempText=(TextView) findViewById(R.id.displayTempValue);
        tempText.setText(temp==null?"None":temp+" °C");

    }

}
