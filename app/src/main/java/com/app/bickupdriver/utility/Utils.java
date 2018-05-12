package com.app.bickupdriver.utility;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.app.bickupdriver.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * <H1>Bickup DriverBickup</H1>
 * <H1>Utils</H1>
 * <p>
 * <p>Provides utility methods that are commonly used in the application</p>
 *
 * @author Divya Thakur
 * @version 1.0
 * @since 9/1/16
 */
public class Utils {

    private static final String TAG = "Utils";
    public static final boolean LOG_ON = true;
    public static final String GOODS_ACTIVITY_NOTIFICATION_BROADCAST_ACTION = "com.app.bickupdriver.";
    public static final float THICKNESS_OF_POLYLINE = 8.0f;

    //public static final int CUSTOM_SHARE_ARTICLE_DIALOG_CONST 				= 870;




    public static String getLastCharacters(String word, int charactersFromLast) {
        if (word.length() == charactersFromLast) {
            return word;
        } else if (word.length() > charactersFromLast) {
            return word.substring(word.length() - charactersFromLast);
        } else {
            // whatever is appropriate in this case
            throw new IllegalArgumentException("word has less than " + charactersFromLast + " characters!");
        }
    }


    // Time stamp : 1525261152000
    // today 05:09 PM
    public static String getFormattedDate(Context context, long timeInMillis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(timeInMillis);

        Calendar now = Calendar.getInstance();

        final String timeFormatString = "hh:mm aa";
        final String dateTimeFormatString = "EEEE, MMMM d, h:mm aa";
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)) {
            return String.format("%s %s", context.getString(R.string.today), android.text.format.DateFormat.format(timeFormatString, smsTime));
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1) {
            return String.format("%s %s", context.getString(R.string.yesterday), android.text.format.DateFormat.format(timeFormatString, smsTime));
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            return android.text.format.DateFormat.format(dateTimeFormatString, smsTime).toString();
        } else {
            return android.text.format.DateFormat.format("MMMM dd yyyy, h:mm aa", smsTime).toString();
        }
    }


    /**
     * Hides the keyboard if the screen is tapped
     * anywhere else then the edit texts
     *
     * @param activity holds the object for Activity
     */
    public static void hideSoftKeyboard(Activity activity) {

        Utils.printLogs(TAG, "Inside hideSoftKeyboard()");

        try {
            InputMethodManager inputMethodManager = (InputMethodManager)
                    activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Utils.printLogs(TAG, "Outside hideSoftKeyboard()");
    }

    /**
     * Returns the current Application version in form of String
     *
     * @param context holds the object for Context
     * @return App version
     */
    public static String getCurrentAppVersion(Context context) {

        Utils.printLogs(TAG, "Inside getCurrentAppVersion()");

        try {

            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);

            String versionName = info.versionName;
            String versionCode = String.valueOf(info.versionCode);
            return versionName + "." + versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            Utils.printLogs(TAG, "Outside getCurrentAppVersion()");
            return null;
        }
    }

    /**
     * Prints the logs if the logs are on
     *
     * @param TAG     holds the name of the class or the
     * @param message holds the string of message to be printed
     */
    public static void printLogs(String TAG, String message) {

        if (Utils.LOG_ON)
            Log.d(String.valueOf(TAG), String.valueOf(message));
    }

    /**
     * Shows the toast for the widgets which are still under development
     *
     * @param context holds the context in which the toast is to be shown
     */
    public static void showUnderDevelopmentToast(Context context) {

        Toast.makeText(context, "Under Development...", Toast.LENGTH_SHORT).show();
    }

    /**
     * Shows the toast
     *
     * @param message holds the message to be shown in toast
     * @param context holds the context in which the toast is to be shown
     */
    public static void showToast(String message, Context context) {

        Toast.makeText(context, String.valueOf(message), Toast.LENGTH_SHORT).show();
    }

    /**
     * Returns the name of the calling method
     */
    public static void printCurrentMethodName() {
        Log.e("Calling Method", Thread.currentThread().getStackTrace()[3].getMethodName());
        //   Log.e("Current Method", Thread.currentThread().getStackTrace()[2].getMethodName());
        //   Log.e("", Thread.currentThread().getStackTrace()[0].getMethodName());
    }

    /**
     * Checks if the Internet Connection is available or not
     *
     * @param context holds the object for Context
     * @return true or false
     */
    public static boolean isNetworkAvailable(Context context) {

        Utils.printLogs(TAG, "Inside isNetworkAvailable()");

        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        Utils.printLogs(TAG, "Outside isNetworkAvailable()");

        return activeNetworkInfo != null && activeNetworkInfo.isAvailable()
                && activeNetworkInfo.isConnected();
    }

    /**
     * Returns the device's unique hardware ID
     *
     * @param context holds the object for Context
     * @return Device ID
     */
    public static String getDeviceUniqueID(Context context) {

        Utils.printLogs(TAG, "Inside getDeviceUniqueID()");

		/*TelephonyManager telephonyManager = (TelephonyManager)
         context.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();*/

        String deviceID = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        Utils.printLogs(TAG, "Outside getDeviceUniqueID()");

        return deviceID;
    }

    /**
     * Displays progress dialog
     *
     * @param context holds the object of context
     * @param message holds the message
     * @return object of Progress Dialog
     */
    public static ProgressDialog showProgressDialog(Context context, String message) {

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        if (message != null && message.length() > 0)
            progressDialog.show();
        return progressDialog;
    }

    /**
     * Gets the Image Intent for picking a image
     *
     * @param context holds the object for Context
     * @return Intent
     */
    public static Intent getPickImageIntent(Context context, String fileName) {
        Intent chooserIntent = null;

        List<Intent> intentList = new ArrayList<>();

        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File f = new File(
                Environment.getExternalStorageDirectory(),
                fileName);
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

        //takePhotoIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);

        intentList = addIntentsToList(context, intentList, pickIntent);
        intentList = addIntentsToList(context, intentList, takePhotoIntent);

        if (intentList.size() > 0) {
            chooserIntent = Intent.createChooser(intentList.remove(intentList.size() - 1),
                    "Take photo from");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                    intentList.toArray(new Parcelable[]{}));
        }

        return chooserIntent;
    }

    /**
     * Gets the Image Intent for picking a image
     *
     * @param context holds the object for Context
     * @return Intent
     */
    public static Intent getPickVideoIntent(Context context) {
        Intent chooserIntent = null;

        List<Intent> intentList = new ArrayList<>();

        Intent pickIntent = new Intent();
        pickIntent.setType("video/*");
        pickIntent.setAction(Intent.ACTION_GET_CONTENT);

        Intent captureVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        intentList = addIntentsToList(context, intentList, pickIntent);
        intentList = addIntentsToList(context, intentList, captureVideoIntent);

        if (intentList.size() > 0) {
            chooserIntent = Intent.createChooser(intentList.remove(intentList.size() - 1),
                    "Video");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                    intentList.toArray(new Parcelable[]{}));
        }

        return chooserIntent;
    }

    /**
     * Adds the intents to a list
     *
     * @param context holds the object for Context
     * @param list    holds list of intents
     * @param intent  holds the object for Intent
     * @return filtered intents
     */
    private static List<Intent> addIntentsToList(Context context,
                                                 List<Intent> list, Intent intent) {
        List<ResolveInfo> resInfo = context.getPackageManager()
                .queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resInfo) {
            String packageName = resolveInfo.activityInfo.packageName;
            Intent targetedIntent = new Intent(intent);
            targetedIntent.setPackage(packageName);
            list.add(targetedIntent);
        }
        return list;
    }

    /**
     * Retrieves the real path of video
     *
     * @param contentUri holds the object for URI
     * @param context    holds the object for Context
     * @return video path
     */
    public static String getRealPathOfVideoURI(Uri contentUri, Context context) {

        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri,
                filePathColumn, null, null, null);
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            return cursor.getString(columnIndex);
        }
        cursor.close();
        return null;
    }

    /**
     * Checks if the file exists or not
     *
     * @param fileName name of the file
     * @return File object
     */
    public static File checkForFileExistence(String fileName) {

        File file = new File(Environment.getExternalStorageDirectory()
                .toString());

        for (File temp : file.listFiles()) {
            if (temp.getName().equals(fileName)) {
                file = temp;
                break;
            }
        }
        return file;
    }

    /**
     * Calculates the screen size in inches
     *
     * @param context Holds the context of activity
     * @return Size of screen in inches
     */
    private double calculateScreenSize(Context context) {

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        double wi = (double) width / (double) dm.xdpi;
        double hi = (double) height / (double) dm.ydpi;
        double x = Math.pow(wi, 2);
        double y = Math.pow(hi, 2);
        return Math.sqrt(x + y);
    }

    public void processJSON(Object obj) {
        JSONObject jsonObj = null;
        JSONArray jsonArr = null;
        jsonObj = objectToJSONObject(obj);
        jsonArr = objectToJSONArray(obj);
        if (jsonObj != null) {
            //process JSONObject
        } else if (jsonArr != null) {
            //process JSONArray
        }
    }

    public static JSONObject objectToJSONObject(Object object) {
        Object json = null;
        JSONObject jsonObject = null;
        try {
            json = new JSONTokener(object.toString()).nextValue();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (json instanceof JSONObject) {
            jsonObject = (JSONObject) json;
        }
        return jsonObject;
    }

    public static JSONArray objectToJSONArray(Object object) {
        Object json = null;
        JSONArray jsonArray = null;
        try {
            json = new JSONTokener(object.toString()).nextValue();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (json instanceof JSONArray) {
            jsonArray = (JSONArray) json;
        }
        return jsonArray;
    }

    /**
     * Checks if the app is in background or not
     *
     * @param context Holds the object of context
     * @return the status of app
     */
    private boolean isAppIsInBackground(Context context) {

        boolean isInBackground = true;

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {

            List<ActivityManager.RunningAppProcessInfo> runningProcesses =
                    am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance ==
                        ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {

                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }


}
