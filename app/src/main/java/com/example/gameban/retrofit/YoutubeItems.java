package com.example.gameban.retrofit;

import com.google.gson.annotations.SerializedName;

public class YoutubeItems {

    @SerializedName("id")
    private YoutubeId youtubeId;

    @SerializedName("snippet")
    private YoutubeSnippet youtubeSnippet;

    public YoutubeId getYoutubeId() {
        return youtubeId;
    }

    public void setYoutubeId(YoutubeId youtubeId) {
        this.youtubeId = youtubeId;
    }

    public YoutubeSnippet getYoutubeSnippet() {
        return youtubeSnippet;
    }

    public void setYoutubeSnippet(YoutubeSnippet youtubeSnippet) {
        this.youtubeSnippet = youtubeSnippet;
    }
}
