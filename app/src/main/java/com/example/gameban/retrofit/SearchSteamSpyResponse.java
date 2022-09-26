package com.example.gameban.retrofit;

import com.google.gson.annotations.SerializedName;

public class SearchSteamSpyResponse {
    @SerializedName("appid")
    private String appid;

    @SerializedName("name")
    private String name;

    @SerializedName("developer")
    private String developer;

    @SerializedName("positive")
    private int positive;

    @SerializedName("negative")
    private int negative;

    @SerializedName("price")
    private String price;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public int getPositive() {
        return positive;
    }

    public void setPositive(int positive) {
        this.positive = positive;
    }

    public int getNegative() {
        return negative;
    }

    public void setNegative(int negative) {
        this.negative = negative;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
