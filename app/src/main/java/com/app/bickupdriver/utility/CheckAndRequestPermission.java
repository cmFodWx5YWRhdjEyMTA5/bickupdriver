package com.app.bickupdriver.utility;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by fluper-pc on 14/9/17.
 */

public class CheckAndRequestPermission {
    private static final CheckAndRequestPermission ourInstance = new CheckAndRequestPermission();

    public static CheckAndRequestPermission getInstance() {
        return ourInstance;
    }

    private CheckAndRequestPermission() {
    }

    public boolean checkAndRequest(String permission, Activity activity){
// Here, thisActivity is the current activity
        boolean proceedtoFurther=false;
        if (ContextCompat.checkSelfPermission(activity,
                permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        1);

        }else {
            proceedtoFurther=true;
        }
        return proceedtoFurther;
    }
}
