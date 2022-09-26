package com.example.gameban.retrofit;

import com.google.gson.annotations.SerializedName;

public class YoutubeSnippet {

    @SerializedName("title")
    private String title;

    @SerializedName("publishedAt")
    private String publishDate;

    @SerializedName("description")
    private String desc;

    @SerializedName("thumbnails")
    private YoutubeThumbnail youtubeThumbnail;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public YoutubeThumbnail getYoutubeThumbnail() {
        return youtubeThumbnail;
    }

    public void setYoutubeThumbnail(YoutubeThumbnail youtubeThumbnail) {
        this.youtubeThumbnail = youtubeThumbnail;
    }
}
