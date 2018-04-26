package com.app.bickupdriver.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.app.bickupdriver.DeliveryActivity;
import com.app.bickupdriver.R;
import com.app.bickupdriver.model.Response;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by fluper-pc on 29/9/17.
 */

public class BookingsAdapter extends BaseAdapter {
    private ArrayList<Response> responseList;
    private ArrayList<String> goodsList;
    private DeliveryActivity activity;
    private Activity activity1;
    private String[] array;
    private LayoutInflater inflater;
    int tabPosition;
    private TextView tvNoOfDeliveries, txt_today, row_user_name, today_txt, row_apx_fare;
    private CircleImageView user_image_row_list;
    private TextView edt_pickup_location, edt_drop_location;
    private RatingBar rbDriverRating;
    private CardView top_card;
    private Context context;
    private LinearLayout llRootLayout;

/*
    public BookingsAdapter(DeliveryActivity activity, String[] array, int tabPosition) {
        //this.goodsList=goodsList;
        this.tabPosition = tabPosition;
        this.array = array;
        this.activity = activity;
        inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
    }*/

    public BookingsAdapter(Context context, Activity activity, String[] array, int tabPosition,
                           ArrayList<Response> responseList) {
        //this.goodsList=goodsList;
        this.context = context;
        this.tabPosition = tabPosition;
        this.array = array;
        this.activity1 = activity;
        this.responseList = responseList;
        inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(responseList != null)
            return responseList.size();
        else
            return 0;
    }

    @Override
    public Response getItem(int i) {
        return responseList.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = null;

        if (tabPosition == 0) {
            rowView = inflater.inflate(R.layout.completed_deliveries_list_item, null);
            this.setWidgetReferences(rowView, tabPosition, i);
        } else if (tabPosition == 1) {
            rowView = inflater.inflate(R.layout.missed_deliveries_list_item, null);
            this.setWidgetReferences(rowView, tabPosition, i);
        }

        this.setDataOnViews(i);

        return rowView;
    }

    /**
     * Sets widget references with their ids
     * @param rowView Holds the view object
     * @param tabPosition Holds the tab Position number
     */
    private void setWidgetReferences(View rowView, int tabPosition, int position) {

       // if(tabPosition == 0) {

           // today_txt = rowView.findViewById(R.id.today_txt);
            tvNoOfDeliveries = rowView.findViewById(R.id.tvNoOfDeliveries);
           // llRootLayout = rowView.findViewById(R.id.llRootLayout);
            if(responseList.get(position).ride != null &&
                    responseList.get(position).ride.size() > 0) {

            }

      /*  } else if(tabPosition == 1) {

            txt_today = rowView.findViewById(R.id.txt_today);
            top_card = rowView.findViewById(R.id.top_card);
            tvNoOfDeliveries = rowView.findViewById(R.id.tvNoOfDeliveries);
            user_image_row_list = rowView.findViewById(R.id.user_image_row_list);
            row_user_name = rowView.findViewById(R.id.row_user_name);
            edt_pickup_location = rowView.findViewById(R.id.edt_pickup_location);
            edt_drop_location = rowView.findViewById(R.id.edt_drop_location);
        }*/
    }

    /**
     * Sets the data on the views according to the tab position
     * @param position Holds the position
     */
    private void setDataOnViews(int position) {

       // if(tabPosition == 0) {

            tvNoOfDeliveries.setText(responseList.get(position).ride.size() + " Delivery(s)");
           // today_txt.setText(responseList.get(position).date);

            //Log.e("BookingsAdapter", responseList.get(0).ride.get(0).name);
           /* if(responseList.get(position).ride != null &&
                    responseList.get(position).ride.size() > 0) {
                LayoutInflater layoutInflater = (LayoutInflater)
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View view = layoutInflater.inflate(R.layout.delivery_list_item_layout,
                        llRootLayout, false);

                *//*user_image_row_list = view.findViewById(R.id.user_image_row_list);
                row_user_name = view.findViewById(R.id.row_user_name);
                edt_pickup_location = view.findViewById(R.id.edt_pickup_location);
                edt_drop_location = view.findViewById(R.id.edt_drop_location);
                row_apx_fare = view.findViewById(R.id.row_apx_fare);
                rbDriverRating = view.findViewById(R.id.rbDriverRating);*//*

                ArrayList<Ride> rideList = responseList.get(position).ride;
                for(int i = 0; i < rideList.size(); i ++) {
                    Log.e("BookingsAdapter", "Static: " +
                            responseList.get(0).ride.get(0).name);
                    Log.e("BookingsAdapter", rideList.get(i).name);
                   // row_user_name.setText(rideList.get(i).name);
                }
              //  llRootLayout.addView(view);
            }*/
       // } /*else if(tabPosition == 1) {

       // }*/
    }

/*
    private void showPopUp(int choosetraveller) {
        final Dialog openDialog = new Dialog(activity);
        openDialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.rate_driver_dialog);
        openDialog.setTitle("Custom Dialog Box");
        openDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
       *//* TextView travellerName = (TextView)openDialog.findViewById(R.id.txt_traveller_name_dialog);*//*

       *//* TextView travellerCost = (TextView)openDialog.findViewById(R.id.txt_traveller_cost);
        ImageView travellerImage = (ImageView)openDialog.findViewById(R.id.img_traveller);
        Button btnDisagree = (Button)openDialog.findViewById(R.id.btn_disagree);
*//*
        Button btnAgree = (Button)openDialog.findViewById(R.id.btn_agree);
       *//* travellerName.setTypeface(mTypefaceBold);
        travellerCost.setTypeface(mTypefaceRegular);
        btnDisagree.setTypeface(mTypefaceBold);
        btnAgree.setTypeface(mTypefaceBold);*//*
      *//*  if(choosetraveller==1){

        }*//*
     *//*   btnDisagree.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                openDialog.dismiss();
            }
        });*//*
        btnAgree.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                openDialog.dismiss();

            }
        });
        openDialog.show();

    }


    @SuppressLint("ResourceType")
    private void flipCard() {
        activity.getFragmentManager().beginTransaction()
                // Replace the default fragment animations with animator resources representing
                // rotations when switching to the back of the card, as well as animator
                // resources representing rotations when flipping back to the front (e.g. when
                // the system Back button is pressed).
                .setCustomAnimations(R.anim.flip_animation, R.anim.flip_animation2)

                // Replace any fragments currently in the container view with a fragment
                // representing the next page (indicated by the just-incremented currentPage
                // variable).
                .add(R.id.booking_container,new BlankFragment())
               .commit();
    }*/
}
