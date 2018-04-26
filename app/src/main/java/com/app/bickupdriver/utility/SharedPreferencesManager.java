package com.app.bickupdriver.utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * <H1>Bickup DriverBickup</H1>
 * <H1>SharedPreferencesManager</H1>
 *
 * <p>Helps in managing the shared preferences tasks</p>
 *
 * @author Divya Thakur
 * @version 1.0
 * @since 9/1/16
 */
public class SharedPreferencesManager {

    public static final String PREFS_USER_CREDENTIALS_FILE = "UserCredentialsFile";
    private static final String TAG = SharedPreferencesManager.class.getSimpleName();
    public static String GCM_REGISTRATION_ID = "GcmRegistrationKey";


    /**
     * Stores the registration ID in the application's Shared Preferences
     *
     * @param context application's context.
     * @param regId   registration ID
     */
    public static void saveGCMRegistrationID(Context context, String regId) {

        Utils.printLogs(TAG, "Inside saveGCMRegistrationID()");

        try {

            SharedPreferences sharedPreferences = context.getSharedPreferences(
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

            SharedPreferences preferencesReader = context.getSharedPreferences(
                    PREFS_USER_CREDENTIALS_FILE, Context.MODE_PRIVATE);
            // Read the shared preference value

            return preferencesReader.getString(
                    GCM_REGISTRATION_ID, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Utils.printLogs(TAG, "Outside getGcmRegistrationId()");
        return null;
    }
}
