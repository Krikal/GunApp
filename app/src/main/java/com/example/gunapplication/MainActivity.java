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
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    TextView headerTitle;
    ImageView imageView;

    MediaPlayer mp_reload;
    MediaPlayer mp_fire;
    /*ammo*/
    int ammoCount;
    TextView ammoLabel;
    SensorManager mySensorManager;

    /*Shake*/
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Title*/
        headerTitle = (TextView) findViewById(R.id.header_title);
        headerTitle.setText("Fegyver applikáció");
        /*Gun image*/
        imageView = (ImageView) findViewById(R.id.imageView);
        /*Ammo label*/
        ammoLabel = (TextView) findViewById(R.id.ammoLabel);

        /*reload sound*/
        mp_reload = MediaPlayer.create(getApplicationContext(), R.raw.gun_reload);
        /*fire sound*/
        mp_fire = MediaPlayer.create(getApplicationContext(), R.raw.gun_fire);



        /* Light sensor */
        mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        Sensor lightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(lightSensor != null){
            System.out.println("Sensor.TYPE_LIGHT Available");
            mySensorManager.registerListener(
                    SensorListener,
                    lightSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);

        } else {
            System.out.println("Sensor.TYPE_LIGHT NOT Available");
        }
        /* light sensor end */


        /* Accelerometer sensor */
        mySensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Objects.requireNonNull(mySensorManager).registerListener(
                SensorListener,
                mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;



    }
    /*light sensor */
    private final SensorEventListener SensorListener  = new SensorEventListener(){
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub

        }
        @Override
        public void onSensorChanged(SensorEvent event) {

            if(event.sensor.getType() == Sensor.TYPE_LIGHT){
                //System.out.println("LIGHT: " + event.values[0]);
                if (event.values[0] < 5){

                    new Handler(getMainLooper()).postDelayed(() -> imageView.setImageResource(R.drawable.gun_basic), 1000); // 1 second

                    imageView.setImageResource(R.drawable.gun_reload);
                    mp_reload.start();
                    ammoCount = 6;
                    ammoLabel.setText("Ammo: " + ammoCount);

                }
            }

            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                mAccelLast = mAccelCurrent;
                mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
                float delta = mAccelCurrent - mAccelLast;
                mAccel = mAccel * 0.9f + delta;
                if (mAccel > 12 && ammoCount > 0) {
                    //System.out.println("Shake event detected");
                    new Handler(getMainLooper()).postDelayed(() -> imageView.setImageResource(R.drawable.gun_basic), 500); // 0.5 second

                    imageView.setImageResource(R.drawable.gun_fire);
                    mp_fire.setOnCompletionListener(mediaPlayer -> ammoLabel.setText("Ammo: " + --ammoCount));
                    mp_fire.start();

                    //System.out.println(ammoCount);
                }

            }
        }
    };
    @Override
    protected void onResume() {
        mySensorManager.registerListener(SensorListener, mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

        super.onResume();
    }
    @Override
    protected void onPause() {
        mySensorManager.unregisterListener(SensorListener);

        super.onPause();
    }
}