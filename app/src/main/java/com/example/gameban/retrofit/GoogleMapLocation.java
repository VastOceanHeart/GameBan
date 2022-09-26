package com.example.gameban.retrofit;

import com.google.gson.annotations.SerializedName;

public class GoogleMapLocation {
    @SerializedName("business_status")
    private String businessStatus;
    @SerializedName("geometry")
    private GeometryLocation geometryLocation;
    @SerializedName("name")
    private String name;
    @SerializedName("place_id")
    private String placeID;
    @SerializedName("price_level")
    private int priceLevel;

    public String getBusinessStatus() {
        return businessStatus;
    }

    public void setBusinessStatus(String businessStatus) {
        this.businessStatus = businessStatus;
    }

    public GeometryLocation getGeometryLocation() {
        return geometryLocation;
    }

    public void setGeometryLocation(GeometryLocation location) {
        this.geometryLocation = location;
    }

    public Double getLat() {
        return this.geometryLocation.getLocation().getLat();
    }

    public Double getLng() {
        return this.geometryLocation.getLocation().getLng();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public int getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(int priceLevel) {
        this.priceLevel = priceLevel;
    }
}

class GeometryLocation {
    @SerializedName("location")
    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}

class Location {
    @SerializedName("lat")
    private Double lat;
    @SerializedName("lng")
    private Double lng;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}