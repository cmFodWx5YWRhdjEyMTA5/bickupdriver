package com.app.bickupdriver.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.bickupdriver.R;
import com.app.bickupdriver.activity.BookingDetailsAcceptRejectActivity;
import com.app.bickupdriver.activity.TrackDriverActivityDriver;
import com.app.bickupdriver.restservices.NotificationListModel;
import com.app.bickupdriver.utility.ConstantValues;
import com.app.bickupdriver.utility.Utils;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by manish on 15/5/18.
 */

public class NotificationRecyclerAdapter extends RecyclerView.Adapter<NotificationRecyclerAdapter.NotificationViewHolder> {

    private static final String TAG = "NOTIFICATION";
    private ArrayList<NotificationListModel> notificationList;
    private Context context;

    public NotificationRecyclerAdapter(ArrayList<NotificationListModel> notificationList, Context context) {
        this.notificationList = notificationList;
        this.context = context;

        /**
         * Display Latest Notification on the Top
         */
        Collections.reverse(notificationList);
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.single_row_notification_list, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        NotificationListModel notification = notificationList.get(position);
        holder.tvNotificationText.setText(notification.notify.notificationText);
        String dateTime = Utils.milliSecondToDateAndTime(notification.timeStamp);
        holder.tvNotificationTime.setText(dateTime);
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {

        public TextView tvNotificationText;
        public ImageView ivIsOnline;
        public TextView tvNotificationTime;
        public LinearLayout layout;

        public NotificationViewHolder(View itemView) {
            super(itemView);

            tvNotificationText = itemView.findViewById(R.id.tv_notification_text);
            tvNotificationTime = itemView.findViewById(R.id.tv_notification_time);
            ivIsOnline = itemView.findViewById(R.id.iv_is_online);
            layout = itemView.findViewById(R.id.layout_notification_list);


            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String rideId = notificationList.get(getAdapterPosition()).payload.rideId;
                    Utils.printLogs(TAG, "Ride id : " + rideId);
                    String notificationType = notificationList.get(getAdapterPosition()).notificationType;
                    Utils.printLogs(TAG, "Notification Type at adapter : " + notificationType);
                    if (notificationType.equals("2")) {
                        // Track Driver Page
                        Intent trackDriverIntent = new Intent(context, TrackDriverActivityDriver.class);
                        trackDriverIntent.putExtra(ConstantValues.RIDE_ID, rideId);
                        context.startActivity(trackDriverIntent);
                    } else if (notificationType.equals("1")) {
                        // Track Accept/Reject Page
                        Intent acceptRjectActivity = new Intent(context, BookingDetailsAcceptRejectActivity.class);
                        acceptRjectActivity.putExtra(ConstantValues.RIDE_ID, rideId);
                        context.startActivity(acceptRjectActivity);
                    }
                }
            });
        }
    }

}
