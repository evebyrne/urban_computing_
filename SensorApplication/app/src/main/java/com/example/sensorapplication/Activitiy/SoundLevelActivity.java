package com.example.sensorapplication.Activitiy;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.sensorapplication.R;
import com.example.sensorapplication.Service.FileService;
import com.example.sensorapplication.SoundService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.sensorapplication.Service.Constants.FIREBASE;
import static com.example.sensorapplication.Service.Constants.SERVICE;
import static com.example.sensorapplication.Service.Constants.SOUND;
import static com.example.sensorapplication.Service.Constants.WRITE_CSV;

public class SoundLevelActivity extends Activity {

    FileOutputStream os = null;
    Timer timer;
    FirebaseFirestore db;
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sound_activity);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        db = FirebaseFirestore.getInstance();

        intent = new Intent(getApplicationContext(), SoundService.class);

        createFile();

        Button startBtn = findViewById(R.id.start_btn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Log.d(SERVICE, "Sending intent to start service");
                Toast toast = Toast.makeText(getApplicationContext(), "starting to record sound amplitude", Toast.LENGTH_SHORT);
                toast.show();

                startService(intent);

            }
        });

        Button stopBtn = findViewById(R.id.stop_btn);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(), "stopped recording sound amplitude", Toast.LENGTH_SHORT);
                toast.show();
                stop();
                stopService(intent);
            }
        });


    }

    private MediaRecorder mRecorder = null;

    public void start() {
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");
            try {
                mRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mRecorder.start();
            Log.d(SOUND, "started media recorder");
        }
    }

    public void stop() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
        Log.d(SOUND, "stopped media recorder");

        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        timer.cancel();
        timer.purge();
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getAmplitude() {
        if (mRecorder != null) {
            String amp = String.valueOf(mRecorder.getMaxAmplitude());
            Log.d(SOUND, amp);
            return amp;
        } else {
            Log.d(SOUND, "returning 0, mRecorder null");
            return "";
        }

    }

    private void createFile() {
        File file = FileService.createFile(this, "sound_level.csv");
        os = null;
        try {
            os = new FileOutputStream(file);
            os.write("Time, Amplitude".getBytes());
        } catch (IOException e) {
            Log.e(WRITE_CSV, "File not found when creating file output stream");
            e.printStackTrace();
        }
    }

    private void writeDataToCsv(Long time, String amplitude) {
        try {
            Log.d(WRITE_CSV, "Time: " + time.toString());
            Log.d(WRITE_CSV, "Amp: " + amplitude);
            os.write(("\n" + time.toString() + "," + amplitude).getBytes());
            Log.d(SOUND, "Written amplitude value to csv");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        stop();

    }

}
