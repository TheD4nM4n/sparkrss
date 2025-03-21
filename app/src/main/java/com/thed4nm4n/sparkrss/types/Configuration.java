package com.thed4nm4n.sparkrss.types;

import java.util.ArrayList;
import java.util.List;

public class Configuration {

    private static Configuration singleInstance = null;
    private final List<String> feeds = new ArrayList<>();

    private Configuration() {
        feeds.add("https://www.reddit.com/r/space/.rss?format=xml");
    }

    public static synchronized Configuration getInstance() {
        if (singleInstance == null) {
            singleInstance = new Configuration();
        }

        return singleInstance;
    }

    public List<String> getFeeds() {
        return feeds;
    }
}
