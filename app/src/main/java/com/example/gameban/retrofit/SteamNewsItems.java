package com.example.gameban.retrofit;

import com.google.gson.annotations.SerializedName;

public class SteamNewsItems {

    @SerializedName("title")
    private String title;

    @SerializedName("url")
    private String url;

    @SerializedName("contents")
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
