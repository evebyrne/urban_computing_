package com.example.sensorapplication;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.sensorapplication.Service.FileService;
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

public class SoundService extends Service {

    FileOutputStream os;
    Timer timer;
    FirebaseFirestore db;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // do your jobs here
        Log.d(SERVICE, "Starting service");
        startMediaRecorder();
        createFile();
        db = FirebaseFirestore.getInstance();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void run() {
                String amp = getAmplitudeFromMediaRecorder();
                Log.d(SOUND, "amplitude returned: " + amp);
                if (amp != "") {
//                    writeDataToCsv(System.currentTimeMillis(), amp);
                    writeSoundToFirebase(System.currentTimeMillis(), amp);
                }
            }
        }, 0, 2000);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(SERVICE, "Destroying service");
        super.onDestroy();
        stop();
    }

    private MediaRecorder mRecorder = null;

    public void startMediaRecorder() {
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

    public String getAmplitudeFromMediaRecorder() {
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void writeSoundToFirebase(Long time, String soundValue) {
        Log.d(FIREBASE, "trying to write to firebase");

        Date date = new Date(time);
        Map<String, String> sound = new HashMap<>();
        sound.put(date.toString(), soundValue);

        db.collection("sound_sensor_values").document(time.toString())
                .set(sound)
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