package com.example.downpour_d;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import java.util.Timer;
import java.util.TimerTask;

public class welcome1 extends AppCompatActivity {
    ImageButton buttonwelcome1;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome1);

//        timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(welcome1.this, welcome2.class);
//                startActivity(intent);
//                finish();
 //           }
 //       }, 5000);

        buttonwelcome1 = (ImageButton) findViewById(R.id.buttonnextwel1);
        buttonwelcome1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(welcome1.this, welcome2.class);
                startActivity(intent);
            }
        });
    }
}
