package com.example.gameban.retrofit;

import com.google.gson.annotations.SerializedName;

public class YoutubeId {
    @SerializedName("videoId")
    private String videoId;

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
