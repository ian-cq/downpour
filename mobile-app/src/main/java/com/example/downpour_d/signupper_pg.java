package com.example.downpour_d;

import static android.content.ContentValues.TAG;

import com.example.downpour_d.signuppg.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import android.Manifest;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class signupper_pg extends AppCompatActivity {

    // for locating
    FusedLocationProviderClient fusedLocationProviderClient;
    private final static int REQUEST_CODE = 100;

    Button locateme;

    Spinner spinner1, spinner2;
    String selectedClass, selectedDiv;

    String uid;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    EditText firstName, lastName, phoneNumber, addressInput;

    public String fname, lname, pnumber, addIn, typestatus, district;

    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_personal);

        spinner1 = findViewById(R.id.spinner1_signup); // Spinner s = (Spinner) findViewById((R.id.spinner1));
        spinner2 =findViewById(R.id.spinner2_signup_district);

// spinner 1 implementing onItemSelectedListener
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                // Toast.makeText ( MainActivity.this, parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                //  leave this empty
            }
        });

        // spinner 2 implementing onItemSelectedListener
//        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
//        {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
//            {
//                //  Toast.makeText ( MainActivity.this, parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent)
//            {
//                //  leave this empty
//            }
//
//        });

        firstName = findViewById(R.id.editTextTextfirstName);
        lastName = findViewById(R.id.editTextTextlastName2);
        phoneNumber = findViewById(R.id.editTextPhone);
        addressInput = findViewById(R.id.editTextTextPostalAddress);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        uid = mUser.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");
        locateme = (Button) findViewById(R.id.editTextTextcurentloc);

        Button currentLocation = findViewById(R.id.editTextTextcurentloc);

        final ImageButton signup = (ImageButton) findViewById(R.id.nextbutton_signpersonal);

        // for locating
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locateme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fname = firstName.getText().toString();
                lname = lastName.getText().toString();
                pnumber = phoneNumber.getText().toString();
                addIn = addressInput.getText().toString();
                typestatus = spinner1.getSelectedItem().toString();
                district = spinner2.getSelectedItem().toString().trim();

                if(fname.isEmpty()){
                    firstName.setError("First name cannot be empty");
                }else if(lname.isEmpty()){
                    lastName.setError("Last name cannot be empty");
                }else if(pnumber.isEmpty()){
                    phoneNumber.setError("Phone number cannot be empty");
                }else if(addIn.isEmpty()){
                    addressInput.setError("Address cannot be empty");
                }else{
                    progressDialog.setMessage("Registering...");
                    progressDialog.setTitle("Sign Up");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    signuppg sgn = new signuppg();

                    Users users = new Users();
                    users.setFname(fname);
                    users.setLname(lname);
                    users.setEmail(mUser.getEmail());
                    users.setPhonenumber(pnumber);
                    users.setAddress(addIn);
                    users.setStatus(typestatus);
                    users.setDistrict(district);

                    databaseReference.child(uid).setValue(users);
                    subscribeWarning();

                    Toast.makeText(signupper_pg.this, "User registered :)", Toast.LENGTH_SHORT).show();

                    nextPage();

                }
            }
        });

    }

    public void subscribeWarning() {

        String userLocation = district;

        FirebaseMessaging.getInstance().subscribeToTopic(userLocation)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed";
                        if (!task.isSuccessful()) {
                            msg = "Subscribe failed";
                        }
                        Log.d(TAG, msg);
                        Toast.makeText(signupper_pg.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getLastLocation() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null){
                        Geocoder geocoder = new Geocoder(signupper_pg.this, Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            addressInput.setText(addresses.get(0).getAddressLine(0));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
        }else{
            askPermission();
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(signupper_pg.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }else{
                Toast.makeText(this, "Please provide the required permission ._.", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void nextPage(){
        Intent intent1 = new Intent(signupper_pg.this, MainActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent1);
    }

}