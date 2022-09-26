package com.example.gameban.retrofit;

import com.google.android.gms.maps.model.LatLng;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleMapRetrofitInterface {
    @GET("place/nearbysearch/json")
    Call<GoogleMapPlaceResponse> locationSearch(
            @Query("key") String googleApiKey,
            @Query("location") String Location,
            @Query("radius") int distance,
            @Query("keyword") String keyWord);
}
