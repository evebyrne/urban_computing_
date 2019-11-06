

package com.example.sensorapplication.Activitiy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sensorapplication.R;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.sensorapplication.Service.Constants.WRITE_CSV;


public class SensorActivity extends Activity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor ambientLight;
    private Float prevTemperature = Float.valueOf(-1);
    private List<Map.Entry<Long, Float>> recordedTemperatures = new ArrayList<>();

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_main);

        // Get an instance of the sensor service, and use that to get an instance of
        // a particular sensor.
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        ambientLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);


        Button next = findViewById(R.id.temp_stop_button);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(WRITE_CSV, "creating intent for csv activity");
                Intent myIntent = new Intent(view.getContext(), WriteCsvActivity.class);
                myIntent.putExtra("Temperature Data", (Serializable) recordedTemperatures);
                startActivityForResult(myIntent, 0);
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
        Float temperature = event.values[0];
        int i = 0;
//        if new temp - prev temp has a difference greater than 0.001
        if ((Math.abs(temperature - prevTemperature) > 0.001)) {
            TextView tv = findViewById(R.id.scroll_text_view);
            tv.setVisibility(View.GONE);
            tv.setVisibility(View.VISIBLE);


            tv.append("\n\ntemp: " + temperature);
            tv.append("\ntime: " + event.timestamp);
            tv.append("\naccuracy: " + event.accuracy);
            tv.append("\nsensor: " + event.sensor);
            tv.append("\nprevTemp:" + prevTemperature);
            tv.append("\nbool:" + (Math.abs(temperature - prevTemperature)));
            recordedTemperatures.add(new AbstractMap.SimpleEntry<>(event.timestamp, event.values[0]));
            prevTemperature = temperature;
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
}

