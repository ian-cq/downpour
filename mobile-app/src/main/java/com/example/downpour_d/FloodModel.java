package com.example.downpour_d;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.downpour_d.ml.FloodDetection;
import com.example.downpour_d.ml.FloodPrediction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.ml.modeldownloader.CustomModel;
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions;
import com.google.firebase.ml.modeldownloader.DownloadType;
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.model.Model;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class FloodModel {
    CustomModelDownloadConditions conditions = new CustomModelDownloadConditions.Builder()
            .requireWifi()  // Also possible: .requireCharging() and .requireDeviceIdle()
            .build();

    Interpreter interpreter;

    Context context;

    public static int floodStatus;

    TextView result;

    int imageSize = 32;

    StorageReference storageReference;
    StorageReference detectionRef;

//    public void loadDetectionModel() {
//        FirebaseModelDownloader.getInstance()
//                .getModel("flood-detection", DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND, conditions)
//                .addOnSuccessListener(new OnSuccessListener<CustomModel>() {
//                    @Override
//                    public void onSuccess(CustomModel model) {
//                        // Download complete. Depending on your app, you could enable the ML
//                        // feature, or switch from the local model to the remote model, etc.
//
//                        // The CustomModel object contains the local path of the model file,
//                        // which you can use to instantiate a TensorFlow Lite interpreter.
//                        File modelFile = model.getFile();
//                        if (modelFile != null) {
//                            interpreter = new Interpreter(modelFile);
//                            setInterpreter(interpreter);
//                        }
//                    }
//                });
//    }

    public void loadPredictionModel() {
        FirebaseModelDownloader.getInstance()
                .getModel("flood-prediction", DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND, conditions)
                .addOnSuccessListener(new OnSuccessListener<CustomModel>() {
                    @Override
                    public void onSuccess(CustomModel model) {
                        // Download complete. Depending on your app, you could enable the ML
                        // feature, or switch from the local model to the remote model, etc.

                        // The CustomModel object contains the local path of the model file,
                        // which you can use to instantiate a TensorFlow Lite interpreter.
                        File modelFile = model.getFile();
                        if (modelFile != null) {
                            interpreter = new Interpreter(modelFile);
                        }
                    }
                });
    }

//    public File getImagePath() {
//        storageReference = FirebaseStorage.getInstance().getReference();
//        String path = storageReference.getPath();
//        return path;
//    }

    public void makeDetection(Bitmap image, Context context) {

        try {
            FloodDetection model = FloodDetection.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 32, 32, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;
            //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
            for(int i = 0; i < imageSize; i ++){
                for(int j = 0; j < imageSize; j++){
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 1));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            FloodDetection.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();

            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > confidences[maxPos]) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            String[] classes = {"Flooded", "Not Flooded"};
            result.setText("Prediction: " + classes[maxPos] + " (" + maxConfidence + ")");

            Toast.makeText(context, "Prediction: " + classes[maxPos], Toast.LENGTH_SHORT).show();

            if (classes[maxPos] == "Flooded") {
                setIsFlood();
            }

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }
//        String filePath = FirebaseStorage.getInstance().getReference().child("2023_02_16_04_55_13").getPath();
//        detectionRef = storageRef.child("images/2023_02_16_04_55_13");
//        Bitmap bitmapImage = BitmapFactory.decodeFile(FirebaseStorage.getInstance().getReference().child("images/2023_02_16_04_55_13.jpg").getPath());
//
//        Bitmap bitmap = Bitmap.createScaledBitmap(image, 224, 224, true);
//        ByteBuffer input = ByteBuffer.allocateDirect(224 * 224 * 3 * 4).order(ByteOrder.nativeOrder());
//
//
//        int bufferSize = 1000 * java.lang.Float.SIZE / java.lang.Byte.SIZE;
//        ByteBuffer modelOutput = ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder());
//        interpreter.run(input, modelOutput);
//
//        Log.d("TAG", "makeDetection: " + modelOutput);

    public void makePrediction() {
        try {
            FloodPrediction model = FloodPrediction.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 4}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(224 * 4).order(ByteOrder.nativeOrder());
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            FloodPrediction.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

    public void setIsFlood() {
        floodStatus = 1;
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference flood_db = db.collection("flood-parameter");

    public void updateDistricts_FloodStatus(String district) {
        Query queryFloodDistrict = flood_db.whereEqualTo("district", district);

        queryFloodDistrict.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        String floodDocuments = document.getId();
                        flood_db.document(floodDocuments).update("isFlood", 1);
                    }
                } else {
                    Log.d("FirestoreUtils", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public Interpreter getInterpreter() {
        return interpreter;
    }

    public void setInterpreter(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    public void closeInterpreter() {
        interpreter.close();
    }

    public void deleteInterpreter() {
        interpreter = null;
    }

    public boolean isInterpreterNull() {
        return interpreter == null;
    }

    public boolean isInterpreterNotNull() {
        return interpreter != null;
    }

    public void closeInterpreterIfNotNull() {
        if (isInterpreterNotNull()) {
            interpreter.close();
        }
    }

    public void closeInterpreterIfNull() {
        if (isInterpreterNull()) {
            interpreter.close();
        }
    }

    public void checkInterpreter() {
        if (isInterpreterNull()) {
            System.out.println("Interpreter is null");
        } else {
            System.out.println("Interpreter is not null");
        }
    }
}
