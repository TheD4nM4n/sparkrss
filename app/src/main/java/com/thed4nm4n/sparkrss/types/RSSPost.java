package com.thed4nm4n.sparkrss.types;

import android.media.Image;

import androidx.annotation.Nullable;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndLink;

import org.jdom2.Element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RSSPost implements Serializable {

    private final String title;
    private final String author;
    private final String description;
    private final String url;
    private final String publicationDate;
    private final String imageUrl;

    private RSSPost(String title, String author, String description, String url, String publicationDate, String imageUrl) {
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.publicationDate = publicationDate;
        this.imageUrl = imageUrl;
    }

    public static RSSPost fromSyndEntry(SyndEntry syndEntry) {

        List<String> urls = new ArrayList<>();

        for (SyndLink syndLink : syndEntry.getLinks()) {
            urls.add(syndLink.toString());
        }

        String imgUrl = null;

        List<Element> foreignMarkups = (List<Element>) syndEntry.getForeignMarkup();
        for (Element foreignMarkup : foreignMarkups) {
            if (foreignMarkup.getAttribute("url") != null) {
                imgUrl = foreignMarkup.getAttribute("url").getValue();
            }
        }

        return new RSSPost(
                syndEntry.getTitle(),
                syndEntry.getAuthor(),
                null == syndEntry.getDescription() ? "None provided" : syndEntry.getDescription().getValue(),
                syndEntry.getLink(),
                syndEntry.getPublishedDate().toString(),
                imgUrl
        );
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

    public @Nullable String getImageUrl() {
        return imageUrl.equals("null") ? null : imageUrl;
    }

    public String getPublicationDate() {
        return publicationDate;
    }
}
