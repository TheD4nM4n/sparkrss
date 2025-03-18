package com.thed4nm4n.sparkrss;

import org.json.JSONObject;

public class RSSPost {

    private String title;
    private String author;
    private String description;
    private String url;
    private String publicationDate;

    public RSSPost(String title, String author, String description, String url, String publicationDate) {
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.publicationDate = publicationDate;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getPublicationDate() {
        return publicationDate;
    }
}
