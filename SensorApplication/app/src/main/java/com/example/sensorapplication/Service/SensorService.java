package com.example.sensorapplication.Service;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import java.util.List;

public final class SensorService extends Activity {
    private List<Sensor> getSensors() {
        SensorManager mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        return mSensorManager.getSensorList(Sensor.TYPE_ALL);

    }
}
