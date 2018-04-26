package com.app.bickupdriver.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.app.bickupdriver.GoodsActivity;
import com.app.bickupdriver.R;
import com.app.bickupdriver.SplashActivity;
import com.app.bickupdriver.utility.ConstantValues;
import com.app.bickupdriver.utility.Utils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <H1>Bickup DriverBickup</H1>
 * <H1>CustomFirebaseMessagingService</H1>
 * <p>
 * <p>Service that receives the FCM messages and filters them accordingly</p>
 *
 * @author Divya Thakur
 * @version 1.0
 * @since 5/18/17
 */
public class CustomFirebaseMessagingService extends FirebaseMessagingService {

    //private final String TAG = getClass().getSimpleName();
    private final String TAG = "FIREBASE";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Utils.printLogs(TAG, "Remote Message : " + remoteMessage);
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Utils.printLogs(TAG, "From: " + remoteMessage.getFrom());
        Utils.printLogs(TAG, "Data: " + remoteMessage.getData());
        RemoteMessage.Notification notification1 = remoteMessage.getNotification();
        if (notification1 != null) {
            Utils.printLogs(TAG, "Body: " + remoteMessage.getNotification().getBody());
        } else {
            Utils.printLogs(TAG, "Notification is null  ");
        }


        if (remoteMessage.getData() != null) {
            JSONObject notification;

            try {
                notification = new JSONObject(remoteMessage.getData().toString());

                //JSONObject data = notification.getJSONObject("data");
                String notificationType = notification.getString(ConstantValues.NOTIFICATION_TYPE);
                String rideId = notification.getString(ConstantValues.RIDE_ID);

                Utils.printLogs(TAG, "Notification Type Notification : " + notificationType);
                Utils.printLogs(TAG, "Ride Id notification : " + rideId);

                Class toBeOpenedClass = this.filterNotifications(Integer.parseInt(notificationType));

                this.sendNotification(remoteMessage.getNotification().getBody(), rideId, toBeOpenedClass, notificationType);

            } catch (JSONException e) {
                Utils.printLogs(TAG, "Exception : " + e.getMessage());
                e.printStackTrace();
            }
            Utils.printLogs(TAG, "Message Notification Body: " + remoteMessage.getData());
        }
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody, String rideId, Class toBeOpenedClass,
                                  String status) {

        Intent intent = new Intent(this, toBeOpenedClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(ConstantValues.RIDE_ID, rideId);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(getNotificationIcon())
                .setContentTitle(getString(R.string.app_name))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(messageBody));

        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT
                >= android.os.Build.VERSION_CODES.LOLLIPOP);

        if (useWhiteIcon) {
            notificationBuilder.setColor(Color.CYAN);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    /**
     * Checks for the Android OS version for using white square as a logo of notification
     *
     * @return id for the icon to be used as logo
     */
    private int getNotificationIcon() {

        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT
                >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.ic_launcher_round : R.mipmap.ic_launcher;
        //return R.drawable.app_icon;
    }

    /**
     * Filters notification
     *
     * @param status Holds the status
     * @return the corresponding class to be moved on
     */
    private Class filterNotifications(int status) {

        switch (status) {
            case 1:
                // DriverBickup Side
                // When user creates a Ride and a push sent to driver
                Utils.printLogs(TAG, "Status 1 ");
                return GoodsActivity.class;
            case 2:
                // DriverBickup Side
                // When user assigns another driver.
                Utils.printLogs(TAG, "Status 2 ");
            case 3:
                // User Side
                // When Request accepted by DriverBickup.
            default:
                Utils.printLogs(TAG, "Status 3 ");
                return SplashActivity.class;
        }

        /*switch (status) {
            case 1:
                return GoodsActivity.class;
            case 2:
            case 3:
            case 6:
                return MainActivity.class;
            case 9:
                return MainActivity.class;
            default:
                return SplashActivity.class;
        }*/
    }
}
