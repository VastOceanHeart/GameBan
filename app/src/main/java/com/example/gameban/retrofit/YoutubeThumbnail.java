package com.example.gameban.retrofit;

import com.google.gson.annotations.SerializedName;

public class YoutubeThumbnail {
    @SerializedName("medium")
    private YoutubeThumbnailDefault youtubeThumbnailDefault;

    public YoutubeThumbnailDefault getYoutubeThumbnailDefault() {
        return youtubeThumbnailDefault;
    }

    public void setYoutubeThumbnailDefault(YoutubeThumbnailDefault youtubeThumbnailDefault) {
        this.youtubeThumbnailDefault = youtubeThumbnailDefault;
    }
}
