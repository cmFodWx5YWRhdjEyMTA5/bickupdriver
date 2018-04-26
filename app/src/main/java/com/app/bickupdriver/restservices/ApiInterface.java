package com.app.bickupdriver.restservices;

import com.app.bickupdriver.model.BaseArrayResponse;
import com.app.bickupdriver.model.RideArrayResponse;
import com.app.bickupdriver.model.RideObjectResponse;
import com.app.bickupdriver.model.RideStatusResponse;
import com.app.bickupdriver.model.ServerResult;
import com.app.bickupdriver.utility.ConstantValues;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Divya Thakur on 4/12/17.
 */
public interface ApiInterface {

    @GET(Urls.GET_COMPLETED_RIDES)
    Call<BaseArrayResponse> getListOfCompletedRides(@Header(ConstantValues.USER_ACCESS_TOKEN)
                                                            String accessToken);

    @GET(Urls.GET_RIDE_DETAILS)
    Call<RideArrayResponse> getRideDetails(@Header(ConstantValues.USER_ACCESS_TOKEN)
                                                   String accessToken);

    @GET(Urls.GET_PARTICULAR_RIDE_DETAIL)
    Call<RideObjectResponse> getParticularRideDetails(@Header(ConstantValues.USER_ACCESS_TOKEN)
                                                              String accessToken,
                                                      @Path(ConstantValues.RIDE_ID) String rideId);

    @GET(Urls.GET_MISSED_RIDES)
    Call<BaseArrayResponse> getListOfMissedRides(@Header(ConstantValues.USER_ACCESS_TOKEN)
                                                         String accessToken);

    @PUT(Urls.ACCEPT_REJECT_DELIVERY)
    Call<ServerResult> acceptOrRejectDelivery(@Header(ConstantValues.USER_ACCESS_TOKEN)
                                                      String accessToken,
                                              @Path(ConstantValues.RIDE_ID) String rideId,
                                              @Path(ConstantValues.IS_ACCEPTED) int isAccepted);

    @POST(Urls.CHANGE_RIDE_STATUS)
    @FormUrlEncoded
    Call<RideStatusResponse> changeRideStatus(@Header(ConstantValues.USER_ACCESS_TOKEN)
                                                      String accessToken,
                                              @Field(ConstantValues.RIDE_ID) String rideId);

    @GET(Urls.GET_RIDE_STATUS)
    Call<RideStatusResponse> getStatusOfRide(@Header(ConstantValues.USER_ACCESS_TOKEN) String accessToken,
                                             @Path(ConstantValues.RIDE_ID) String rideId);

    @GET(Urls.GET_REVENUES)
    Call<ServerResult> getRevenues(@Header(ConstantValues.USER_ACCESS_TOKEN) String accessToken);

    @GET("/driver/rideDetails")
    Call<ServerResponseRideDetails> getRideDetail(
            @Header("access_token") String accessToken
    );

}
