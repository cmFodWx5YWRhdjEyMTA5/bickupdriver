package com.app.bickupdriver.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.bickupdriver.GoodsActivity;
import com.app.bickupdriver.R;
import com.app.bickupdriver.TrackDriverActivity;
import com.app.bickupdriver.model.Ride;
import com.app.bickupdriver.utility.ConstantValues;
import com.app.bickupdriver.utility.DateHelper;

import java.util.ArrayList;

/**
 * Created by fluper-pc on 25/10/17.
 */
public class RideListRecyclerAdapter extends RecyclerView.Adapter<RideListRecyclerAdapter.MyViewHolder> {

    private ArrayList<Ride> rideList;
    private Activity activity;

    public RideListRecyclerAdapter(Activity activity, ArrayList<Ride> rideList) {
        this.activity = activity;
        this.rideList = rideList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtOngoing, tvBookingCode, tvFromLocation,
                tvPrice, tvDestinationLocation;
        private TextView tvDate, tvTime, tvDistance, tvCurrentBookingNo;
        private RelativeLayout rlcontainer;

        public MyViewHolder(View view) {

            super(view);
            rlcontainer = view.findViewById(R.id.content_container);
            txtOngoing = view.findViewById(R.id.txt_ongoing);
            tvBookingCode = view.findViewById(R.id.booking_code);
            tvFromLocation = view.findViewById(R.id.edt_from);
            tvPrice = view.findViewById(R.id.txt_dhz);
            tvDestinationLocation = view.findViewById(R.id.edt_to);
            tvDate = view.findViewById(R.id.date_time);
            tvTime = view.findViewById(R.id.time);
            tvDistance = view.findViewById(R.id.distance);
            tvCurrentBookingNo = view.findViewById(R.id.tvCurrentBookingNo);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_bookings, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.tvCurrentBookingNo.setText((position + 1) + "/" + getItemCount());
        holder.tvBookingCode.setText(rideList.get(position).rideId);
        holder.tvPrice.setText(rideList.get(position).totalPrice + " DHZ");
        holder.tvDestinationLocation.setText(rideList.get(position).dropLocationAddress);
        holder.tvFromLocation.setText(rideList.get(position).pickupLocationAddress);
        String date = DateHelper.convertTimestampToDate("dd/MM/yyyy",
                rideList.get(position).timestamp);
        holder.tvDate.setText(date);
        holder.tvDistance.setText(rideList.get(position).distance + " km");

        switch (rideList.get(position).rideCompletedStatus) {
            case 0:
                holder.txtOngoing.setText("Pending");
                break;
            case 1:
                holder.txtOngoing.setText("Ongoing");
                break;
            case 2:
                //holder.txtOngoing.setText("Completed");
                break;
            case 3:
                holder.txtOngoing.setText("Schd.");
                break;
        }

        // Prashant
        /*if(position == 0){
            holder.txtOngoing.setText(activity.getString(R.string.txt_ongoing));
        }else {
            holder.txtOngoing.setText(activity.getString(R.string.txt_waiting));

        }*/

        holder.rlcontainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rideList.get(position).rideCompletedStatus == 1) {
                    Intent intent = new Intent(activity, TrackDriverActivity.class);
                    intent.putExtra("ride", rideList.get(position));
                    activity.startActivity(intent);
                } else {
                    Intent intent = new Intent(activity, GoodsActivity.class);
                    String ride_id = rideList.get(position).rideId;
                    intent.putExtra(ConstantValues.RIDE_ID, ride_id);
                    activity.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (this.rideList != null)
            return rideList.size();
        else
            return 0;
    }
}
