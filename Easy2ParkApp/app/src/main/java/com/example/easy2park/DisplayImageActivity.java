package com.example.easy2park;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    private int sendMessagesInterval = 1000;

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

        Log.d("AzureServ", "In onStartCommand");
        //temperature = getIntent().getStringExtra("temp");
        //devID = getIntent().getStringExtra("devID");
        temperature = "19";
        devID = "1235446";
        start();

    }

    public void start(){
        sendThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    initClient();
                    for(int j = 0; j < 50; j++) {
                        sendMessages();
                        Thread.sleep(sendMessagesInterval);
                    }
                } catch (InterruptedException e) {
                    return;
                } catch (Exception e) {
                    lastException = "Exception while opening IoTHub connection: " + e;
                    handler.post(exceptionRunnable);
                }
            }
        });
        sendThread.start();
    }

    private void sendMessages(){
        JSONObject json = new JSONObject();

        try {
            json.put("devID", devID);
            json.put("temperature", temperature);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        msgStr = json.toString();

        try {
            sendMessage = new Message(msgStr);
            Log.d("asd", "Sending message " + msgStr);
            //sendMessage.setProperty("temperatureAlert", temperature > 28 ? "true" : "false");
            System.out.println("Message sent: " + msgStr);
            EventCallback eventCallback = new EventCallback();
            client.sendEventAsync(sendMessage, eventCallback, msgSentCount);
            msgSentCount++;
        } catch (Exception e) {
            System.err.println("Exception while sending event: " + e);
        }
    }

    final Runnable methodNotificationRunnable = new Runnable(){
        @Override
        public void run() {
            Context context = getApplicationContext();
            CharSequence text = "Set Send Messages Interval to " + sendMessagesInterval + "ms";
            // Toast is a small popup in which there are some informations
        }
    };

    private void initClient() throws URISyntaxException, IOException {
        client = new DeviceClient(connString, protocol);
        try {
            Log.d("asd","Before IotHubConnectionChange...");
            client.registerConnectionStatusChangeCallback(new AzureService.IotHubConnectionStatusChangeCallbackLogger(), new Object());
            Log.d("asd","After IotHubConnectionChange...");
            client.open();
            MessageCallback callback = new MessageCallback();
            client.setMessageCallback(callback, null);
            client.subscribeToDeviceMethod(new SampleDeviceMethodCallback(), getApplicationContext(), new DeviceMethodStatusCallback(), null);
        } catch (Exception e){
            System.err.println("Exception while opening IoTHub connection: " + e);
            client.closeNow();
            System.out.println("Shutting down...");
        }
    }

    protected static class IotHubConnectionStatusChangeCallbackLogger implements IotHubConnectionStatusChangeCallback {
        @Override
        public void execute(IotHubConnectionStatus status, IotHubConnectionStatusChangeReason statusChangeReason, Throwable throwable, Object callbackContext) {
            Log.d("asd","In IotHubConnectionChange...");
            System.out.println();
            System.out.println("CONNECTION STATUS UPDATE: " + status);
            System.out.println("CONNECTION STATUS REASON: " + statusChangeReason);
            System.out.println("CONNECTION STATUS THROWABLE: " + (throwable == null ? "null" : throwable.getMessage()));
            System.out.println();

            if (throwable != null) {
                throwable.printStackTrace();
            }

            if (status == IotHubConnectionStatus.DISCONNECTED) {
                //connection was lost, and is not being re-established. Look at provided exception for
                // how to resolve this issue. Cannot send messages until this issue is resolved, and you manually
                // re-open the device client
            }
            else if (status == IotHubConnectionStatus.DISCONNECTED_RETRYING) {
                //connection was lost, but is being re-established. Can still send messages, but they won't
                // be sent until the connection is re-established
            }
            else if (status == IotHubConnectionStatus.CONNECTED) {
                //Connection was successfully re-established. Can send messages.
            }
        }
    }

    protected class SampleDeviceMethodCallback implements com.microsoft.azure.sdk.iot.device.DeviceTwin.DeviceMethodCallback {
        @Override
        public DeviceMethodData call(String methodName, Object methodData, Object context) {
            DeviceMethodData deviceMethodData ;
            try {
                switch (methodName) {
                    case "setSendMessagesInterval": {
                        int status = method_setSendMessagesInterval(methodData);
                        deviceMethodData = new DeviceMethodData(status, "executed " + methodName);
                        break;
                    }
                    default: {
                        int status = method_default(methodData);
                        deviceMethodData = new DeviceMethodData(status, "executed " + methodName);
                    }
                }
            }
            catch (Exception e) {
                int status = METHOD_THROWS;
                deviceMethodData = new DeviceMethodData(status, "Method Throws " + methodName);
            }
            return deviceMethodData;
        }
    }

    private int method_setSendMessagesInterval(Object methodData) throws UnsupportedEncodingException, JSONException {
        String payload = new String ((byte[])methodData, "UTF-8").replace("\"", "");
        JSONObject obj = new JSONObject(payload);
        sendMessagesInterval = obj.getInt("sendInterval");
        handler.post(methodNotificationRunnable);
        return METHOD_SUCCESS;
    }

    private int method_default(Object data){
        System.out.println("Invoking default method for this device");
        // Insert device specific code here
        return METHOD_NOT_DEFINED;
    }


    final Runnable exceptionRunnable = new Runnable() {
        @Override
        public void run() {
            System.out.println("EXCEPTION RUNNABLE");
        }
    };

    class EventCallback implements IotHubEventCallback { // Received confirm message from IoT Hub
        public void execute(IotHubStatusCode status, Object context) {
            Integer i = context instanceof Integer ? (Integer) context : 0;
            System.out.println("IoT Hub responded to message " + i.toString()
                    + " with status " + status.name());

            if((status == IotHubStatusCode.OK) || (status == IotHubStatusCode.OK_EMPTY)) { // OK response
                receiptsConfirmedCount++;
                System.out.println("Response ok");
            }
            else { // Failed response
                sendFailuresCount++;
                System.out.println("Response no");
            }
        }
    }

    class MessageCallback implements com.microsoft.azure.sdk.iot.device.MessageCallback {

        @Override
        public IotHubMessageResult execute(Message msg, Object callbackContext) {
            String s = msgStr + Message.DEFAULT_IOTHUB_MESSAGE_CHARSET;
            System.out.println("Received message with content: " + s);
            msgReceivedCount++;
            return IotHubMessageResult.COMPLETE;
        }
    }

    protected class DeviceMethodStatusCallback implements IotHubEventCallback {

        @Override
        public void execute(IotHubStatusCode responseStatus, Object callbackContext) {
            System.out.println("IoT Hub responded to device method operation with status " + responseStatus.name());
        }
    }

}
