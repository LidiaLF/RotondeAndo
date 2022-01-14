package com.example.rotondeando.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

public class Route implements Serializable {
    String _idRotondas, name, description;
    boolean fromWalk, fromBike, fromCar, isDeleted, isFav;
    int _id, levelWalk, levelBike, levelCar;
    double km;

    public Route(){}

    public Route(String _idRotondas, String name, String description,
                 boolean fromWalk, boolean fromBike, boolean fromCar, boolean isDeleted,
                 boolean isFav, int levelWalk, int levelBike, int levelCar, double km) {
        this._idRotondas = _idRotondas;
        this.name = name;
        this.description = description;
        this.fromWalk = fromWalk;
        this.fromBike = fromBike;
        this.fromCar = fromCar;
        this.isDeleted = isDeleted;
        this.isFav = isFav;
        this.levelWalk = levelWalk;
        this.levelBike = levelBike;
        this.levelCar = levelCar;
        this.km = km;
    }

    public Route(int _id, String name, String description, double km, boolean fromWalk,
                 boolean fromBike, boolean fromCar, int levelWalk, int levelBike, int levelCar,
                 boolean isDeleted, boolean isFav, String _idRotondas) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.fromWalk = fromWalk;
        this.fromBike = fromBike;
        this.fromCar = fromCar;
        this.levelWalk = levelWalk;
        this.levelBike = levelBike;
        this.levelCar = levelCar;
        this.km = km;
        this.isDeleted = isDeleted;
        this.isFav = isFav;
        this._idRotondas = _idRotondas;
    }



    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFromWalk() {
        return fromWalk;
    }

    public void setFromWalk(boolean fromWalk) {
        this.fromWalk = fromWalk;
    }

    public boolean isFromBike() {
        return fromBike;
    }

    public void setFromBike(boolean fromBike) {
        this.fromBike = fromBike;
    }

    public boolean isFromCar() {
        return fromCar;
    }

    public void setFromCar(boolean fromCar) {
        this.fromCar = fromCar;
    }

    public int getLevelWalk() {
        return levelWalk;
    }

    public void setLevelWalk(int levelWalk) {
        this.levelWalk = levelWalk;
    }

    public int getLevelBike() {
        return levelBike;
    }

    public void setLevelBike(int levelBike) {
        this.levelBike = levelBike;
    }

    public int getLevelCar() {
        return levelCar;
    }

    public void setLevelCar(int levelCar) {
        this.levelCar = levelCar;
    }

    public double getKm() {
        return km;
    }

    public void setKm(double km) {
        this.km = km;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public String get_idRotondas() {
        return _idRotondas;
    }

    public void set_idRotondas(String _idRotondas) {
        this._idRotondas = _idRotondas;
    }

    @Override
    public String toString() {
        return "Route{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", rotondas=" + _idRotondas +
                ", fromWalk=" + fromWalk +
                ", fromBike=" + fromBike +
                ", fromCar=" + fromCar +
                ", isDeleted=" + isDeleted +
                ", isFav=" + isFav +
                ", _id=" + _id +
                ", levelWalk=" + levelWalk +
                ", levelBike=" + levelBike +
                ", levelCar=" + levelCar +
                ", km=" + km +
                '}';
    }
}
