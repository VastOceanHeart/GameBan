package com.example.gameban.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SteamAppNews {

    @SerializedName("appid")
    private String appid;

    @SerializedName("newsitems")
    private List<SteamNewsItems> steamNewsItemsList;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public List<SteamNewsItems> getSteamNewsItemsList() {
        return steamNewsItemsList;
    }

    public void setSteamNewsItemsList(List<SteamNewsItems> steamNewsItemsList) {
        this.steamNewsItemsList = steamNewsItemsList;
    }
}
