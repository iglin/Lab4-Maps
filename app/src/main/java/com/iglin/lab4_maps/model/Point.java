package com.iglin.lab4_maps.model;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 21.02.2017.
 */

public class Point {
    private int id;
    private String name;
    private String description;
    private LatLng latLng;
    private List<Picture> pics;
    private BitmapDescriptor icon;

    public Point() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public List<Picture> getPics() {
        return pics;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public BitmapDescriptor getIcon() {
        return icon;
    }

    public void setIcon(BitmapDescriptor icon) {
        this.icon = icon;
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

    public MarkerOptions toMarker() {
        MarkerOptions marker = new MarkerOptions().position(latLng).title(name).snippet(description);
        if (icon != null) marker.icon(icon);
        return marker;
    }
}
