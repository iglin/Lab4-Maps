package com.iglin.lab4_maps.model;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 21.02.2017.
 */

public class Journey {
    private int id;
    private String name;
    private Point startPoint;;
    private Point endPoint;

    public Journey() {
    }

    public Journey(String name) {
        this.name = name;
    }

    public Journey(Point startPoint, Point endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
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

    public Point getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
    }

    public PolylineOptions toPolyLine(int color) {
        if (startPoint == null || endPoint == null) return null;
        return new PolylineOptions()
                .add(new LatLng(startPoint.getLat(), startPoint.getLng()),
                        new LatLng(endPoint.getLat(), endPoint.getLng()))
                .color(color);
    }

  /*  public Polyline updatePolyline(GoogleMap map) {
        if (polyline != null) polyline.remove();
        polyline = map.addPolyline(toPolyLine(color));
        return polyline;
    }

    public void removePolyline() {
        if (polyline != null) polyline.remove();
    }*/
}
