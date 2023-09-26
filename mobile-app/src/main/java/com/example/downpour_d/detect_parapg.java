package com.example.downpour_d;

import static com.example.downpour_d.FloodModel.floodStatus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class detect_parapg extends AppCompatActivity {

    String tempdist;

    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "23048ac1d89fb1f5903baff3094176c6";
    DecimalFormat df = new DecimalFormat("#.##");

    private static final DecimalFormat decfor = new DecimalFormat("0.00");

    ProgressDialog progressDialog;
    ImageButton toprofile;
    ImageButton process;
    ImageButton gmaplocate;

    Spinner district;
    Spinner causeof;
    EditText date, temp1, humid, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detectparameter);

        toprofile = findViewById(R.id.imageButtonpfdetect);
        district = findViewById(R.id.spinner3);
        process = findViewById(R.id.buttonsignupdetectpara);
        date = findViewById(R.id.editTextDate_para);
        temp1 = findViewById(R.id.editTextTexttemp_para);
        humid = findViewById(R.id.editTexthumid_para);
        time = findViewById(R.id.editTextTime2_para);
        causeof = findViewById(R.id.spinnercause_para);
        gmaplocate = findViewById(R.id.imageButtonmappara);

        Date dateAndTime = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm", Locale.getDefault());
        String date1 = dateFormat.format(dateAndTime);
        String time1 = timeFormat.format(dateAndTime);

        date.setText(date1);
        time.setText(time1);

        district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tempdist = district.getSelectedItem().toString().trim();
                getWeather(tempdist);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        progressDialog = new ProgressDialog(this);

        toprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(detect_parapg.this, userpf_pg.class));
            }
        });

        process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getDistrict = tempdist;
                String getDate = date.getText().toString();
                String getTemp = temp1.getText().toString();
                String getHumid = humid.getText().toString();
                String getTime = time.getText().toString();
                String getCause = causeof.getSelectedItem().toString();
                String getLocation = "";
                int isFlood = 0;

                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("cause", getCause);
                hashMap.put("date", getDate);
                hashMap.put("district", getDistrict);
                hashMap.put("humidity", getHumid);
                hashMap.put("temperature", getTemp);
                hashMap.put("time", getTime);
                hashMap.put("location", getLocation);
                hashMap.put("flood", isFlood);

                String id = FirebaseFirestore.getInstance().collection("flood-parameters").document().getId();

                FirebaseFirestore.getInstance().collection("flood-parameters").document(id).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.setMessage("Detecting O_O...");
                        progressDialog.setTitle("Identifying the flood....");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                        Toast.makeText(detect_parapg.this, "(●'◡'●)", Toast.LENGTH_SHORT).show();

                        // add here to see result
                        if (isFlood == updateFloodStatus()) {
                            floodModel.updateDistricts_FloodStatus(tempdist);

                            Intent godetect_yespg = new Intent(detect_parapg.this, detect_yespg.class);
                            startActivity(godetect_yespg);
                        } else {
                            Intent godetect_nopg = new Intent(detect_parapg.this, detect_nopg.class);;
                            startActivity(godetect_nopg);
                        }


                        // intent to detect_yespg.class if yes
                        // intent to detect_nopg.class if no
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(detect_parapg.this, "Failed ψ(._. )>", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    FloodModel floodModel = new FloodModel();

    public int updateFloodStatus() {
        return floodStatus;
    }
    public void getWeather(String city){
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

                    String temphumid = "";
                    temphumid = temphumid + humidity;

                    temp1.setText(tempshown);
                    humid.setText(temphumid);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }
}