package com.example.sensorapplication.Activitiy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.sensorapplication.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static com.example.sensorapplication.Service.Constants.WRITE_CSV;
import static com.example.sensorapplication.Service.FileService.createFile;

public class WriteCsvActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(WRITE_CSV, "creating WriteCsvActivity");
        setContentView(R.layout.activity_write_csv);
        Intent callingIntent = getIntent();
        List<Map.Entry<Long, Float>> temperatureList = getDataFromIntent(callingIntent);

        writeDataToCsv(temperatureList);

    }

    private List<Map.Entry<Long, Float>> getDataFromIntent(Intent intent) {
        Serializable temperatureData = intent.getSerializableExtra("Temperature Data");
        Log.d(WRITE_CSV, "received intent data");
        return (List<Map.Entry<Long, Float>>) temperatureData;
    }

    private void writeDataToCsv(List<Map.Entry<Long, Float>> temperatureList) {

        File file = createFile(this, "temperature.csv");
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
        } catch (IOException e) {
            Log.e(WRITE_CSV, "File not found when creating file output stream");
            e.printStackTrace();
        }
        try {
            os.write("Time, Temperature".getBytes());
            for (Map.Entry<Long, Float> entry : temperatureList) {
                Log.d(WRITE_CSV, "Time: " + entry.getKey().toString());
                Log.d(WRITE_CSV, "Temp: " + entry.getValue().toString());
                os.write(("\n" + entry.getKey().toString() + "," + entry.getValue().toString()).getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
