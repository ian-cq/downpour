package com.example.downpour_d;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class safteymeasurepg extends AppCompatActivity {

    ImageButton back, profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_steps);

        back = findViewById(R.id.imageButtonnotify_emergency);
        profile = findViewById(R.id.imageButtonpfemergency);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backhome = new Intent(safteymeasurepg.this, MainActivity.class);
                startActivity(backhome);

//                secondFragment homefragment = new secondFragment();
//                FragmentTransaction fm = safteymeasurepg.this.getSupportFragmentManager().beginTransaction();
//                fm.replace(R.id.flFragment, homefragment).commit();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goprofile = new Intent(safteymeasurepg.this, userpf_pg.class);
                startActivity(goprofile);
            }
        });
    }
}