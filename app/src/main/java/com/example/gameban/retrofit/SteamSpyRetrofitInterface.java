package com.example.gameban.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SteamSpyRetrofitInterface {
    @GET("api.php")
    Call<SearchSteamSpyResponse> steamSpySearch(
            @Query("request") String request,
            @Query("appid") String appId);
}
