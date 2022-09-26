package com.example.gameban.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SteamRetrofitInterface {
    @GET("ISteamNews/GetNewsForApp/v0002/")
    Call<SearchSteamNewsResponse> steamNewsSearch(
            @Query("appid") String appId,
            @Query("count") int resultCount,
            @Query("format") String resultFormat);
}
