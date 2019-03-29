package com.sensoro.bootcompleted;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class DisplayImageActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

        String map_id= getIntent().getStringExtra("map_id");
        if (map_id!= null )Log.d("map_id_received",map_id);
        ImageView imageView;
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
    }

    /*  //function to distinguish maps
    public void displayImage(){

        ImageView imageView;

        ImageView map_img=(ImageView) findViewById(R.id.map_img);
        int imgResource= getResources().getIdentifier("@drawable/park_map",null,this.getPackageName());
        map_img.setImageResource(imgResource);

    }
    */
}
