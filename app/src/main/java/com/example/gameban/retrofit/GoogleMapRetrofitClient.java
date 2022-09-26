package com.example.gameban.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoogleMapRetrofitClient {
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/";
    private static Retrofit retrofit;

    public static GoogleMapRetrofitInterface getRetrofitService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(GoogleMapRetrofitInterface.class);
    }

    public static GoogleMapDirectionRetrofitInterface getDirectionRetrofitService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(GoogleMapDirectionRetrofitInterface.class);
    }
}

