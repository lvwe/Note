package com.example.administrator.note;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017/5/2 0002.
 */

public class Note {


    private Bitmap imgId;
    private String title;
    private String category;
    private Bitmap imgStar;
    private Bitmap imgClock;
    private String reTime;

    public Bitmap getImgId() {
        return imgId;
    }

    public void setImgId(Bitmap imgId) {
        this.imgId = imgId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Bitmap getImgStar() {
        return imgStar;
    }

    public void setImgStar(Bitmap imgStar) {
        this.imgStar = imgStar;
    }

    public Bitmap getImgClock() {
        return imgClock;
    }

    public void setImgClock(Bitmap imgClock) {
        this.imgClock = imgClock;
    }

    public String getReTime() {
        return reTime;
    }

    public void setReTime(String reTime) {
        this.reTime = reTime;
    }

    public Note(Bitmap imgId, String title, String category, Bitmap imgStar, Bitmap imgClock, String reTime) {
        this.imgId = imgId;
        this.title = title;
        this.category = category;
        this.imgStar = imgStar;
        this.imgClock = imgClock;
        this.reTime = reTime;
    }
}

