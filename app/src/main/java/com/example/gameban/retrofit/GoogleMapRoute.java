package com.example.gameban.retrofit;

import androidx.room.Ignore;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GoogleMapRoute {
    @SerializedName("legs")
    private List<Leg> legs = new ArrayList<Leg>();

    public List<Leg> getLegs() {
        return legs;
    }

    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }
    @Ignore
    public ArrayList<LatLng> getAllSteps(){
        ArrayList<LatLng> result = new ArrayList<>();
        for (Step oneStep: legs.get(0).getSteps()){
            LatLng oneLatLng = new LatLng(oneStep.getStartLocation().getLat(), oneStep.getStartLocation().getLng());
            result.add(oneLatLng);
            oneLatLng = new LatLng(oneStep.getEndLocation().getLat(), oneStep.getEndLocation().getLng());
            result.add(oneLatLng);
        }
        return result;
    }
    @Ignore
    public String getMesage(){
        String result = legs.get(0).getDistance().getText() + " in total\n"+ legs.get(0).getDuration().getText()+"' drive";
        return result;
    }
}
class Leg{
    @SerializedName("distance")
    private OneText distance;
    @SerializedName("duration")
    private OneText duration;
    @SerializedName("steps")
    private List<Step> steps = new ArrayList<Step>();

    public OneText getDistance() {
        return distance;
    }

    public void setDistance(OneText distance) {
        this.distance = distance;
    }

    public OneText getDuration() {
        return duration;
    }

    public void setDuration(OneText duration) {
        this.duration = duration;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }
}

class OneText{
    @SerializedName("text")
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

class Step{
    @SerializedName("end_location")
    private routeLocation endLocation;
    @SerializedName("start_location")
    private routeLocation startLocation;

    public routeLocation getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(routeLocation endLocation) {
        this.endLocation = endLocation;
    }

    public routeLocation getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(routeLocation startLocation) {
        this.startLocation = startLocation;
    }
}

class routeLocation {
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