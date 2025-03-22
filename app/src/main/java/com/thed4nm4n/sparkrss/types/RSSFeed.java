package com.thed4nm4n.sparkrss.types;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;

import java.util.ArrayList;
import java.util.List;

public class RSSFeed {

    private final String name;
    private final List<RSSPost> entries;

    private RSSFeed(String name, List<RSSPost> entries) {
        this.name = name;
        this.entries = entries;
    }

    public static RSSFeed fromSyndFeed(SyndFeed syndFeed) {

        SyndEntry[] syndEntries = syndFeed.getEntries().toArray(new SyndEntry[0]);

        List<RSSPost> posts = new ArrayList<>();
        for (SyndEntry syndEntry : syndEntries) {
            posts.add(RSSPost.fromSyndEntry(syndEntry));
        }
        return new RSSFeed(syndFeed.getTitle(), posts);
    }

    public String getName() {
        return name;
    }

    public List<RSSPost> getEntries() {
        return entries;
    }

}
