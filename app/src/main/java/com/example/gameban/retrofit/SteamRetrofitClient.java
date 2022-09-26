package com.example.gameban.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SteamRetrofitClient {
    private static final String BASE_URL = "https://api.steampowered.com/";
    private static Retrofit retrofit;

    public static SteamRetrofitInterface getRetrofitService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(SteamRetrofitInterface.class);
    }
}
