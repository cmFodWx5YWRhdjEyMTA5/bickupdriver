package com.app.bickupdriver.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.app.bickupdriver.activity.BookingDetailsAcceptRejectActivity;
import com.app.bickupdriver.R;
import com.app.bickupdriver.activity.SplashActivity;
import com.app.bickupdriver.utility.ConstantValues;
import com.app.bickupdriver.utility.Utils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

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
            try {
                Map<String, String> notificationData = remoteMessage.getData();
                String notificationType = notificationData.get("notification_type");
                String rideId = notificationData.get(ConstantValues.RIDE_ID);
                String pickupTime = notificationData.get("pickup_time");
                String pickupTimeType = notificationData.get("pickup_time_type");
                String message = notificationData.get("message");
                String title = notificationData.get("title");
                String color = notificationData.get("color");

                Utils.printLogs(TAG, "Notification Type Notification : " + notificationType);
                Utils.printLogs(TAG, "Ride Id notification : " + rideId);
                Utils.printLogs(TAG, "Pickup Time  : " + pickupTime);
                Utils.printLogs(TAG, "Pickup Time Type : " + pickupTimeType);
                Utils.printLogs(TAG, "Title : " + title);
                Utils.printLogs(TAG, "Color : " + color);

                /**
                 * Automatic Update when a new Notification is arrived.
                 */
                Intent intent = new Intent();
                intent.putExtra(ConstantValues.RIDE_ID, rideId);
                intent.setAction(Utils.GOODS_ACTIVITY_NOTIFICATION_BROADCAST_ACTION);
                sendBroadcast(intent);
                /////----------------


                Class toBeOpenedClass = this.filterNotifications(Integer.parseInt(notificationType));
                this.sendNotification(title, message, rideId, toBeOpenedClass, notificationType, color);

            } catch (Exception e) {
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
    private void sendNotification(String title, String messageBody, String rideId, Class toBeOpenedClass,
                                  String status, String color) {

        Intent intent = new Intent(this, toBeOpenedClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(ConstantValues.RIDE_ID, rideId);


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = "com.app.bickupdriver";
        String channelName = "bickup_driver";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(getNotificationIcon())
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setColor(Color.parseColor(color))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(messageBody));

        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT
                >= android.os.Build.VERSION_CODES.LOLLIPOP);

        if (useWhiteIcon) {
            notificationBuilder.setColor(Color.CYAN);
        }

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
                return BookingDetailsAcceptRejectActivity.class;
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
    }
}
