package com.app.bickupdriver.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.bickupdriver.R;
import com.app.bickupdriver.model.Response;
import com.app.bickupdriver.model.Ride;
import com.app.bickupdriver.utility.ConstantValues;
import com.app.bickupdriver.utility.Utils;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * <H1>Bickup DriverBickup</H1>
 * <H1>DeliveriesAdapter</H1>
 * <p>
 * <p>Represents the Booking List Adapter which helps in listing the bookings</p>
 *
 * @author Divya Thakur
 * @version 1.0
 * @since 12/12/17
 */
@SuppressWarnings("deprecation")
public class DeliveriesAdapter extends BaseAdapter {

    //private final String TAG = getClass().getSimpleName();
    private ViewHolderCompleted holderCompleted = null;
    private ArrayList<Response> responseList = null;
    private Context context = null;
    private int tabPosition;
    private ViewHolderMissed holderMissed = null;

    /**
     * Parameterized Constructor
     *
     * @param context      holds the context
     * @param responseList holds the response list
     */
    public DeliveriesAdapter(Context context, int tabPosition, ArrayList<Response> responseList) {

        this.context = context;
        this.tabPosition = tabPosition;
        this.responseList = responseList;
    }

    @Override
    public int getCount() {
        if (responseList != null)
            return responseList.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        if (responseList != null)
            return responseList.get(position);
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        try {

            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {

                if (tabPosition == 0) {
                    convertView = inflater.inflate(R.layout.completed_deliveries_list_item,
                            null);
                    holderCompleted = new ViewHolderCompleted();
                    this.setWidgetReferencesForCompleted(convertView);
                    convertView.setTag(holderCompleted);
                    this.setTagForCompleted(convertView);
                } else if (tabPosition == 1) {
                    convertView = inflater.inflate(R.layout.missed_deliveries_list_item,
                            null);
                    holderMissed = new ViewHolderMissed();
                    this.setWidgetReferencesForMissed(holderMissed, convertView);
                    convertView.setTag(holderMissed);
                    this.setTagForMissed(holderMissed, convertView);
                }
            } else {
                if (tabPosition == 0)
                    holderCompleted = (ViewHolderCompleted) convertView.getTag();
                else if (tabPosition == 1)
                    holderMissed = (ViewHolderMissed) convertView.getTag();
            }
            if (tabPosition == 0)
                this.editViewsForCompleted(holderCompleted, position);
            else
                this.editViewsForMissed(holderMissed, position);


        } catch (Exception e) {
            e.printStackTrace();
            Utils.printLogs("HISTORY", "Exception in Delivery Adapter : " + e.getMessage());

        }

        return convertView;
    }

