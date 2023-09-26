package com.example.downpour_d;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class detect_yespg extends AppCompatActivity {

    Button safetypage;
    ImageButton next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detect_yes);

        safetypage = findViewById(R.id.buttonsaftey);
        next = findViewById(R.id.buttonsignupdetectpara);

        safetypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gosafetypage = new Intent(detect_yespg.this, safteymeasurepg.class);
                startActivity(gosafetypage);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gobackhome = new Intent(detect_yespg.this, MainActivity.class);
                startActivity(gobackhome);
            }
        });
    }
}
