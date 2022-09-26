package com.example.gameban.retrofit;

import com.google.gson.annotations.SerializedName;

public class SearchSteamNewsResponse {
    @SerializedName("appnews")
    private SteamAppNews steamAppNews;

    public SteamAppNews getSteamAppNews() {
        return steamAppNews;
    }

    public void setSteamAppNews(SteamAppNews steamAppNews) {
        this.steamAppNews = steamAppNews;
    }
}
