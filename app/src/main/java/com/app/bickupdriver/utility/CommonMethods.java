package com.app.bickupdriver.utility;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fluper-pc on 14/9/17.
 */

public class CommonMethods {
    private static final CommonMethods ourInstance = new CommonMethods();

    public static CommonMethods getInstance() {
        return ourInstance;
    }

    private CommonMethods() {
    }

    // Hide keyboard
    public void hideSoftKeyBoard(Activity activity){
        InputMethodManager inputMethodManager=(InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view=activity.getCurrentFocus();
        if(view!=null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

  // save data to preferences
    public void saveDataToPreferences(Context context, String preferenceName, HashMap<String,String> hashMapFordata){
        if(context!=null&&hashMapFordata!=null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceName, 0);
            SharedPreferences.Editor preferenceEditor=sharedPreferences.edit();
            if(hashMapFordata.size()>0) {
                Set keys = hashMapFordata.keySet();
                for (Iterator i = keys.iterator(); i.hasNext(); ) {
                    String key = (String) i.next();
                    String value = (String) hashMapFordata.get(key);
                    preferenceEditor.putString(key,value);

                }
                preferenceEditor.commit();
            }

        }
    }


    // clear Preferences on session out
    public void clearSharePreferences(Context context, String preferenceName){
        if(context!=null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceName, 0);
            SharedPreferences.Editor preferenceEditor = sharedPreferences.edit();
            preferenceEditor.clear();
            preferenceEditor.commit();
        }
    }

    //Validate email address
    public boolean validateEmailAddress(String email){
        boolean isValid = false;
        String expression ="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;

    }

    // Validate date
    public boolean isThisDateValid(String dateToValidate, String dateFromat){
        if(dateToValidate == null){
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
        sdf.setLenient(false);
        try {
            //if not valid, it will throw ParseException
            sdf.parse(dateToValidate);

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    // Validate Expiry date
    public boolean validateExpiredDate(String date){
        try {
            Date expirydate=new SimpleDateFormat(ConstantValues.DATE_FORMAT).parse(date);
            boolean isvalid=expirydate.after(new Date());
            if (isvalid) {
                return true;}
            else {
                return false;}
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }



   // validate mobile number
    public boolean validateMobileNumber(String mobilenumber,int length){
        boolean isValid=false;
        if(mobilenumber!=null) {
            if (mobilenumber.length() >= length) {
                isValid = true;
            }
        }
        return isValid;
    }

   // validate edit text feild data
    public boolean validateEditFeild(String feilddata){
        boolean isValid=false;
        if(feilddata!=null) {
            if (feilddata.length() > 0) {
                isValid = true;
            }
        }
        return isValid;

    }


    public  String getTimeStamp(String date){
        long unixTimestamp=0;
        DateFormat df = new SimpleDateFormat(ConstantValues.DATE_FORMAT);
        Date startDate;
        try {
           startDate = df.parse(date);
           unixTimestamp=startDate.getTime()/1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return String.valueOf(unixTimestamp);
    }

    public String getDatafromTimeStamp(String timeStamp){
        long unixSeconds = 0;
        try {
           unixSeconds= Long.parseLong(timeStamp);
        }catch (Exception e){

        }
        Date date = new Date(unixSeconds*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat(ConstantValues.DATE_FORMAT); // the format of your date
        String formattedDate = sdf.format(date);
      return formattedDate;
    }

    //check Internet connection when activity gets open
    public boolean checkInterNetConnection(Context context){
        ConnectivityManager mConnectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworKInfo=mConnectivityManager.getActiveNetworkInfo();
        boolean isConnected=mNetworKInfo!=null&&mNetworKInfo.isConnectedOrConnecting();
        return isConnected;
    }

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public  Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    /**
     * Retrieves the unique device id of the device
     * @param context Holds the Context object
     * @return Unique Device ID
     */
    public static String getUniqueDeviceId(Context context) {

        String deviceId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return deviceId;
    }
}
