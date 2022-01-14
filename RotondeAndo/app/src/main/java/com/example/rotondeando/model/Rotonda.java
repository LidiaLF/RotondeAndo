package com.example.rotondeando.model;

import java.io.Serializable;

public class Rotonda implements Serializable {
    int _id;
    double lat, lng;
    String name;

    public Rotonda(){}
    public Rotonda(int _id, double lat, double lng, String name) {
        this._id = _id;
        this.lat = lat;
        this.lng = lng;
        this.name = name;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Rotonda{" +
                "_id=" + _id +
                ", lat=" + lat +
                ", lng=" + lng +
                ", name='" + name + '\'' +
                '}';
    }
}
