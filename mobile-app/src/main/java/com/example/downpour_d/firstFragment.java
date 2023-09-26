package com.example.downpour_d;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.PermissionChecker;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import android.Manifest;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.downpour_d.ml.FloodDetection;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Stack;

public class firstFragment extends Fragment {

    ImageButton profile, camerabtn, gallerybtn;
    Uri imageUri;

    ImageView tempimage;

    TextView result;

    StorageReference storageReference;

    ProgressDialog progressDialog;

    public final FloodModel floodModel = new FloodModel();

    public firstFragment() {
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup flFragment, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first, flFragment, false);

        profile = view.findViewById(R.id.buttonpf_detect);
        camerabtn = view.findViewById(R.id.imageButtoncamera);
        gallerybtn = view.findViewById(R.id.imagegallery);

        tempimage = view.findViewById(R.id.imageView18);

        progressDialog = new ProgressDialog(getActivity());

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), userpf_pg.class);
                startActivity(intent);
            }
        });

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }

        gallerybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFromGalery();
            }
        });

        camerabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 100);
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });


        return view;
    }

    private void uploadImage() {

        progressDialog.setTitle("Uploading Image...");
        progressDialog.show();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US);
        Date now = new Date();
        String filename = formatter.format(now);

        storageReference = FirebaseStorage.getInstance("gs://images-flood-downpour-taylors-2022").getReference("images/" + filename);

        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        tempimage.setImageURI(null);
                        Toast.makeText(getActivity(), "Uploaded :)", Toast.LENGTH_SHORT).show();

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }


                        Intent processparam = new Intent(getActivity(), detect_parapg.class);
                        startActivity(processparam);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Failed :<", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    //    public String getImagePath() {
//        storageReference = FirebaseStorage.getInstance().getReference();
//        String path = storageReference.getPath();
//        return path;
//    }
    private void selectFromGalery() {
        Intent itnent5 = new Intent();
        itnent5.setType("image/");
        itnent5.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(itnent5, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null && data.getData() != null) {

            Bitmap image = (Bitmap) data.getExtras().get("data");
            int dimension = Math.min(image.getWidth(), image.getHeight());
            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
            imageUri = data.getData();
            tempimage.setImageBitmap(image);

//            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);


//            Intent testting = new Intent(getActivity(), userpf_pg.class);
//            startActivity(testting);

            // import data to ML Model and store data in Firebase
//            imageUri = data.getData();
//            tempimage.setImageURI(imageUri);
//
//            Bitmap image = (Bitmap) data.getExtras().get("data");
//            int dimension = Math.min(image.getWidth(), image.getHeight());
//            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
//            tempimage.setImageBitmap(image);

            floodModel.makeDetection(image, getActivity());

            uploadImage();
        }

//    ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    if (result.getResultCode() == Activity.RESULT_OK && result != null && result.getData() != null) {
//
//                        // Image captured successfully, do something with it
//                        Bundle extras = result.getData().getExtras();
//                        Bitmap imageBitmap = (Bitmap) extras.get("data");
//                        int dimension = Math.min(imageBitmap.getWidth(), imageBitmap.getHeight());
//                        imageBitmap = ThumbnailUtils.extractThumbnail(imageBitmap, dimension, dimension);
//                        tempimage.setImageBitmap(imageBitmap);
//
//                        // import data to ML Model and store data in Firebase
//
//                        imageUri = result.getData().getData();
//                        tempimage.setImageURI(imageUri);
//
//                        floodModel.makeDetection(imageBitmap);
//                        uploadImage();
//                    }
//                }
//            });

//        ActivityResultLauncher<String> galleryLauncher = registerForActivityResult(
//                new ActivityResultContracts.GetContent(),
//                new ActivityResultCallback<Uri>() {
//                    @Override
//                    public void onActivityResult(Uri result) {
//                        // Image selected successfully, do something with it
//                        tempimage.setImageURI(result);
//                        Bitmap bitmap;
//                        try {
//                            bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(result));
//                        } catch (FileNotFoundException e) {
//                            throw new RuntimeException(e);
//                        }
////
////                        floodModel.makeDetection(bitmap);
//                        uploadImage();
//                    }
//                });
    }
}