    /**
     * Identifying the views by using their ID's and setting the references
     *
     * @param view holds the view
     */
    private void setWidgetReferencesForCompleted(View view) {

        try {
            holderCompleted.tvDate = view.findViewById(R.id.tvDate);
            holderCompleted.tvNoOfDeliveries = view.findViewById(R.id.tvNoOfDeliveries);
            holderCompleted.rlDateDeliveryLayout = view.findViewById(R.id.rlDateDeliveryLayout);
            holderCompleted.llRootLayout = view.findViewById(R.id.llRootLayout);
        } catch (Exception e) {
            Utils.printLogs("HISTORY", "Exception in Delivery Adapter : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Identifying the views by using their ID's and setting the references
     *
     * @param holderMissed Holds the View Holder
     * @param view         holds the view
     */
    private void setWidgetReferencesForMissed(ViewHolderMissed holderMissed, View view) {

        try {
            holderMissed.txt_today = view.findViewById(R.id.txt_today);
            holderMissed.tvNoOfDeliveries = view.findViewById(R.id.tvNoOfDeliveries);
            holderMissed.rlDateDeliveryLayout = view.findViewById(R.id.rlDateDeliveryLayout);
            holderMissed.llRootLayout = view.findViewById(R.id.llRootLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the tags to the views
     *
     * @param view holds the view
     */
    private void setTagForCompleted(View view) {

        try {

            view.setTag(holderCompleted);
            view.setTag(R.id.tvDate, holderCompleted.tvDate);
            view.setTag(R.id.tvNoOfDeliveries, holderCompleted.tvNoOfDeliveries);
            view.setTag(R.id.rlDateDeliveryLayout, holderCompleted.rlDateDeliveryLayout);
            view.setTag(R.id.llRootLayout, holderCompleted.llRootLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the tags to the views
     *
     * @param holderMissed Holds the View Holder
     * @param view         holds the view
     */
    private void setTagForMissed(ViewHolderMissed holderMissed, View view) {

        try {

            view.setTag(holderMissed);
            view.setTag(R.id.txt_today, holderMissed.txt_today);
            view.setTag(R.id.tvNoOfDeliveries, holderMissed.tvNoOfDeliveries);
            view.setTag(R.id.rlDateDeliveryLayout, holderMissed.rlDateDeliveryLayout);
            view.setTag(R.id.llRootLayout, holderMissed.llRootLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the data on the views
     *
     * @param holderCompleted Holds the object for View Holder
     * @param position        holds the position
     */
    private void editViewsForCompleted(ViewHolderCompleted holderCompleted, final int position) {

        try {

            holderCompleted.tvDate.setText(responseList.get(position).date);

            if (responseList.get(position).ride != null &&
                    responseList.get(position).ride.size() > 0) {

                holderCompleted.tvNoOfDeliveries.setText(responseList.get(position).ride.size()
                        + " Delivery(s)");

                LayoutInflater layoutInflater = (LayoutInflater)
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View view = layoutInflater.inflate(R.layout.delivery_list_item_layout,
                        holderCompleted.llRootLayout, false);

                CircleImageView user_image_row_list = view.findViewById(R.id.user_image_row_list);
                TextView row_user_name = view.findViewById(R.id.row_user_name);
                TextView edt_pickup_location = view.findViewById(R.id.edt_pickup_location);
                TextView edt_drop_location = view.findViewById(R.id.edt_drop_location);
                TextView row_apx_fare = view.findViewById(R.id.row_apx_fare);
                RatingBar rbDriverRating = view.findViewById(R.id.rbDriverRating);

                ArrayList<Ride> rideList = responseList.get(position).ride;
                for (int i = 0; i < rideList.size(); i++) {

                    user_image_row_list.setId(i + position + 777);
                    row_user_name.setId(i + position + 555);
                    edt_pickup_location.setId(i + position + 333);
                    edt_drop_location.setId(i + position + 111);
                    row_apx_fare.setId(i + position + 999);
                    rbDriverRating.setId(i + position + 1111);

                    if (rideList.get(i).goodsImage != null &&
                            rideList.get(i).goodsImage.imageUrl != null) {
                        Ion.with(user_image_row_list)
                                .placeholder(R.drawable.ac_sing_helper)
                                .load(ConstantValues.BASE_URL + "/" +
                                        rideList.get(i).goodsImage.imageUrl);
                    }

                    row_user_name.setText(rideList.get(i).name);
                    edt_pickup_location.setText(rideList.get(i).pickupLocationAddress);
                    edt_drop_location.setText(rideList.get(i).dropLocationAddress);
                    row_apx_fare.setText(String.valueOf(rideList.get(i).totalPrice) + " DHZ");
                    rbDriverRating.setRating(Float.valueOf(rideList.get(i).rating));
                }
                holderCompleted.llRootLayout.addView(view);
            }

            //   this.setImage(position);

            //holderCompleted.tvNotificationTitle.setTag(position);
            holderCompleted.tvDate.setTag(position);     // This line is important
            holderCompleted.tvNoOfDeliveries.setTag(position);
            holderCompleted.rlDateDeliveryLayout.setTag(position);
            holderCompleted.llRootLayout.setTag(position);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the data on the views
     *
     * @param holderMissed Holds the object for View Holder
     * @param position     holds the position
     */
    private void editViewsForMissed(ViewHolderMissed holderMissed, final int position) {

        try {

            holderMissed.txt_today.setText(responseList.get(position).date);
            if (responseList.get(position).ride != null &&
                    responseList.get(position).ride.size() > 0) {
                holderMissed.tvNoOfDeliveries.setText(responseList.get(position).ride.size()
                        + " Delivery(s)");

                LayoutInflater layoutInflater = (LayoutInflater)
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                holderMissed.llRootLayout.removeAllViews();

                ArrayList<Ride> rideList = responseList.get(position).ride;
                for (int i = 0; i < rideList.size(); i++) {

                    final View view = layoutInflater.inflate(
                            R.layout.missed_delivery_list_item_layout,
                            holderMissed.llRootLayout, false);

                    CircleImageView user_image_row_list = view.findViewById(R.id.user_image_row_list);
                    TextView row_user_name = view.findViewById(R.id.row_user_name);
                    TextView edt_pickup_location = view.findViewById(R.id.edt_pickup_location);
                    TextView edt_drop_location = view.findViewById(R.id.edt_drop_location);

                    user_image_row_list.setId(i + position + 777);
                    row_user_name.setId(i + position + 555);
                    edt_pickup_location.setId(i + position + 333);
                    edt_drop_location.setId(i + position + 111);

                    if (rideList.get(i).goodsImage != null &&
                            rideList.get(i).goodsImage.imageUrl != null) {
                        Ion.with(user_image_row_list)
                                .placeholder(R.drawable.ac_sing_helper)
                                .load(ConstantValues.BASE_URL + "/" +
                                        rideList.get(i).goodsImage.imageUrl);
                    }

                    row_user_name.setText(rideList.get(i).name);
                    edt_pickup_location.setText(rideList.get(i).pickupLocationAddress);
                    edt_drop_location.setText(rideList.get(i).dropLocationAddress);
                    holderMissed.llRootLayout.addView(view);
                }
            }

            // this.setImage(position);

            holderMissed.txt_today.setTag(position);     // This line is important
            holderMissed.tvNoOfDeliveries.setTag(position);
            holderMissed.rlDateDeliveryLayout.setTag(position);
            holderMissed.llRootLayout.setTag(position);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Public view holder class to hold the data
     */
    public class ViewHolderCompleted {

        TextView tvDate;
        TextView tvNoOfDeliveries;
        RelativeLayout rlDateDeliveryLayout;
        LinearLayout llRootLayout;
    }

    /**
     * Public view holder class to hold the data
     */
    public class ViewHolderMissed {

        TextView txt_today;
        TextView tvNoOfDeliveries;
        RelativeLayout rlDateDeliveryLayout;
        LinearLayout llRootLayout;
    }
}
