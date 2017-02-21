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
    private List<Point> points;

    private Polyline polyline;
    private int color;

    public Journey() {
    }

    public Journey(String name) {
        this.name = name;
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

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public Polyline getPolyline() {
        return polyline;
    }

    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void addPoint(Point point) {
        if (points == null) points = new ArrayList<>();
        points.add(point);
    }

    public void removePoint(Point point) {
        if (points != null) points.remove(point);
    }

    public PolylineOptions toPolyLine(int color) {
        if (points == null) points = new ArrayList<>();
        LatLng[] array = new LatLng[points.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = points.get(i).getMarker().getPosition();
        }
        return new PolylineOptions().add(array).color(color);
    }

    public Polyline updatePolyline(GoogleMap map) {
        if (polyline != null) polyline.remove();
        polyline = map.addPolyline(toPolyLine(color));
        return polyline;
    }

    public void removePolyline() {
        if (polyline != null) polyline.remove();
    }
}
