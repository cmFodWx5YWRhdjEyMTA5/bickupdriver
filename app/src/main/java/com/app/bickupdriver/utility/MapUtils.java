package com.app.bickupdriver.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import com.app.bickupdriver.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.paypal.android.sdk.onetouch.core.metadata.ah.R;

/**
 * Created by manish on 3/5/18.
 */

public class MapUtils {

    public static final float CAMERA_ZOOM_LEVEL = 15.0f;
    public static final double CIRCLE_RADIUS = 30;


    public static void addMarker(GoogleMap googleMap, LatLng latLng, String title, int markerIcon) {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(markerIcon))
                .anchor(0.5f, 0.5f);
        //.title(title);
        Marker marker = googleMap.addMarker(markerOptions);
        ArrayList<Marker> markerList = new ArrayList<>();
        markerList.add(marker);
    }

    @SuppressLint("MissingPermission")
    public static void currentLocation(GoogleMap mMap, Context context) {
        try {
            mMap.clear();
            GpsTracker gpsTracker = new GpsTracker(context);
            mMap.getUiSettings().setCompassEnabled(false);
            mMap.getUiSettings().setZoomControlsEnabled(false);
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()), MapUtils.CAMERA_ZOOM_LEVEL));
        } catch (Exception e) {
            Log.d("BICKUP", "Exception while getting current location : " + e.getMessage());
        }
    }

    public static void zoomRoute(GoogleMap googleMap, List<LatLng> lstLatLngRoute) {

        if (googleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;
        googleMap.setPadding(10, 600, 10, 500);   // left, top, right, bottom
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLngPoint : lstLatLngRoute)
            boundsBuilder.include(latLngPoint);

        int routePadding = 10;
        LatLngBounds latLngBounds = boundsBuilder.build();

        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding));
    }

    public static String latLngToAddress(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String address = null;
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            //address = addresses.get(0).getAddressLine(0);
            // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            if (addresses != null) {
                String locality = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                String premises = addresses.get(0).getPremises();
                String subAdminArea = addresses.get(0).getSubAdminArea();
                String subLocality = addresses.get(0).getSubLocality();

                Log.d("BICKUP", "city : " + locality);
                Log.d("BICKUP", "state : " + state);
                Log.d("BICKUP", "Country : " + country);
                Log.d("BICKUP", "Postal Code : " + postalCode);
                Log.d("BICKUP", "Known Name : " + knownName);
                Log.d("BICKUP", "Premises : " + premises);
                Log.d("BICKUP", "Sub Admin Area : " + subAdminArea);
                Log.d("BICKUP", "Sub Locality  : " + subLocality);
                address = knownName + ", " + subLocality + ", " + locality;
                Log.d("BICKUP", "Address : " + address);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("BICKUP", "Exception while converting LatLng to address : " + e.getMessage());
        }
        return address;
    }


    /*@SuppressLint("MissingPermission")
    public static void addMarkerToMap(Context context, GoogleMap googleMap, LatLng latLng, String address) {
        Log.d("BICKUP","Adding marker to map --- : ");
        googleMap.setMyLocationEnabled(false);
        // Decrease Size of Old Marker
        int height = 80;
        int width = 80;
        BitmapDrawable bitmapdraw = (BitmapDrawable) context.getResources().getDrawable(R.drawable.blue_point);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                .anchor(0.5f, 0.5f)
                .title(address);
        googleMap.addMarker(markerOptions);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, MapUtils.CAMERA_ZOOM_LEVEL));
        //MapUtils.drawCircle(context, googleMap, latLng);
    }

    public static void drawCircle(Context context, GoogleMap googleMap, LatLng location) {
        CircleOptions options = new CircleOptions();
        options.center(location);
        options.radius(CIRCLE_RADIUS); //Radius in meters
        options.fillColor(ContextCompat.getColor(context, R.color.color_blue));
        options.strokeColor(ContextCompat.getColor(context, R.color.color_white));
        options.strokeWidth(5);
        googleMap.addCircle(options);
    }*/
}
