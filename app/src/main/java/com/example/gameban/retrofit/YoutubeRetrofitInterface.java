package com.example.gameban.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YoutubeRetrofitInterface {
    @GET("youtube/v3/search")
    Call<SearchYoutubeResponse> youtubeSearch(
            @Query("part") String youtubeSearchPart,
            @Query("maxResults") int youtubeSearchMaxResult,
            @Query("key") String youtubeApiKey,
            @Query("q") String youtubeKeyword);
}
