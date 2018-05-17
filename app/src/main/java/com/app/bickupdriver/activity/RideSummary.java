package com.app.bickupdriver.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.bickupdriver.R;
import com.app.bickupdriver.model.Ride;
import com.app.bickupdriver.model.RideObjectResponse;
import com.app.bickupdriver.model.User;
import com.app.bickupdriver.restservices.ApiClientConnection;
import com.app.bickupdriver.restservices.ApiInterface;
import com.app.bickupdriver.restservices.ServerResponseRideDetails;
import com.app.bickupdriver.utility.ConstantValues;
import com.app.bickupdriver.utility.MapUtils;
import com.app.bickupdriver.utility.Utils;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RideSummary extends AppCompatActivity implements View.OnClickListener,
        OnMapReadyCallback, RoutingListener {

    private Toolbar toolbar;
    private TextView tvToolbarText;
    private ImageView ivBackImage;
    //private Button btnProceedToPay;
    //private RadioButton rbCard;
    //private RadioButton rbCod;
    //private RadioGroup rgPaymentType;
    private String TAG = "BRAINTREE";
    private String rideId;

    // UI
    private TextView tvPickupAddress;
    private TextView tvDropAddress;
    private TextView tvRideCompletionTime;
    private TextView tvDriverName;
    private TextView tvRideFare;
    private TextView tvTollTax;
    private TextView tvHelperFare;
    private TextView tvTotalFare;
    private int tollTax = 24;

    // Google Map
    private GoogleMap googleMap;
    private List<Polyline> polylines;
    private int totalFare;
    private LinearLayout layoutPayment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_summary);
        getId();
        setListener();
        setData();


        getRideDetailsFromApi();
    }

    private void getRideDetailsFromApi() {
        if (Utils.isNetworkAvailable(this)) {
            ApiInterface apiService = null;
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.show();
            try {
                apiService = ApiClientConnection.getInstance().createService();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Call<RideObjectResponse> call = apiService.getParticularRideDetails(User.getInstance().getAccesstoken(), rideId);
            call.enqueue(new Callback<RideObjectResponse>() {
                @Override
                public void onResponse(Call<RideObjectResponse> call, Response<RideObjectResponse> response) {
                    progressDialog.dismiss();
                    if (response.isSuccessful()) {
                        Utils.printLogs(TAG, "Ride information Retrieved successfully ... ");
                        RideObjectResponse serverResponseRideDetails = response.body();
                        Ride ride = serverResponseRideDetails.ride;
                        updateUI(ride);
                    } else {
                        try {
                            Utils.printLogs(TAG, "Some Error Occurs while getting ride details : " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<RideObjectResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Utils.printLogs(TAG, "onFailure while getting ride details : " + t.getMessage());
                }
            });
        } else {
            Utils.showToast("No Internet Connection", this);
        }
    }

    private void updateUI(Ride ride) {
        tvPickupAddress.setText(ride.pickupLocationAddress);
        tvDropAddress.setText(ride.dropLocationAddress);
        String dateAndTime = Utils.milliSecondToDateAndTime(ride.timestamp);
        tvRideCompletionTime.setText(dateAndTime);
        tvDriverName.setText(ride.name);

        tvRideFare.setText("" + ride.totalPrice + " $");
        tvTollTax.setText("" + tollTax + " $");

        int noOfHelpers = ride.noOfHelpers;
        int helperFare = noOfHelpers * 5;
        tvHelperFare.setText("" + helperFare + " $");
        totalFare = (int) ride.totalPrice + tollTax + helperFare;
        tvTotalFare.setText("" + totalFare + " $");

        //btnProceedToPay.setText("Pay $" + totalFare);

        double pickupLatitude = Double.parseDouble(ride.pickupLatitude);
        double pickupLongitude = Double.parseDouble(ride.pickupLongitude);
        double dropLatitude = Double.parseDouble(ride.dropLatitude);
        double dropLongitude = Double.parseDouble(ride.dropLongitude);

        LatLng pickupLatLng = new LatLng(pickupLatitude, pickupLongitude);
        LatLng dropLatLng = new LatLng(dropLatitude, dropLongitude);


        /**
         * Zoom the Map
         */
        List<LatLng> latLngList = new ArrayList<>();
        latLngList.add(pickupLatLng);
        latLngList.add(dropLatLng);
        zoomRoute(googleMap, latLngList);

        /**
         * Draw Route Between Two Places
         */
        drawRouteBetweenTwoPlaces(pickupLatLng, dropLatLng);
    }


    public static void zoomRoute(GoogleMap googleMap, List<LatLng> lstLatLngRoute) {

        if (googleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;
        googleMap.setPadding(100, 100, 100, 150);   // left, top, right, bottom
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLngPoint : lstLatLngRoute)
            boundsBuilder.include(latLngPoint);

        int routePadding = 10;
        LatLngBounds latLngBounds = boundsBuilder.build();

        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding));
    }

    private void drawRouteBetweenTwoPlaces(LatLng pickupLatLng, LatLng dropLatLng) {
        if (pickupLatLng != null) {
            if (dropLatLng != null) {
                MapUtils.addMarker(googleMap, pickupLatLng, "", R.drawable.pin_location_pin);
                MapUtils.addMarker(googleMap, dropLatLng, "", R.drawable.drop_location_pin);

                Routing routing = new Routing.Builder()
                        .travelMode(Routing.TravelMode.DRIVING)
                        .withListener(this)
                        .waypoints(pickupLatLng, dropLatLng)
                        .key(getString(R.string.google_maps_key))
                        .build();
                routing.execute();

            } else {
                Utils.printLogs(TAG, "Drop LatLng is null");
            }
        } else {
            Utils.printLogs(TAG, "Pickup LatLng is null");
        }
    }

    private void getId() {
        toolbar = findViewById(R.id.include_toolbar_payment_screen);
        tvToolbarText = toolbar.findViewById(R.id.txt_activty_header);
        ivBackImage = toolbar.findViewById(R.id.backImage_header);
        //btnProceedToPay = findViewById(R.id.btn_proceed_to_pay);
        //rbCard = findViewById(R.id.rb_card_payment);
        //rbCod = findViewById(R.id.rb_cod_payment);

        tvPickupAddress = findViewById(R.id.tv_pickup_location_ride_history);
        tvDropAddress = findViewById(R.id.tv_drop_location_ride_history);
        tvRideCompletionTime = findViewById(R.id.tv_ride_completion_time);
        tvDriverName = findViewById(R.id.tv_driver_name);

        tvRideFare = findViewById(R.id.tv_ride_fare);
        tvTollTax = findViewById(R.id.tv_toll_tax);
        tvHelperFare = findViewById(R.id.tv_helper_fare);
        tvTotalFare = findViewById(R.id.tv_total_fare);
        //rgPaymentType = findViewById(R.id.rg_payment_type);
        //layoutPayment = findViewById(R.id.layout_payment);
    }

    private void setListener() {
        ivBackImage.setOnClickListener(this);
        //btnProceedToPay.setOnClickListener(this);
    }

    private void setData() {
        tvToolbarText.setText("Ride Summary");

        rideId = getIntent().getStringExtra(ConstantValues.RIDE_ID);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.google_map_payment_info);
        SupportMapFragment supportMapFragment = (SupportMapFragment) fragment;
        supportMapFragment.getMapAsync(this);
        polylines = new ArrayList<>();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backImage_header:
                onBackPressed();
                break;

            /*case R.id.btn_proceed_to_pay:

                break;*/
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        Utils.printLogs(TAG, "onRoutingFailure : " + e.getMessage());
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int i1) {

        Utils.printLogs(TAG, "Routing Success .. ");
        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i < route.size(); i++) {
            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(R.color.blue));
            polyOptions.width(Utils.THICKNESS_OF_POLYLINE);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = googleMap.addPolyline(polyOptions);
            polylines.add(polyline);
        }
    }

    @Override
    public void onRoutingCancelled() {

    }
}
