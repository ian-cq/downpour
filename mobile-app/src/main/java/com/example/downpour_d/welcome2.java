package com.example.downpour_d;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class welcome2 extends AppCompatActivity{
    ImageButton buttonwelcome2;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome2);

//        timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(welcome2.this, welcome3.class);
//                startActivity(intent);
//                finish();
//            }
//        }, 5000);

        buttonwelcome2 = (ImageButton) findViewById(R.id.buttonnextwel2);
        buttonwelcome2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(welcome2.this, welcome3.class);
                startActivity(intent);
            }
        });
    }
}