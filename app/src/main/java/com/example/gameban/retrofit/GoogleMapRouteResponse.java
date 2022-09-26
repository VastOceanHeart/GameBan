package com.example.gameban.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GoogleMapRouteResponse {
    @SerializedName("routes")
    private List<GoogleMapRoute> routes;

    public List<GoogleMapRoute> getRoutes() {
        return routes;
    }

    public void setRoutes(List<GoogleMapRoute> routes) {
        this.routes = routes;
    }
}
