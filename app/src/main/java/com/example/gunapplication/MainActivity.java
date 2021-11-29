package com.example.gunapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView headerTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Title*/
        headerTitle = (TextView) findViewById(R.id.header_title);
        headerTitle.setText("Fegyver applikáció");



    }
}