package com.example.sensorapplication.Service;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

import static com.example.sensorapplication.Service.Constants.FOLDER;
import static com.example.sensorapplication.Service.Constants.WRITE_CSV;

public final class
FileService {
    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Log.d(FOLDER, "is writable");
            return true;
        }
        Log.d(FOLDER, "is not writable");
        return false;
    }

    public static final File createFile(Context context, String fileName) {

        Boolean hasAllPermissions = Permissions.hasReadExternalStoragePermission(context) && Permissions.hasWriteExternalStoragePermission(context);
        if (isExternalStorageWritable() && hasAllPermissions) {
            File file = new File(Environment.getExternalStorageDirectory(), fileName);
            Log.d(WRITE_CSV, "creating file: " + file);
            return file;
        } else {
            Log.d(WRITE_CSV, "couldn't create file");
            return null;
        }
    }
}
