package com.iglin.lab4_maps.model;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 21.02.2017.
 */

public class Point {
    private int id;
    private Marker marker;
    private List<Picture> pics;

    public Point() {
    }

    public Point(Marker marker) {
        this.marker = marker;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public List<Picture> getPics() {
        return pics;
    }

    public void setPics(List<Picture> pics) {
        this.pics = pics;
    }

    public void addPic(Picture picture) {
        if (pics == null) pics = new ArrayList<>();
        pics.add(picture);
    }

    public void removePic(Picture picture) {
        if (pics != null) pics.remove(picture);
    }

    public double getLatitude() {
        return marker.getPosition().latitude;
    }

    public double getLongitude() {
        return marker.getPosition().longitude;
    }
}
