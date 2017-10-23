package com.example.eric.popularmovies.Models;

/**
 *
 * Created by eric on 17/10/2017.
 */

public class VideoModel {
    private String name;
    private String key;
    private int size;

    public VideoModel() {
    }

    public VideoModel(String name, String key, int size) {
        this.name = name;
        this.key = key;
        this.size = size;

    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public int getSize() {
        return size;
    }
}
