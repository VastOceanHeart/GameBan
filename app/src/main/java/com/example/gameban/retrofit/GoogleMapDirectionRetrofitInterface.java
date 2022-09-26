package com.example.gameban.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleMapDirectionRetrofitInterface {
    @GET("directions/json")
    Call<GoogleMapRouteResponse> routeSearch(
            @Query("key") String googleApiKey,
            @Query("destination") String destination,
            @Query("origin") String origin,
            @Query("mode") String mode);
}
