package com.app.bickupdriver.utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

import com.app.bickupdriver.MainActivity;
import com.app.bickupdriver.TrackDriverActivityDriver;

import io.socket.client.Socket;

/**
 * <H1>Bickup DriverBickup</H1>
 * <H1>GpsTracker</H1>
 *
 * <p>Service which provides the user's current location's
 *  latitude and longitude</p>
 *
 * @author Divya Thakur
 * @version 1.0
 * @since 5/8/17
 */
public class GpsTracker extends Service implements LocationListener {

	private final Context mContext;
	private final String TAG = getClass().getSimpleName();
	private Socket socket = null;
	private Activity activity = null;
	boolean isGPSEnabled = false;                                    // flag for GPS status
	boolean isNetworkEnabled = false;                                    // flag for network status
	boolean canGetLocation = false;                                    // flag for GPS status
	Location location;                                                                // location
	double latitude;                                                                // latitude
	double longitude;                                                                // longitude
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;                // The minimum distance to change Updates in meters i.e. 10 meters
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;                    // The minimum time between updates in milliseconds i.e. 1 minute
	protected LocationManager locationManager;                                        // Declaring a Location Manager

	public GpsTracker(Context context) {
		this.mContext = context;
		getLocation();
	}

	public GpsTracker(Context context, Activity activity) {
		this.mContext = context;
		this.activity = activity;
		getLocation();
	}

	/**
	 * Gets the current location of  the user
	 * @return Location
	 */
	@SuppressLint("MissingPermission")
	public Location getLocation() {

		Utils.printLogs(TAG, "Inside getLocation()");

		try {
			locationManager = (LocationManager) mContext
					.getSystemService(LOCATION_SERVICE);

			// getting GPS status
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				// no network provider is enabled
			} else {
				this.canGetLocation = true;
				// First get location from Network Provider
				if (isNetworkEnabled) {

					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
							if((activity) != null && activity instanceof MainActivity)
								((MainActivity)activity).onLocationChange(String.valueOf(latitude),
										String.valueOf(longitude));
							else if(activity != null && activity instanceof TrackDriverActivityDriver)
								((TrackDriverActivityDriver)activity).onLocationChange(
										String.valueOf(latitude), String.valueOf(longitude));
						}
					}
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
								if (activity != null)
								((MainActivity)activity).onLocationChange(String.valueOf(latitude),
										String.valueOf(longitude));
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Utils.printLogs(TAG, "Outside getLocation()");

		return location;
	}

	/**
	 * Stop using GPS listener
	 * Calling this function will stop using GPS in your app
	 * */
	public void stopUsingGPS() {
		if (locationManager != null) {
			locationManager.removeUpdates(GpsTracker.this);
		}       
	}

	/**
	 * Function to get latitude
	 * */
	public double getLatitude(){
		if(location != null){
			latitude = location.getLatitude();
		}
		return latitude;
	}

	/**
	 * Function to get longitude
	 * */
	public double getLongitude(){
		if(location != null){
			longitude = location.getLongitude();
		}

		// return longitude
		return longitude;
	}

	/**
	 * Function to check GPS/wifi enabled
	 * @return boolean
	 * */
	public boolean canGetLocation() {
		return this.canGetLocation;
	}

	/**
	 * Function to show settings alert dialog
	 * On pressing Settings button will launch Settings Options
	 * */
	/*public void showSettingsAlert(){
		
		Utils.printLogs(TAG, "Inside showSettingsAlert()");
		
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

		// Setting Dialog Title
		alertDialog.setTitle("GPS Settings");

        if(this.canToggleGPS(mContext)) {
            // Setting Dialog Message
            alertDialog.setMessage("GPS is not enabled. Do you want to turn it on?");

            // On pressing Yes button
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    GpsTracker.this.turnGPSOn();
                }
            });
            // on pressing No button
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        } else {

            // Setting Dialog Message
            alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

            // On pressing Settings button
            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mContext.startActivity(intent);
                }
            });

            // on pressing cancel button
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        }

		// Showing Alert Message
		alertDialog.show();
		
		Utils.printLogs(TAG, "Outside showSettingsAlert()");
	}*/

	private void turnGPSOn(){
		String provider = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

		if(!provider.contains("gps")){ //if gps is disabled
			final Intent poke = new Intent();
			poke.setClassName("com.android.settings",
                    "com.android.settings.widget.SettingsAppWidgetProvider");
			poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
			poke.setData(Uri.parse("3"));
			sendBroadcast(poke);
		}
	}

	private void turnGPSOff(){
		String provider = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

		if(provider.contains("gps")){ //if gps is enabled
			final Intent poke = new Intent();
			poke.setClassName("com.android.settings",
                    "com.android.settings.widget.SettingsAppWidgetProvider");
			poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
			poke.setData(Uri.parse("3"));
			sendBroadcast(poke);
		}
	}

    private boolean canToggleGPS(Context context) {
        PackageManager pacman = context.getPackageManager();
        PackageInfo pacInfo = null;

        try {
            pacInfo = pacman.getPackageInfo("com.android.settings", PackageManager.GET_RECEIVERS);
        } catch (PackageManager.NameNotFoundException e) {
            return false; //package not found
        }

        if(pacInfo != null){
            for(ActivityInfo actInfo : pacInfo.receivers){
                //test if recevier is exported. if so, we can toggle GPS.
                if(actInfo.name.equals("com.android.settings.widget.SettingsAppWidgetProvider")
                        && actInfo.exported){
                    return true;
                }
            }
        }

        return false; //default
    }

	@Override
	public void onLocationChanged(Location location) {
		Utils.printLogs(TAG, "Lat : " + latitude + "\nLong : " + longitude);
		String latitude = String.valueOf(location.getLatitude());
		String longitude = String.valueOf(location.getLongitude());
		if((activity) != null && activity instanceof MainActivity)
			((MainActivity)activity).onLocationChange(latitude, longitude);
		else if(activity != null && activity instanceof TrackDriverActivityDriver)
			((TrackDriverActivityDriver)activity).onLocationChange(latitude, longitude);
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}