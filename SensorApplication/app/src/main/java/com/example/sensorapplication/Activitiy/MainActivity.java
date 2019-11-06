package com.example.sensorapplication.Activitiy;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.sensorapplication.R;
import com.example.sensorapplication.Service.Constants;
import com.example.sensorapplication.Service.Permissions;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

import static com.example.sensorapplication.Service.Constants.PERMISSIONS;


public class MainActivity extends Activity implements EasyPermissions.PermissionCallbacks {
    TextView tv1 = null;
    private SensorManager mSensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("Main", "creating MainActivity");
        setContentView(R.layout.activity_main);

        Permissions.requestPermissions(this);

        listSensors();

        Button next = findViewById(R.id.temp_start_button);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), SensorActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

        Button firebaseButton = findViewById(R.id.start_firebase_button);
        firebaseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), FirebaseActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

        Button soundBtn = findViewById(R.id.get_sound_btn);
        soundBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), SoundLevelActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        Log.d(Constants.PERMISSIONS, "received permission result");
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.d(PERMISSIONS, "permission granted");
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.d(PERMISSIONS, "permission refused");
    }

    private void listSensors() {
        tv1 = findViewById(R.id.textView2);
        tv1.setVisibility(View.GONE);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> mList = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        for (int i = 1; i < mList.size(); i++) {
            tv1.setVisibility(View.VISIBLE);
            tv1.append("\n\n" + mList.get(i).getName() + "\n" + mList.get(i).getVendor() + "\n" + mList.get(i).getVersion() + "\n" + mList.get(i).getType());
        }
    }
}

