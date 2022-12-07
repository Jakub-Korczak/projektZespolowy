package com.example.gps;

//Model of what we are supposed to save in database

public class LocationData {
    private double latitude;
    private double longitude;  //location data of the phone in the moment when AP scan happened
    private String ap_BSSID;  //BSSID of the access point

    public LocationData(double latitude, double longitude, String ap_BSSID) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.ap_BSSID = ap_BSSID;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAp_BSSID() {
        return ap_BSSID;
    }

    public void setAp_BSSID(String ap_BSSID) {
        this.ap_BSSID = ap_BSSID;
    }

    @Override
    public String toString() {
        return "LocationData{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", ap_BSSID='" + ap_BSSID + '\'' +
                '}';
    }
}
