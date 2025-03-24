package com.thed4nm4n.sparkrss.types;

import android.util.Log;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class RSSFeed {

    private final String name;
    private final List<RSSPost> entries;
    private final String url;

    private RSSFeed(String name, List<RSSPost> entries, String url) {
        this.name = name;
        this.entries = entries;
        this.url = url;
    }

    public static RSSFeed fromSyndFeed(SyndFeed syndFeed, String url) {

        List<SyndEntry> syndEntries = Arrays.asList(syndFeed.getEntries().toArray(new SyndEntry[0]));

        List<RSSPost> posts = new ArrayList<>();

        for (int i = 0; i < 10 && i < syndEntries.size(); i++) {
            posts.add(RSSPost.fromSyndEntry(syndEntries.get(i)));
        }

        return new RSSFeed(syndFeed.getTitle(), posts, url);
    }

    public String getName() {
        return name;
    }

    public List<RSSPost> getEntries() {
        return entries;
    }

    public String getUrl() {
        return url;
    }
}
