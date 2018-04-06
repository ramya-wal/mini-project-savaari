package com.example.ramya.savaari.network;

import com.example.ramya.savaari.models.SignInResponse;
import com.example.ramya.savaari.models.UpdateDetails;
import com.example.ramya.savaari.models.User;
import com.example.ramya.savaari.models.Vehicle;
import com.example.ramya.savaari.models.VehicleAddedResponse;
import com.example.ramya.savaari.models.VehicleImageList;
import com.example.ramya.savaari.models.VehicleImages;
import com.example.ramya.savaari.models.VehicleList;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface APIInterface {

    @POST("users")
    Call<SignInResponse> createUser(@HeaderMap Map<String, String> headers, @Body User user);

    @POST("classes/Vehicle")
    Call<VehicleAddedResponse> addVehicle(@HeaderMap Map<String, String> headers, @Body Vehicle vehicle);

    @POST("classes/VehicleImages")
    Call<VehicleAddedResponse> addVehicleImage(@HeaderMap Map<String, String> headers, @Body VehicleImages vehicleImages);

    @GET("login")
    Call<User> getUser(@HeaderMap Map<String, String> headers, @QueryMap(encoded = true) Map<String, String> fields);

    @GET("classes/Vehicle")
    Call<VehicleList> getAllVehicles(@HeaderMap Map<String, String> headers);

    @GET("classes/VehicleImages")
    Call<VehicleImageList> getAllVehicleImages(@HeaderMap Map<String, String> headers);

    @DELETE("classes/Vehicle/{vehicleId}")
    Call<ResponseBody> deleteVehicle(@HeaderMap Map<String, String> headers, @Path("vehicleId") String vehicleId);

    @DELETE("classes/VehicleImages/{objectId}")
    Call<ResponseBody> deleteVehicleImages(@HeaderMap Map<String, String> headers, @Path("objectId") String vehicleObjectId);

    @GET("classes/VehicleImages")
    Call<VehicleImageList> getVehicleImageForVehicle(@HeaderMap Map<String, String> headers, @QueryMap Map<String, String> query);

    @GET("users/{userId}")
    Call<User> getUserDetails(@HeaderMap Map<String, String> headers, @Path("userId") String userId);

    @PUT("classes/Vehicle/{vehicleId}")
    Call<UpdateDetails> updateVehicle(@HeaderMap Map<String, String> headers, @Body Vehicle vehicle, @Path("vehicleId") String vehicleId);

    @PUT("classes/Vehicle/{userId}")
    Call<UpdateDetails> updateUser(@HeaderMap Map<String, String> headers, @Body User user, @Path("userId") String userId);
}
