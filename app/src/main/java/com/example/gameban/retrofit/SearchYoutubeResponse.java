package com.example.gameban.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchYoutubeResponse {
    @SerializedName("items")
    private List<YoutubeItems> youtubeItemsList;

    public List<YoutubeItems> getYoutubeItemsList() {
        return youtubeItemsList;
    }

    public void setYoutubeItemsList(List<YoutubeItems> youtubeItemsList) {
        this.youtubeItemsList = youtubeItemsList;
    }
}
