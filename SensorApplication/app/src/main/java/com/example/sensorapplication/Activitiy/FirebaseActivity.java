package com.example.sensorapplication.Activitiy;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.sensorapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.sensorapplication.Service.Constants.FIREBASE;

public class FirebaseActivity extends Activity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor ambientLight;
    private Float prevLight = Float.valueOf(-1);
    private List<Map.Entry<Long, Float>> recordedLight = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firebase_activity);
//        writeToFirebase();
        db = FirebaseFirestore.getInstance();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        ambientLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

    }

    private void writeToFirebase() {
        Log.d(FIREBASE, "trying to write to firebase");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Los Angeles");
        city.put("state", "CA");
        city.put("country", "USA");

        db.collection("cities").document("LA")
                .set(city)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(FIREBASE, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(FIREBASE, "Error writing document", e);
                    }
                });
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        // Do something with this sensor data.
        Float light = event.values[0];
        int i = 0;
//        if new temp - prev temp has a difference greater than 0.001
        if ((Math.abs(light - prevLight) > 0.001)) {

//            writeToTextView(light, event);
            writeLightToFirebase(event.timestamp, light);
            recordedLight.add(new AbstractMap.SimpleEntry<>(event.timestamp, event.values[0]));
            prevLight = light;
            i++;

        }
    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        sensorManager.registerListener(this, ambientLight, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        sensorManager.unregisterListener(this);

    }

    private void writeToTextView(Float light, SensorEvent event) {
        TextView tv = findViewById(R.id.scroll_text_view);
        tv.setVisibility(View.GONE);
        tv.setVisibility(View.VISIBLE);


        tv.append("\n\nlight: " + light);
        tv.append("\ntime: " + event.timestamp);
        tv.append("\naccuracy: " + event.accuracy);
        tv.append("\nsensor: " + event.sensor);
        tv.append("\nprevTemp:" + prevLight);
        tv.append("\nbool:" + (Math.abs(light - prevLight)));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void writeLightToFirebase(Long time, Float lightValue) {
        Log.d(FIREBASE, "trying to write to firebase");

        long millis = System.currentTimeMillis() + (time - SystemClock.elapsedRealtimeNanos()) / 1000000L;
        Date date = new Date(millis);
        Map<String, Float> light = new HashMap<>();
        light.put(date.toString(), lightValue);

        db.collection("light sensor values").document(time.toString())
                .set(light)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(FIREBASE, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(FIREBASE, "Error writing document", e);
                    }
                });
    }
}
