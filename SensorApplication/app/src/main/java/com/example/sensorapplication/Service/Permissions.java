package com.example.sensorapplication.Service;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static com.example.sensorapplication.Service.Constants.*;

public final class Permissions {

    static Boolean hasWriteExternalStoragePermission(Context context) {
        int writePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            Log.d(PERMISSIONS, "no permission to write to external storage");
            return false;
        } else {
            Log.d(PERMISSIONS, "has permission to write to external storage");
            return true;
        }
    }

    static Boolean hasReadExternalStoragePermission(Context context) {
        int readPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (readPermission != PackageManager.PERMISSION_GRANTED) {
            Log.d(PERMISSIONS, "no permission to read from external storage");
            return false;
        } else {
            Log.d(PERMISSIONS, "has permission to read from external storage");
            return true;
        }
    }

    private static Boolean hasRecordAudioPermission(Context context) {
        int readPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO);
        if (readPermission != PackageManager.PERMISSION_GRANTED) {
            Log.d(PERMISSIONS, "no permission to record audio");
            return false;
        } else {
            Log.d(PERMISSIONS, "has permission to record audio");
            return true;
        }
    }

    private static void requestReadPermission(Activity activity) {
        Log.d(PERMISSIONS, "Requesting read permission");
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
    }

    private static void requestWritePermission(Activity activity) {
        Log.d(PERMISSIONS, "Requesting write permission");
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    private static void requestRecordAudioPermission(Activity activity) {
        Log.d(PERMISSIONS, "Requesting record audio permission");
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.RECORD_AUDIO}, 1);
    }

    public static void requestPermissions(Activity activity) {
        if (!hasWriteExternalStoragePermission(activity)) {
            requestWritePermission(activity);

        }
        if (!hasReadExternalStoragePermission(activity)) {
            requestReadPermission(activity);
        }
        if (!hasRecordAudioPermission(activity)) {
            requestRecordAudioPermission(activity);
        }
    }


}
