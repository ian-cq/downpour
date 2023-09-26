package com.example.downpour_d;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class detect_useraccess extends Fragment {

    Button backhome;
    public detect_useraccess(){
        // require a empty public constructor
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.detect_unauthorized);
//    }

    public View onCreateView(LayoutInflater inflater, ViewGroup flFragment, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.detect_unauthorized, flFragment, false);

        backhome = view.findViewById(R.id.buttonbackhome);

        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                secondFragment homefragment = new secondFragment();
                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                fm.replace(R.id.flFragment, homefragment).commit();

            }
        });


        return view;

    }


}