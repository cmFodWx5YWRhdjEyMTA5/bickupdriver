package com.app.bickupdriver.broadcastreciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by fluper-pc on 15/9/17.
 */

public  class InternetConnectionBroadcast extends BroadcastReceiver {
    public static ConnectivityRecieverListener mConnectivityRecieverListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager mConnectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworKInfo=mConnectivityManager.getActiveNetworkInfo();
        boolean isConnected=mNetworKInfo!=null&&mNetworKInfo.isConnectedOrConnecting();
        if(mConnectivityRecieverListener!=null){
            mConnectivityRecieverListener.onNetworkConnectionChanged(isConnected);
        }

    }

   public interface ConnectivityRecieverListener{
       void onNetworkConnectionChanged(boolean isConnected);
   }



}
