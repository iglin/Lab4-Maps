package com.iglin.lab4_maps.model;

import android.graphics.Bitmap;

/**
 * Created by user on 21.02.2017.
 */

public class Picture {
    private int id;
    private Bitmap picture;

    public Picture() {
    }

    public Picture(int id, Bitmap picture) {
        this.id = id;
        this.picture = picture;
    }

    public Picture(Bitmap picture) {

        this.picture = picture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }
}
