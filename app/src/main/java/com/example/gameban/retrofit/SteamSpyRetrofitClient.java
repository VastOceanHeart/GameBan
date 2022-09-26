package com.example.gameban.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SteamSpyRetrofitClient {
    private static final String BASE_URL = "https://steamspy.com/";
    private static Retrofit retrofit;

    public static SteamSpyRetrofitInterface getRetrofitService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(SteamSpyRetrofitInterface.class);
    }
}
