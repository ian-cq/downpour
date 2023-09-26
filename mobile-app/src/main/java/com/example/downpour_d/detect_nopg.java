package com.example.downpour_d;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.downpour_d.R;

public class detect_nopg extends AppCompatActivity {

    ImageButton gonext1, gonext2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detect_no);

        gonext1 = findViewById(R.id.buttonnext_detectno);
        gonext2 = findViewById(R.id.buttonsignupdetectpara);

        gonext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(detect_nopg.this, MainActivity.class));
            }
        });

        gonext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(detect_nopg.this, MainActivity.class));
            }
        });

    }
}