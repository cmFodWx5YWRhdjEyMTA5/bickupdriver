package com.app.bickupdriver.utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * <H1>Bickup DriverBickup</H1>
 * <H1>SharedPrefManager</H1>
 *
 * <p>Helps in managing the shared preferences tasks</p>
 *
 * @author Divya Thakur
 * @version 1.0
 * @since 9/1/16
 */
public class SharedPrefManager {

    public static final String PREFS_USER_CREDENTIALS_FILE = "UserCredentialsFile";
    private static final String TAG = SharedPrefManager.class.getSimpleName();
    public static final String APP_LANGUAGE = "app_language";
    public static String GCM_REGISTRATION_ID = "GcmRegistrationKey";
    private static SharedPreferences sharedPreferences;
    public static final String REFERRAL_CODE = "referral_code";


    private Context context;
    private SharedPreferences.Editor editor;
    private static SharedPrefManager mInstance;
    private static Context mCtx;


    public SharedPrefManager(Context context){
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }


    /**
     * Stores the registration ID in the application's Shared Preferences
     *
     * @param context application's context.
     * @param regId   registration ID
     */
    public static void saveGCMRegistrationID(Context context, String regId) {
        Utils.printLogs(TAG, "Inside saveGCMRegistrationID()");
        try {
            sharedPreferences = context.getSharedPreferences(
                    PREFS_USER_CREDENTIALS_FILE, 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(GCM_REGISTRATION_ID, regId);
            editor.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Utils.printLogs(TAG, "Outside saveGCMRegistrationID()");
    }

    /**
     * Returns the registration ID from the application's Shared Preferences
     *
     * @param context application's context.
     */
    public static String getGcmRegistrationId(Context context) {

        Utils.printLogs(TAG, "Inside getGcmRegistrationId()");

        try {

            sharedPreferences = context.getSharedPreferences(
                    PREFS_USER_CREDENTIALS_FILE, Context.MODE_PRIVATE);
            // Read the shared preference value

            return sharedPreferences.getString(GCM_REGISTRATION_ID, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Utils.printLogs(TAG, "Outside getGcmRegistrationId()");
        return null;
    }

    /*public void saveStringData(String key, String value) {
        sharedPreferences = context.getSharedPreferences(PREFS_USER_CREDENTIALS_FILE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getStringData(String key) {
        sharedPreferences = context.getSharedPreferences(PREFS_USER_CREDENTIALS_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "---");
    }*/

    public void saveStringData(String key, String value) {
        sharedPreferences = mCtx.getSharedPreferences(PREFS_USER_CREDENTIALS_FILE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getStringData(String key) {
        sharedPreferences = mCtx.getSharedPreferences(PREFS_USER_CREDENTIALS_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "---");
    }

}
