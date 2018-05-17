package com.app.bickupdriver.restservices;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.app.bickupdriver.activity.BookingDetailsAcceptRejectActivity;
import com.app.bickupdriver.activity.MainActivity;
import com.app.bickupdriver.activity.TrackDriverActivityDriver;

/**
 * Created by Divya Thakur on 6/12/17.
 */

public class MapRouteAsyncTask extends AsyncTask<Void, Void, String> {

    private ProgressDialog progressDialog;
    private String url;
    private Context context;

    public MapRouteAsyncTask(Context context, String urlPass) {
        this.url = urlPass;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Fetching route, please wait...");
        progressDialog.setIndeterminate(true);
        //progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(Void... params) {
        JsonParser jParser = new JsonParser();
        String json = jParser.getJSONFromUrl(url);
        return json;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        progressDialog.hide();
        if (result != null) {
            if(context instanceof MainActivity){}
                //((MainActivity)context).drawPath(result);
            else if(context instanceof TrackDriverActivityDriver){}
                //((TrackDriverActivityDriver)context).drawPath(result);
            else if(context instanceof BookingDetailsAcceptRejectActivity){}
                //((BookingDetailsAcceptRejectActivity)context).drawPath(result);
        }
    }
}
