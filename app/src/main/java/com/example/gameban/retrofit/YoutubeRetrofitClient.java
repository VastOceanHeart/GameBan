package com.example.gameban.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class YoutubeRetrofitClient {

    private static final String BASE_URL = "https://youtube.googleapis.com/";
    private static Retrofit retrofit;

    public static YoutubeRetrofitInterface getRetrofitService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(YoutubeRetrofitInterface.class);
    }

}
