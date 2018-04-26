package com.app.bickupdriver.notifications;

import com.app.bickupdriver.utility.SharedPreferencesManager;
import com.app.bickupdriver.utility.Utils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * <H1>Basket</H1>
 * <H1>CustomFirebaseInstanceIdService</H1>
 *
 * <p>Generates the refreshed token for the FCM messages</p>
 *
 * @author Divya Thakur
 * @since 5/18/17
 * @version 1.0
 */
public class CustomFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private final String TAG = getClass().getSimpleName();

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Utils.printLogs(TAG, "Refreshed token: " + refreshedToken);
        SharedPreferencesManager.saveGCMRegistrationID(getApplicationContext(), refreshedToken);
    }
}
