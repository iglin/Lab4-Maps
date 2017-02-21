package com.iglin.lab4_maps.model;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 21.02.2017.
 */

public class Point {
    private int id;
    private List<Picture> pics;
    private Bitmap icon;
    private String title;
    private String description;
    private double lng;
    private double lat;

    public Point() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap iconId) {
        this.icon = iconId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
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

}
