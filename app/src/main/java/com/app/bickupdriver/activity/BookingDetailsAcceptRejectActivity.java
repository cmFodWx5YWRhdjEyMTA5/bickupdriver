package com.app.bickupdriver.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.bickupdriver.R;
import com.app.bickupdriver.adapter.GoodsImageListAdapter;
import com.app.bickupdriver.adapter.TypesOfGoodsAdapter;
import com.app.bickupdriver.controller.AppConstants;
import com.app.bickupdriver.model.GoodsImage;
import com.app.bickupdriver.model.Ride;
import com.app.bickupdriver.model.RideObjectResponse;
import com.app.bickupdriver.model.ServerResult;
import com.app.bickupdriver.restservices.ApiClientConnection;
import com.app.bickupdriver.restservices.ApiInterface;
import com.app.bickupdriver.utility.ConstantValues;
import com.app.bickupdriver.utility.DateHelper;
import com.app.bickupdriver.utility.GpsTracker;
import com.app.bickupdriver.utility.MapUtils;
import com.app.bickupdriver.utility.Utils;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BookingDetailsAcceptRejectActivity extends AppCompatActivity implements View.OnClickListener, RoutingListener, OnMapReadyCallback {

    private TextView txtHeader;
    private boolean mIsConnected;
    private Snackbar snackbar;
    public Ride ride;
    public String rideId;
    private BroadcastReceiver broadcastReceiver;


    //////*****************
    private static int REQUEST_CODE_LOCATION_PERMISSION = 578;
    private Typeface mTypefaceRegular;
    private Typeface mTypefaceBold;
    //private BookingDetailsAcceptRejectActivity mActivityReference;
    private TextView btnAccept, btnReject;
    private TextView labelBookingDate;
    private TextView labelBookingTime;
    private TextView labelHelper;
    private TextView lebalAmount;
    private TextView valuePrice;
    private TextView valueHelperPrice;
    private TextView labelHelperPrice;
    private TextView valueTotalPrice;
    private TextView txtContact;
    private TextView txtTypesGoods;
    private TextView valueBookingDate;
    private TextView valueBookingTime, date_time, time, distance, value_pickup_location,
            value_pickup_contact_name, value_pickup_contact_number, value_drop_location,
            value_drop_contact_name, value_drop_contact_number;
    private TextView tvPickupAdditionalComment;
    private TextView tvDropAdditionalComment;
    private ImageView img_helpers;
    private RelativeLayout header_container, rlGoodsDetail;
    private LinearLayout contactDetailsLayout;
    private RecyclerView rvGoodsImage, goodsDetailLayout;

    private String TAG = "UPDATE";
    private GoogleMap googleMap;
    private GpsTracker gpsTracker;
    private String latitude;
    private String longitude;
    private Polyline line;
    private PolylineOptions options;
    private ImageView ivNumberOfHelpers;
    private TextView tvNumberOfHelpersTop;
    private List<Polyline> polylines;
    private LatLng pickupLatLng;
    private LatLng dropLatLng;
    private TextView toolbarText;


    ///////////*************


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.slide_in, R.anim._slide_out);
        setContentView(R.layout.activity_booking_details_accept_reject);
        polylines = new ArrayList<>();
        Utils.printLogs(TAG, "Opening Goods Activity ... ");
        initializeViews();

        /**
         * Initialize GoogleMap
         */
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.map_accept_reject);
        SupportMapFragment supportMapFragment = (SupportMapFragment) fragment;
        supportMapFragment.getMapAsync(this);
        /**
         * Implement Broadcast Receiver
         */
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Utils.GOODS_ACTIVITY_NOTIFICATION_BROADCAST_ACTION)) {
                    rideId = intent.getStringExtra(ConstantValues.RIDE_ID);
                    fetchRideDetails();
                } else {
                    Utils.printLogs(TAG, "Action don't matches Broadcast Receiver .. BookingDetailsAcceptRejectActivity");
                }
            }
        };


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Utils.GOODS_ACTIVITY_NOTIFICATION_BROADCAST_ACTION);

        registerReceiver(broadcastReceiver, intentFilter);

        this.getIntentValues();
        this.fetchRideDetails();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    /**
     * Fetches values from Intent
     */
    private void getIntentValues() {

        Intent intent = getIntent();
        if (intent != null) {
            rideId = intent.getStringExtra(ConstantValues.RIDE_ID);
            Utils.printLogs(TAG, "Ride Id on BookingDetailsAcceptRejectActivity : " + rideId);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initializeViews() {

        ImageView imgBack = findViewById(R.id.backImage_header);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(this);


        Toolbar toolbar = findViewById(R.id.toolbar_layout_booking_details);
        toolbarText = toolbar.findViewById(R.id.txt_activty_header);
        mTypefaceRegular = Typeface.createFromAsset(getAssets(), ConstantValues.TYPEFACE_REGULAR);
        mTypefaceBold = Typeface.createFromAsset(getAssets(), ConstantValues.TYPEFACE_BOLD);
        txtHeader = findViewById(R.id.txt_activty_header);
        txtHeader.setTypeface(mTypefaceRegular);
        //rlContainer = findViewById(R.id.goods_activity_container);

        //// ****************

        mTypefaceRegular = Typeface.createFromAsset(getAssets(),
                ConstantValues.TYPEFACE_REGULAR);
        mTypefaceBold = Typeface.createFromAsset(getAssets(),
                ConstantValues.TYPEFACE_BOLD);
        labelBookingDate = findViewById(R.id.txt_header_otp);
        labelBookingDate = findViewById(R.id.label_booking_date);
        labelBookingTime = findViewById(R.id.label_booking_time);
        labelHelper = findViewById(R.id.label_helper);
        valueBookingDate = findViewById(R.id.value_booking_date);
        valueBookingTime = findViewById(R.id.value_booking_time);
        labelHelperPrice = findViewById(R.id.label_helper_price);


        lebalAmount = findViewById(R.id.label_amount);
        valuePrice = findViewById(R.id.value_price);
        valueHelperPrice = findViewById(R.id.value_helper_price);
        valueTotalPrice = findViewById(R.id.value_total_price);
        txtContact = findViewById(R.id.label_contact_details);
        txtTypesGoods = findViewById(R.id.label_types_goods);
        date_time = findViewById(R.id.date_time);
        time = findViewById(R.id.time);
        distance = findViewById(R.id.distance);
        contactDetailsLayout = findViewById(R.id.contactDetailsLayout);
        goodsDetailLayout = findViewById(R.id.goodsDetailLayout);
        header_container = findViewById(R.id.header_container);
        rlGoodsDetail = findViewById(R.id.rlGoodsDetail);

        btnAccept = findViewById(R.id.btn_accept);
        btnReject = findViewById(R.id.btn_reject);
        btnReject.setOnClickListener(this);
        btnAccept.setOnClickListener(this);
        header_container.setOnClickListener(this);
        rlGoodsDetail.setOnClickListener(this);

        btnReject.setTypeface(mTypefaceRegular);
        btnReject.setTypeface(mTypefaceRegular);

        lebalAmount.setTypeface(mTypefaceRegular);
        valuePrice.setTypeface(mTypefaceRegular);
        valueHelperPrice.setTypeface(mTypefaceRegular);
        valueTotalPrice.setTypeface(mTypefaceBold);
        labelBookingDate.setTypeface(mTypefaceRegular);
        labelBookingTime.setTypeface(mTypefaceRegular);
        labelHelper.setTypeface(mTypefaceRegular);
        valueBookingDate.setTypeface(mTypefaceRegular);
        valueBookingTime.setTypeface(mTypefaceRegular);
        labelHelperPrice.setTypeface(mTypefaceRegular);
        txtContact.setTypeface(mTypefaceRegular);
        txtTypesGoods.setTypeface(mTypefaceRegular);

        value_pickup_location = findViewById(R.id.value_pickup_location);
        value_pickup_contact_name = findViewById(R.id.value_pickup_contact_name);
        value_pickup_contact_number = findViewById(R.id.value_pickup_contact_number);


        value_drop_location = findViewById(R.id.value_drop_location);
        value_drop_contact_name = findViewById(R.id.value_drop_contact_name);
        value_drop_contact_number = findViewById(R.id.value_drop_contact_number);

        img_helpers = findViewById(R.id.img_helpers);

        rvGoodsImage = findViewById(R.id.rvGoodsImage);
        rvGoodsImage.setHasFixedSize(true);

        goodsDetailLayout.setHasFixedSize(true);

        tvPickupAdditionalComment = findViewById(R.id.tv_pickup_additional_comment);
        tvDropAdditionalComment = findViewById(R.id.tv_drop_additional_comment);
        ivNumberOfHelpers = findViewById(R.id.iv_number_of_helper_top);
        tvNumberOfHelpersTop = findViewById(R.id.tv_number_of_helpers_top);

        /////***********
    }

    /**
     * Fetches current ride details
     */
    private void fetchRideDetails() {
        Utils.printLogs(TAG, "Fetching Ride Details on Goods Details Activity");
        ApiInterface service;
        final ProgressDialog progressDialog = Utils.showProgressDialog(this,
                AppConstants.DIALOG_PLEASE_WAIT);
        try {
            if (Utils.isNetworkAvailable(this)) {
                service = ApiClientConnection.getInstance().createService();
                SharedPreferences sharedPreferences = getSharedPreferences(
                        ConstantValues.USER_PREFERENCES,
                        Context.MODE_PRIVATE);

                String accessToken = sharedPreferences.getString(
                        ConstantValues.USER_ACCESS_TOKEN, "");

                ////
                Utils.printLogs(TAG, "Ride id ... " + rideId);
                Call<RideObjectResponse> call = service.getParticularRideDetails(accessToken,
                        rideId);

                call.enqueue(new Callback<RideObjectResponse>() {
                    @Override
                    public void onResponse(Call<RideObjectResponse> call,
                                           Response<RideObjectResponse> response) {
                        if (progressDialog != null && progressDialog.isShowing())
                            progressDialog.dismiss();
                        try {
                            if (response.isSuccessful()) {
                                RideObjectResponse apiResponse = response.body();
                                Utils.printLogs(TAG, "onResponse : Success : -- " +
                                        apiResponse.ride);
                                if (apiResponse.ride != null) {
                                    ride = apiResponse.ride;
                                    //addBookingDetailsFragment();
                                    setDataOnViews(apiResponse.ride);
                                    pickupLatLng = new LatLng(Double.parseDouble(apiResponse.ride.pickupLatitude), Double.parseDouble(apiResponse.ride.pickupLongitude));
                                    dropLatLng = new LatLng(Double.parseDouble(apiResponse.ride.dropLatitude), Double.parseDouble(apiResponse.ride.dropLongitude));

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
                                } else {
                                    Utils.showToast(apiResponse.message, BookingDetailsAcceptRejectActivity.this);
                                }
                            } else {
                                Utils.printLogs(TAG, "onResponse : Failure : -- " +
                                        response.body());
                            }
                        } catch (Exception e) {
                            Utils.printLogs(TAG, "Exception 222 " + e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<RideObjectResponse> call, Throwable t) {
                        if (progressDialog != null && progressDialog.isShowing())
                            progressDialog.dismiss();
                        Utils.printLogs(TAG, "onFailure : -- " + t.getCause());
                    }
                });
            } else {
            }
        } catch (Exception e) {
            Utils.printLogs(TAG, "Exception while fetching Ride details : " + e.getMessage());
            e.printStackTrace();
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();

        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.backImage_header:
                onBackPressed();
                break;

            //***********

            /*case R.id.btn_confirm_booking:
               if(validateFields()){
                    showPopUp();
                }
                break;*/

            case R.id.btn_accept:
                this.acceptOrRejectDelivery(1);
                break;
            case R.id.btn_reject:
                this.acceptOrRejectDelivery(0);
                break;
            case R.id.header_container:
                if (contactDetailsLayout != null && contactDetailsLayout.isShown()) {
                    contactDetailsLayout.startAnimation(AnimationUtils.loadAnimation(
                            this, R.anim.slide_up));
                    contactDetailsLayout.setVisibility(View.GONE);
                } else {
                    contactDetailsLayout.setVisibility(View.VISIBLE);
                    contactDetailsLayout.startAnimation(AnimationUtils.loadAnimation(
                            this, R.anim.slide_down));
                }
                break;
            case R.id.rlGoodsDetail:

                if (goodsDetailLayout != null && goodsDetailLayout.isShown()) {
                    goodsDetailLayout.startAnimation(AnimationUtils.loadAnimation(
                            this, R.anim.slide_up));
                    goodsDetailLayout.setVisibility(View.GONE);
                } else {
                    goodsDetailLayout.setVisibility(View.VISIBLE);
                    goodsDetailLayout.startAnimation(AnimationUtils.loadAnimation(
                            this, R.anim.slide_down));
                }
                break;
            //****
        }
    }


    private void acceptOrRejectDelivery(final int isAccepted) {

        ApiInterface service;
        try {
            if (Utils.isNetworkAvailable(this)) {

                final ProgressDialog progressDialog = Utils.showProgressDialog(this,
                        AppConstants.DIALOG_PLEASE_WAIT);

                service = ApiClientConnection.getInstance().createService();

                SharedPreferences sharedPreferences = getSharedPreferences(
                        ConstantValues.USER_PREFERENCES,
                        Context.MODE_PRIVATE);

                String accessToken = sharedPreferences.getString(
                        ConstantValues.USER_ACCESS_TOKEN, "");

                Call<ServerResult> call = service.acceptOrRejectDelivery(accessToken,
                        ride.rideId, isAccepted);

                call.enqueue(new Callback<ServerResult>() {
                    @Override
                    public void onResponse(Call<ServerResult> call,
                                           Response<ServerResult> response) {
                        if (progressDialog != null && progressDialog.isShowing())
                            progressDialog.dismiss();
                        try {
                            if (response.isSuccessful()) {
                                ServerResult apiResponse = response.body();
                                Utils.printLogs(TAG, "onResponse : Success : -- " +
                                        apiResponse.response);
                                if (apiResponse.response != null) {
                                    Log.e(TAG, "Valid");
                                    if (isAccepted == 1) {
                                        Intent trackDriverIntent = new Intent(BookingDetailsAcceptRejectActivity.this, TrackDriverActivityDriver.class);
                                        trackDriverIntent.putExtra(ConstantValues.RIDE_ID, ride.rideId);
                                        //trackDriverIntent.putExtra(ConstantValues.RIDE, ride);
                                        startActivity(trackDriverIntent);
                                        //mActivityReference.callTrackDriverActivity();
                                    } else {
                                        Intent mainActivityIntent = new Intent(BookingDetailsAcceptRejectActivity.this, MainActivity.class);
                                        startActivity(mainActivityIntent);
                                    }

                                } else {
                                    Log.e(TAG, "Invalid");
                                    Utils.showToast(apiResponse.message, BookingDetailsAcceptRejectActivity.this);
                                }
                            } else {
                                /**
                                 * Ride is already Accepted
                                 */
                                String errorString = response.errorBody().string();
                                Utils.printLogs(TAG, "onResponse : Failure : -- " + errorString);
                                JSONObject jsonObject = new JSONObject(errorString);
                                String message = jsonObject.getString("message");
                                Utils.showToast("" + message, BookingDetailsAcceptRejectActivity.this);
                                //btnAccept.setText("Accepted");
                                //btnAccept.setEnabled(false);
                                //btnReject.setEnabled(false);
                                if (MainActivity.isOngoing)
                                    Utils.showToast(getString(R.string.text_you_have_a_ride_which_is_still_on_the_way), BookingDetailsAcceptRejectActivity.this);
                                //Utils.showToast(response.body().message, getContext());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ServerResult> call, Throwable t) {
                        if (progressDialog != null && progressDialog.isShowing())
                            progressDialog.dismiss();
                        Utils.printLogs(TAG, "onFailure : -- " + t.getCause());
                    }
                });
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setDataOnViews(final Ride ride) {


        //final Ride rideNew = mActivityReference.rideNew;
        if (ride != null) {

            toolbarText.setText(getString(R.string.text_booking_number) + ride.rideId);

            valueBookingDate.setText(DateHelper.convertTimestampToDate("dd-MM-yyyy",
                    ride.timestamp));
            //valueBookingTime.setText(DateHelper.convertTimestampToTime(ride.timestamp));
            String formattedDate = Utils.getFormattedDate(this, ride.timestamp); // today 10:15 PM
            String requiredTime = Utils.getLastCharacters(formattedDate, 8);
            valueBookingTime.setText(requiredTime);
            valueTotalPrice.setText(" = " + ride.totalPrice + " DHZ");

            date_time.setText(DateHelper.convertTimestampToDate("dd-MM-yyyy",
                    ride.timestamp));
            distance.setText(ride.distance + " km");

            value_pickup_location.setText(ride.pickupLocationAddress);

            value_pickup_contact_name.setText(ride.pickUpContactName);
            value_pickup_contact_number.setText(ride.pickupContactNumber);

            value_drop_location.setText(ride.dropLocationAddress);

            value_drop_contact_name.setText(ride.dropContactName);
            value_drop_contact_number.setText(ride.dropOffContactNumber);

            if (ride.goodsImageList != null && ride.goodsImageList.size() > 0) {
                Utils.printLogs(TAG, "" + ride.goodsImage);
                this.setGoodsImageListAdapter(ride.goodsImageList);
            }

            // TODO: 9/5/18

            if (ride.pickupComment == null) {
                tvPickupAdditionalComment.setVisibility(View.GONE);
            } else {
                tvPickupAdditionalComment.setText("" + ride.pickupComment);
            }

            if (ride.dropComment == null) {
                tvDropAdditionalComment.setVisibility(View.GONE);
            } else {
                tvDropAdditionalComment.setText("" + ride.dropComment);
            }

            switch (ride.noOfHelpers) {
                case 0:
                    img_helpers.setImageDrawable(null);
                    //ivNumberOfHelpers.setImageDrawable(null);
                    ivNumberOfHelpers.setVisibility(View.GONE);
                    tvNumberOfHelpersTop.setVisibility(View.VISIBLE);

                    break;
                case 1:
                    img_helpers.setImageDrawable(getResources().getDrawable(R.drawable.sing_helper));
                    ivNumberOfHelpers.setImageDrawable(getResources().getDrawable(R.drawable.sing_helper));
                    break;
                case 2:
                    img_helpers.setImageDrawable(getResources().getDrawable(R.drawable.double_helper));
                    ivNumberOfHelpers.setImageDrawable(getResources().getDrawable(R.drawable.double_helper));
                    break;
            }

            if (ride.typesOfGoods != null && ride.typesOfGoods.length > 0) {

                TypesOfGoodsAdapter goodsImageListAdapter = new TypesOfGoodsAdapter(this,
                        ride.typesOfGoods);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                        LinearLayoutManager.HORIZONTAL, false) {
                    @Override
                    public boolean canScrollHorizontally() {
                        return ride.typesOfGoods.length != 1;
                    }
                };
                goodsDetailLayout.setLayoutManager(linearLayoutManager);
                goodsDetailLayout.setAdapter(goodsImageListAdapter);
            }
        }
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


    private void setGoodsImageListAdapter(final ArrayList<GoodsImage> goodsImageList) {

        try {
            GoodsImageListAdapter goodsImageListAdapter = new GoodsImageListAdapter(this,
                    goodsImageList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                    LinearLayoutManager.HORIZONTAL, false) {
                @Override
                public boolean canScrollHorizontally() {
                    return goodsImageList.size() != 1;
                }
            };
            rvGoodsImage.setLayoutManager(linearLayoutManager);
            rvGoodsImage.setAdapter(goodsImageListAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    public void moveToMainScreen() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private Fragment getCurrentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
        Fragment currentFragment = fragmentManager.findFragmentByTag(fragmentTag);
        return currentFragment;
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        Utils.printLogs(TAG, "Routing Failure : " + e.getMessage());
    }

    @Override
    public void onRoutingStart() {
        Utils.printLogs(TAG, "Routing Start ");
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    public static void zoomRoute(GoogleMap googleMap, List<LatLng> lstLatLngRoute) {

        if (googleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;
        googleMap.setPadding(100, 150, 100, 150);   // left, top, right, bottom
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLngPoint : lstLatLngRoute)
            boundsBuilder.include(latLngPoint);

        int routePadding = 10;
        LatLngBounds latLngBounds = boundsBuilder.build();

        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding));
    }
}
