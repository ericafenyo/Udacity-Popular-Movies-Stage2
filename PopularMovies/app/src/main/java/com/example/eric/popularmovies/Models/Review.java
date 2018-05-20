package com.example.eric.popularmovies.Models;

/**
 * Created by eric on 17/10/2017.
 */

public class Review {

    private String author;
    private String content;
    private String url;
    private int total_pages;

    public Review(String author, String content, String url) {
        this.author = author;
        this.content = content;
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public int getTotal_pages() {
        return total_pages;
    }
}
