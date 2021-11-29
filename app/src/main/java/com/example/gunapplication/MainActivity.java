package com.example.gunapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView headerTitle;
    ImageView imageView;

    MediaPlayer mp_reload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Title*/
        headerTitle = (TextView) findViewById(R.id.header_title);
        headerTitle.setText("Fegyver applikáció");
        /*Gun image*/
        imageView = (ImageView) findViewById(R.id.imageView);
        /*reload sound*/
        mp_reload = MediaPlayer.create(getApplicationContext(), R.raw.gun_reload);


        /* Light sensor */
        SensorManager mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        Sensor lightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(lightSensor != null){
            System.out.println("Sensor.TYPE_LIGHT Available");
            mySensorManager.registerListener(
                    lightSensorListener,
                    lightSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);

        } else {
            System.out.println("Sensor.TYPE_LIGHT NOT Available");
        }
        /* light sensor end */



    }
    /*light sensor */
    private final SensorEventListener lightSensorListener  = new SensorEventListener(){
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub

        }
        @Override
        public void onSensorChanged(SensorEvent event) {

            if(event.sensor.getType() == Sensor.TYPE_LIGHT){
                System.out.println("LIGHT: " + event.values[0]);
                if (event.values[0] < 5){

                    new Handler(getMainLooper()).postDelayed(() -> {
                        imageView.setImageResource(R.drawable.gun_basic);

                    }, 1000); // 1 second

                    imageView.setImageResource(R.drawable.gun_reload);
                    mp_reload.start();



                }
            }
        }
    };
}