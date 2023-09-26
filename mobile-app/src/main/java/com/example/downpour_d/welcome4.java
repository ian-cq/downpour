package com.example.downpour_d;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.util.Timer;
import java.util.TimerTask;

public class welcome4 extends AppCompatActivity {

    ImageButton buttonwelcome4;
    Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcomepg4);

//        timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(welcome4.this, signinpg.class);
//                startActivity(intent);
//                finish();
//            }
//        }, 5000);

        buttonwelcome4 = (ImageButton) findViewById(R.id.buttonnextwel4);
        buttonwelcome4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(welcome4.this, signinpg.class);
                startActivity(intent);
            }
        });
    }
}
