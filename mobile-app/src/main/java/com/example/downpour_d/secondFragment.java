package com.example.downpour_d;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Locale;

public class secondFragment extends Fragment {

    // for weather
    TextView dateInput, temptInput, condInput;
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "23048ac1d89fb1f5903baff3094176c6";
    DecimalFormat df = new DecimalFormat("#.##");

    private static final DecimalFormat decfor = new DecimalFormat("0.00");

    TextView username, district;

    ImageButton news1, news2;

    TextView updates, analytic, safetyguide;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String uid;

    ImageButton profile;

    public secondFragment(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup flFragment, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_second, flFragment, false);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        uid = mUser.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");

        username = view.findViewById(R.id.textnamehome_update);
        district = view.findViewById(R.id.textView14);

        safetyguide = view.findViewById(R.id.Saftey1);

        news1 = view.findViewById(R.id.buttonnews1_updatehome);
        news2 = view.findViewById(R.id.Buttonnews2_updatehome);

        // for weather
        dateInput = view.findViewById(R.id.textdateweather);
        temptInput = view.findViewById(R.id.tempweather);
        condInput = view.findViewById(R.id.weatherdescription_home);

        Calendar calendar = Calendar.getInstance();

        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        dateInput.setText(currentDate);

        news1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gonews1 = new Intent(getActivity(), newspg1.class);
                startActivity(gonews1);
            }
        });

        news2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gonews2 = new Intent(getActivity(), newspg2.class);
                startActivity(gonews2);
            }
        });

        safetyguide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gosafety = new Intent(getActivity(), safteymeasurepg.class);
                startActivity(gosafety);
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username.setText(snapshot.child(uid).child("fname").getValue(String.class).toUpperCase() + " " + snapshot.child(uid).child("lname").getValue(String.class).toUpperCase());
                district.setText(snapshot.child(uid).child("district").getValue(String.class));

                getWeather(view, snapshot.child(uid).child("district").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        profile = view.findViewById(R.id.imageButtonpfhome_pf);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), userpf_pg.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void getWeather(View view, String city){
        String tempURL = "";
        tempURL = url + "?q=" + city + ",MY" +"&appid=" + appid;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                String output = "";
                try {
                    JSONObject jsonresponse = new JSONObject(response);
                    JSONArray jsonArray = jsonresponse.getJSONArray("weather");
                    JSONObject jsonObjectweather = jsonArray.getJSONObject(0);
                    String description = jsonObjectweather.getString("description");
                    JSONObject jsonobjectmain = jsonresponse.getJSONObject("main");
                    double temp = jsonobjectmain.getDouble("temp") - 273.15;
                    double feelslike = jsonobjectmain.getDouble("feels_like") - 273.15;
                    float pressure = jsonobjectmain.getInt("pressure");
                    int humidity = jsonobjectmain.getInt("humidity");
                    JSONObject jsonObjectwind = jsonresponse.getJSONObject("wind");
                    String wind = jsonObjectwind.getString("speed");
                    JSONObject jsonObjectclouds = jsonresponse.getJSONObject("clouds");
                    String clouds = jsonObjectclouds.getString("all");
                    JSONObject jsonObjectsys = jsonresponse.getJSONObject("sys");

                    String tempshown = "";
                    tempshown = tempshown + decfor.format(temp);

                    temptInput.setText(tempshown+"℃");
                    condInput.setText(description.toUpperCase());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);

    }

}