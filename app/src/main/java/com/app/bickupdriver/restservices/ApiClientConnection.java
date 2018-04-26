package com.app.bickupdriver.restservices;

import com.app.bickupdriver.model.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
//import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * <H1>Bickup DriverBickup</H1>
 * <H1>ApiInterface</H1>
 *
 * <p>Represents the main interface that declares all the methods for API interaction</p>
 *
 * @author Divya Thakur
 * @version 1.0
 * @since 5/11/17
 */
public class ApiClientConnection {

    private static ApiClientConnection connect;

    public static synchronized ApiClientConnection getInstance() {

        if(connect == null) {
            connect = new ApiClientConnection();
        }
        return connect;
    }


    private ApiInterface clientService;

    public ApiInterface createService() throws Exception {

        if(clientService == null) {

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.readTimeout(30, TimeUnit.SECONDS);
            httpClient.connectTimeout(30, TimeUnit.SECONDS);

            // add logging as last interceptor
            httpClient.addInterceptor(logging);

           /* Gson gson = new GsonBuilder().registerTypeAdapter(Response.class,
                    new ResponseDeserializer()).create();*/


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Urls.FINAL_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();

            clientService = retrofit.create(ApiInterface.class);
        }
        return clientService;
    }
}
