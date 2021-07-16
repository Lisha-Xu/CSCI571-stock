package com.example.stocktradingapp.detail;

import com.example.stocktradingapp.utils.DateToLongAdapter;
import com.google.gson.annotations.JsonAdapter;

import java.util.Date;

public class News {
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private String publishedAt;
    private String sourceNew;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getSourceNew() {
        return sourceNew;
    }

    public void setSourceNew(String sourceNew) {
        this.sourceNew = sourceNew;
    }
}
