package com.completesportsnigeria.completesports.classes;

import java.io.Serializable;

public class FeedItem implements Serializable{
    String title;
    String link;
    String description;
    String pubDate;
    String thumbnailUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public FeedItem(String title, String link, String description, String pubDate, String thumbnailUrl){
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
        this.thumbnailUrl = thumbnailUrl;
    }

    public FeedItem(){

    }
}