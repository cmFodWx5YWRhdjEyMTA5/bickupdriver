package com.app.bickupdriver.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.bickupdriver.GoodsActivity;
import com.app.bickupdriver.MainActivity;
import com.app.bickupdriver.R;
import com.app.bickupdriver.adapter.GoodsImageListAdapter;
import com.app.bickupdriver.adapter.TypesOfGoodsAdapter;
import com.app.bickupdriver.controller.AppConstants;
import com.app.bickupdriver.interfaces.HandlerGoodsNavigations;
import com.app.bickupdriver.model.GoodsImage;
import com.app.bickupdriver.model.Ride;
import com.app.bickupdriver.model.ServerResult;
import com.app.bickupdriver.restservices.ApiClientConnection;
import com.app.bickupdriver.restservices.ApiInterface;
import com.app.bickupdriver.restservices.MapRouteAsyncTask;
import com.app.bickupdriver.utility.ConstantValues;
import com.app.bickupdriver.utility.DateHelper;
import com.app.bickupdriver.utility.GpsTracker;
import com.app.bickupdriver.utility.Utils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingDetailsFragment extends Fragment implements View.OnClickListener,
        OnMapReadyCallback {

    private static int REQUEST_CODE_LOCATION_PERMISSION = 578;
    public static String Tag = BookingDetailsFragment.class.getSimpleName();
    private Typeface mTypefaceRegular;
    private Typeface mTypefaceBold;
    private GoodsActivity mActivityReference;
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
    private ImageView img_helpers;
    private RelativeLayout header_container, rlGoodsDetail;
    private LinearLayout contactDetailsLayout;
    private RecyclerView rvGoodsImage, goodsDetailLayout;

    private String TAG = getClass().getSimpleName();
    private Context context = mActivityReference;
    private GoogleMap googleMap;
    private GpsTracker gpsTracker;
    private String latitude;
    private String longitude;
    private Polyline line;
    private PolylineOptions options;

    public BookingDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Utils.printLogs(TAG, "onCreateView on Booking Details Fragment ");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking_detail, container, false);
        initializeViews(view);
        this.setDataOnViews(mActivityReference.rideNew);
        setGoogleMap();
        gpsTracker = new GpsTracker(getActivity(), getActivity());
        this.checkForLocationPermission();


        Utils.printLogs(TAG, "Opening Booking Details Fragment ... ");
        Utils.printLogs(TAG, "Current Latitude : " + gpsTracker.getLatitude());
        Utils.printLogs(TAG, "Current Longitude : " + gpsTracker.getLongitude());
        Utils.printLogs(TAG, "Drop Latitude : " + mActivityReference.rideNew.dropLatitude);
        Utils.printLogs(TAG, "Drop Longitude : " + mActivityReference.rideNew.dropLongitude);

        String urlTopass = makeURL(gpsTracker.getLatitude(),
                gpsTracker.getLongitude(),
                Double.valueOf(mActivityReference.rideNew.dropLatitude),
                Double.valueOf(mActivityReference.rideNew.dropLongitude));
        Utils.printLogs(TAG, "Url to Pass on Booking Details : : " + urlTopass);

        new MapRouteAsyncTask(getActivity(), urlTopass).execute();
        return view;
    }

    /**
     * Creates the URL for drawing the polyline on google map
     *
     * @param sourcelat
     * @param sourcelog
     * @param destlat
     * @param destlog
     * @return
     */
    public String makeURL(double sourcelat, double sourcelog, double destlat,
                          double destlog) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("http://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString.append(Double.toString(sourcelog));
        urlString.append("&destination=");// to
        urlString.append(Double.toString(destlat));
        urlString.append(",");
        urlString.append(Double.toString(destlog));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        return urlString.toString();
    }

    private void initializeViews(View view) {

        mTypefaceRegular = Typeface.createFromAsset(mActivityReference.getAssets(),
                ConstantValues.TYPEFACE_REGULAR);
        mTypefaceBold = Typeface.createFromAsset(mActivityReference.getAssets(),
                ConstantValues.TYPEFACE_BOLD);
        labelBookingDate = (TextView) view.findViewById(R.id.txt_header_otp);
        labelBookingDate = (TextView) view.findViewById(R.id.label_booking_date);
        labelBookingTime = (TextView) view.findViewById(R.id.label_booking_time);
        labelHelper = (TextView) view.findViewById(R.id.label_helper);
        valueBookingDate = (TextView) view.findViewById(R.id.value_booking_date);
        valueBookingTime = (TextView) view.findViewById(R.id.value_booking_time);
        labelHelperPrice = (TextView) view.findViewById(R.id.label_helper_price);


        lebalAmount = (TextView) view.findViewById(R.id.label_amount);
        valuePrice = (TextView) view.findViewById(R.id.value_price);
        valueHelperPrice = (TextView) view.findViewById(R.id.value_helper_price);
        valueTotalPrice = (TextView) view.findViewById(R.id.value_total_price);
        txtContact = (TextView) view.findViewById(R.id.label_contact_details);
        txtTypesGoods = (TextView) view.findViewById(R.id.label_types_goods);
        date_time = view.findViewById(R.id.date_time);
        time = view.findViewById(R.id.time);
        distance = view.findViewById(R.id.distance);
        contactDetailsLayout = view.findViewById(R.id.contactDetailsLayout);
        goodsDetailLayout = view.findViewById(R.id.goodsDetailLayout);
        header_container = view.findViewById(R.id.header_container);
        rlGoodsDetail = view.findViewById(R.id.rlGoodsDetail);

        btnAccept = (TextView) view.findViewById(R.id.btn_accept);
        btnReject = (TextView) view.findViewById(R.id.btn_reject);
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

        value_pickup_location = view.findViewById(R.id.value_pickup_location);
        value_pickup_contact_name = view.findViewById(R.id.value_pickup_contact_name);
        value_pickup_contact_number = view.findViewById(R.id.value_pickup_contact_number);


        value_drop_location = view.findViewById(R.id.value_drop_location);
        value_drop_contact_name = view.findViewById(R.id.value_drop_contact_name);
        value_drop_contact_number = view.findViewById(R.id.value_drop_contact_number);

        img_helpers = view.findViewById(R.id.img_helpers);

        rvGoodsImage = view.findViewById(R.id.rvGoodsImage);
        rvGoodsImage.setHasFixedSize(true);

        goodsDetailLayout.setHasFixedSize(true);

    }

    /**
     * Sets data on the views
     *
     * @param ride Holds the Ride object
     */
    private void setDataOnViews(Ride ride) {

        final Ride rideNew = mActivityReference.rideNew;
        if (rideNew != null) {
            valueBookingDate.setText(DateHelper.convertTimestampToDate("dd-MM-yyyy",
                    ride.timestamp));
            valueBookingTime.setText(DateHelper.convertTimestampToTime(ride.timestamp));
            valueTotalPrice.setText(" = " + ride.totalPrice + " DHZ");

            date_time.setText(DateHelper.convertTimestampToDate("dd-MM-yyyy",
                    ride.timestamp));
            distance.setText(ride.distance + " km");

            value_pickup_location.setText(ride.pickupLocationAddress);

            value_pickup_contact_name.setText(rideNew.pickUpContactName);
            value_pickup_contact_number.setText(rideNew.pickupContactNumber);

            value_drop_location.setText(ride.dropLocationAddress);

            value_drop_contact_name.setText(rideNew.dropContactName);
            value_drop_contact_number.setText(rideNew.dropOffContactNumber);

            if (rideNew.goodsImageList != null && rideNew.goodsImageList.size() > 0) {
                this.setGoodsImageListAdapter(rideNew.goodsImageList);
            }

            switch (rideNew.noOfHelpers) {
                case 0:
                    img_helpers.setImageDrawable(null);
                    break;
                case 1:
                    img_helpers.setImageDrawable(getResources().getDrawable(R.drawable.sing_helper));
                    break;
                case 2:
                    img_helpers.setImageDrawable(getResources().getDrawable(R.drawable.double_helper));
                    break;
            }

            if (rideNew.typesOfGoods != null && rideNew.typesOfGoods.length > 0) {

                TypesOfGoodsAdapter goodsImageListAdapter = new TypesOfGoodsAdapter(getActivity(),
                        rideNew.typesOfGoods);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                        LinearLayoutManager.HORIZONTAL, false) {
                    @Override
                    public boolean canScrollHorizontally() {
                        return rideNew.typesOfGoods.length != 1;
                    }
                };
                goodsDetailLayout.setLayoutManager(linearLayoutManager);
                goodsDetailLayout.setAdapter(goodsImageListAdapter);
            }
        }
    }

    private void setGoogleMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Checks for the Permission of Location
     */
    private boolean checkForLocationPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(getContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            this.trackCurrentLocation();
            Utils.printLogs(TAG, "Permission Granted");
            return true;
        } else {

            ActivityCompat.requestPermissions(getActivity(), new String[]{
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION);
            Utils.printLogs(TAG, "Permission NOT Granted, Requesting for the same");
            return false;
        }
    }

    /**
     * Tracks current location of user
     */
    private void trackCurrentLocation() {

        gpsTracker = new GpsTracker(getActivity(), getActivity());
        if (gpsTracker.canGetLocation()) {
            latitude = String.valueOf(gpsTracker.getLatitude());
            longitude = String.valueOf(gpsTracker.getLongitude());
            this.updateLocationUI();
        } else {
            latitude = "";
            longitude = "";
        }
    }

    private void fixZoom() {
        List<LatLng> points = options.getPoints(); // route is instance of PolylineOptions
        LatLngBounds.Builder bc = new LatLngBounds.Builder();
        for (LatLng item : points) {
            bc.include(item);
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 50));
    }

    private void updateLocationUI() {

        if (googleMap != null) {

            if (gpsTracker != null) {

                LatLngBounds currentBounds = new LatLngBounds(
                        new LatLng(gpsTracker.getLatitude(),
                                gpsTracker.getLongitude()),
                        new LatLng(gpsTracker.getLatitude(),
                                gpsTracker.getLongitude()));

                LatLng currentLatLng = new LatLng(gpsTracker.getLatitude(),
                        gpsTracker.getLongitude());

                CameraUpdate myLocation = CameraUpdateFactory.newLatLngZoom(
                        currentBounds.getCenter(),
                        15);
                googleMap.animateCamera(myLocation);
                UiSettings mUiSetting = googleMap.getUiSettings();
                mUiSetting.setTiltGesturesEnabled(true);
                mUiSetting.setRotateGesturesEnabled(true);
            }
        }
    }

    public void drawPath(String result) {

        if (line != null) {
            googleMap.clear();
        }
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.valueOf(mActivityReference.rideNew.dropLatitude),
                        Double.valueOf(mActivityReference.rideNew.dropLongitude)))
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.drop_location_pin)));
        //googleMap.addMarker(new MarkerOptions().position(currentLatLng));
        try {
            // Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes
                    .getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);

            options = new PolylineOptions().width(20)
                    .color(getResources().getColor(R.color.appcolor)).geodesic(true);
            for (int z = 0; z < list.size(); z++) {
                LatLng point = list.get(z);
                options.add(point);
            }
            line = googleMap.addPolyline(options);
            this.fixZoom();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Decodes the polyline
     *
     * @param encoded
     * @return
     */
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    /**
     * Sets the Goods Image Adapter
     *
     * @param goodsImageList holds the goods image list
     */
    private void setGoodsImageListAdapter(final ArrayList<GoodsImage> goodsImageList) {

        try {
            GoodsImageListAdapter goodsImageListAdapter = new GoodsImageListAdapter(getActivity(),
                    goodsImageList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
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

   /* private boolean validateFields() {
        if(!CommonMethods.getInstance().validateEditFeild(edtContactName.getText().toString())){
            Toast.makeText(mActivityReference, mActivityReference.getResources().getString(R.string.txt_contact_person_name), Toast.LENGTH_SHORT).show();
            return false;
        }


        if(!CommonMethods.getInstance().validateEditFeild(edtContactNumber.getText().toString())){
            Toast.makeText(mActivityReference, mActivityReference.getResources().getString(R.string.txt_vaidate_mobile), Toast.LENGTH_SHORT).show();
            return false;
        }else{
            if(!CommonMethods.getInstance().validateMobileNumber(edtContactNumber.getText().toString(),6)){
                Toast.makeText(mActivityReference, mActivityReference.getResources().getString(R.string.txt_vaidate_mobile_number), Toast.LENGTH_SHORT).show();
                return false;
            }

        }
        return true;
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(getContext());
        mActivityReference = (GoodsActivity) getContext();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {

        HandlerGoodsNavigations handlerGoodsNavigations = mActivityReference;
        int id = view.getId();

        switch (id) {
            case R.id.btn_confirm_booking:
              /*  if(validateFields()){
                    showPopUp();
                }*/
                break;
            case R.id.btn_accept:
                this.acceptOrRejectDelivery(1);
                break;
            case R.id.btn_reject:
                this.acceptOrRejectDelivery(0);
                break;
            case R.id.header_container:
                if (contactDetailsLayout != null && contactDetailsLayout.isShown()) {
                    contactDetailsLayout.startAnimation(AnimationUtils.loadAnimation(
                            mActivityReference, R.anim.slide_up));
                    contactDetailsLayout.setVisibility(View.GONE);
                } else {
                    contactDetailsLayout.setVisibility(View.VISIBLE);
                    contactDetailsLayout.startAnimation(AnimationUtils.loadAnimation(
                            mActivityReference, R.anim.slide_down));
                }
                break;
            case R.id.rlGoodsDetail:

                if (goodsDetailLayout != null && goodsDetailLayout.isShown()) {
                    goodsDetailLayout.startAnimation(AnimationUtils.loadAnimation(
                            mActivityReference, R.anim.slide_up));
                    goodsDetailLayout.setVisibility(View.GONE);
                } else {
                    goodsDetailLayout.setVisibility(View.VISIBLE);
                    goodsDetailLayout.startAnimation(AnimationUtils.loadAnimation(
                            mActivityReference, R.anim.slide_down));
                }
                break;

        }
    }

    /**
     * Accepts or Rejects deliveries
     *
     * @param isAccepted Holds the value of acceptance of delivery
     */
    private void acceptOrRejectDelivery(final int isAccepted) {

        ApiInterface service;
        try {
            if (Utils.isNetworkAvailable(mActivityReference)) {

                final ProgressDialog progressDialog = Utils.showProgressDialog(mActivityReference,
                        AppConstants.DIALOG_PLEASE_WAIT);

                service = ApiClientConnection.getInstance().createService();

                SharedPreferences sharedPreferences = mActivityReference.getSharedPreferences(
                        ConstantValues.USER_PREFERENCES,
                        Context.MODE_PRIVATE);

                String accessToken = sharedPreferences.getString(
                        ConstantValues.USER_ACCESS_TOKEN, "");

                Call<ServerResult> call = service.acceptOrRejectDelivery(accessToken,
                        mActivityReference.rideNew.rideId, isAccepted);

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
                                    if (isAccepted == 1)
                                        mActivityReference.callTrackDriverActivity();
                                    else
                                        mActivityReference.moveToMainScreen();
                                } else {
                                    Log.e(TAG, "Invalid");
                                    Utils.showToast(apiResponse.message, getContext());
                                }
                            } else {
                                Utils.printLogs(TAG, "onResponse : Failure : -- " +
                                        response.body());
                                if (MainActivity.isOngoing)
                                    Utils.showToast("You have a ride which is still on " +
                                            "the way, please complete it first.", getActivity());
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            this.googleMap = googleMap;
            MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(
                    getActivity(), R.raw.map_style);
            this.googleMap.setMapStyle(style);
            if (this.checkForLocationPermission()) {
                this.trackCurrentLocation();
            }
        }
    }
}
