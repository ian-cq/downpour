package com.example.downpour_d;


import static android.content.ContentValues.TAG;
import static com.example.downpour_d.FloodModel.floodStatus;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.*;

public class thirdFragment extends Fragment {

    ImageButton profile, buttonrefresh;

    FloodModel floodmodel = new FloodModel();

    public thirdFragment(){
        // require a empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup flFragment, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_third, flFragment, false);

//        buttonrefresh = (ImageButton) findViewById(R.id.buttonrefresh);
//        buttonrefresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (floodStatus == 1) {
//                    Intent intent = new Intent(getActivity(), alertpg.class);
//                }
//            }
//        });

        profile = view.findViewById(R.id.buttonpf_frag_3);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), userpf_pg.class);
                startActivity(intent);
            }
        });


        return view;
    }


    public void databaseListener() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("flood-parameters").document("{districtid}");
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                } else {
                    Log.d(TAG, "Current data: null");
                }

                Intent intent = new Intent(getActivity(), alertpg.class);
                startActivity(intent);
            }
        });
    }


}