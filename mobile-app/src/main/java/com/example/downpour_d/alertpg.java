package com.example.downpour_d;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.Manifest;

public class alertpg extends AppCompatActivity {

    ImageButton profile;

    Button emergency, safety;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert);

        profile = findViewById(R.id.buttonpf_alert);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(alertpg.this, userpf_pg.class);
                startActivity(intent);
            }
        });

        safety = findViewById(R.id.buttonsaftey);
        safety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gosafety = new Intent(alertpg.this, safteymeasurepg.class);
                startActivity(gosafety);
            }
        });

        emergency = findViewById(R.id.emergencybutton);
        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callEmergency();
            }
        });

    }
    public void callEmergency(){
        String number = "999";
        Intent callem = new Intent(Intent.ACTION_CALL);
        callem.setData(Uri.parse("tel:" + number));

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            return;
        }

        startActivity(callem);
    }
}