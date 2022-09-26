package com.example.gameban.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GoogleMapPlaceResponse {
    @SerializedName("results")
    private List<GoogleMapLocation> result;

    public List<GoogleMapLocation> getResult() {
        return result;
    }

    public void setResult(List<GoogleMapLocation> result) {
        this.result = result;
    }
}
