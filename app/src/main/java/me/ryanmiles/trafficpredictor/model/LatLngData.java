package me.ryanmiles.trafficpredictor.model;

/**
 * Created by Ryan Miles on 11/22/2016.
 */

public class LatLngData {
    private int stationId;
    private String lat;
    private String lng;

    public LatLngData(int stationId, String lat, String lng) {
        this.stationId = stationId;
        this.lat = lat;
        this.lng = lng;
    }

    public LatLngData() {

    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
