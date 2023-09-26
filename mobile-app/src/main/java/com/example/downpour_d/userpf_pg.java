package com.example.downpour_d;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class userpf_pg extends AppCompatActivity {

    EditText name, email,phone, district, address;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String uid;

    ImageButton backbtn;

    Button signout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userprofile);

        backbtn = findViewById(R.id.backButton1_pf);

        signout = findViewById(R.id.buttonupdate_pf);

        name = findViewById(R.id.editTextTextPersonName3);
        email = findViewById(R.id.editTextTextEmailAddress2);
        phone = findViewById(R.id.editTextPhone2);
        district = findViewById(R.id.editTextTextPostalAddress2);
        address = findViewById(R.id.editTextTextPostalAddress4);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        uid = mUser.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(userpf_pg.this, MainActivity.class);
                startActivity(intent);
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.setText("Name: " + snapshot.child(uid).child("fname").getValue(String.class) + " " + snapshot.child(uid).child("lname").getValue(String.class));
                district.setText("District: " + snapshot.child(uid).child("district").getValue(String.class));
                email.setText("Email: " + snapshot.child(uid).child("email").getValue(String.class));
                phone.setText("Phone: " + snapshot.child(uid).child("phonenumber").getValue(String.class));
                address.setText("Address: " + snapshot.child(uid).child("address").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent backloginin = new Intent(userpf_pg.this, signinpg.class);
                backloginin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(backloginin);
            }
        });

    }
}
