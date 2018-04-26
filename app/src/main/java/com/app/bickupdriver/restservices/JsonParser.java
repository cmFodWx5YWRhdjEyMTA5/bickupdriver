package com.app.bickupdriver.restservices;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Divya Thakur on 6/12/17.
 */

public class JsonParser {

    InputStream is = null;
    JSONObject jObj = null;
    String json = "";

    // constructor
    public JsonParser() { }

    public String getJSONFromUrl(String url1) {

        // Making HTTP request
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(url1);

            urlConnection = (HttpURLConnection) url
                    .openConnection();

            InputStream in = urlConnection.getInputStream();

            InputStreamReader isw = new InputStreamReader(in);

            int data = isw.read();
            while (data != -1) {
                char current = (char) data;
                data = isw.read();
                //System.out.print(current);
                json = json + current;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return json;
    }
}
