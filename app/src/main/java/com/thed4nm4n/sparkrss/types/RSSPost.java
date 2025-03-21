package com.thed4nm4n.sparkrss.types;

public class RSSPost {

    private final String title;
    private final String author;
    private final String description;
    private final String url;
    private final String publicationDate;

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
