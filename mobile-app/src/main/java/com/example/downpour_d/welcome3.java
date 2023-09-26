package com.example.downpour_d;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.util.Timer;
import java.util.TimerTask;

public class welcome3 extends AppCompatActivity {

    ImageButton buttonwelcome3;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcomepg3);

//        timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(welcome3.this, welcome4.class);
//                startActivity(intent);
//                finish();
//            }
//        }, 5000);

        buttonwelcome3 = (ImageButton) findViewById(R.id.buttonnextwel3);
        buttonwelcome3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(welcome3.this, welcome4.class);
                startActivity(intent);
            }
        });
    }
}