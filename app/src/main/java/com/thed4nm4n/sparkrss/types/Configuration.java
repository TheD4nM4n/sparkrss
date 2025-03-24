package com.thed4nm4n.sparkrss.types;

import android.content.Context;
import android.util.Log;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Configuration {

    static Configuration singleInstance = null;
    private final List<RSSFeed> feeds = new CopyOnWriteArrayList<>();
    private final ReadWriteLock feedsLock = new ReentrantReadWriteLock();
    private final JSONObject defaultConfig = new JSONObject();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private Configuration(Context context) {

        try {
            defaultConfig.put("feeds", new JSONArray().put("https://feeds.washingtonpost.com/rss/world"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        readConfigFromDisk(context);
    }

    public static synchronized Configuration getInstance(Context context) {
        if (singleInstance == null) {
            singleInstance = new Configuration(context);
        }

        return singleInstance;
    }

    public void addFeed(Context context, String feedUrl) {
        Lock writeLock = feedsLock.writeLock();
        executor.execute(() -> {
            writeLock.lock();

            // [1] Build feed from String, add to feeds
            try {
                URL urlObj = new URL(feedUrl);
                InputStream urlStream = urlObj.openStream();

                SyndFeed syndFeed = new SyndFeedInput().build(new XmlReader(urlStream));
                RSSFeed feed = RSSFeed.fromSyndFeed(syndFeed, feedUrl);

                feeds.add(feed);
                writeConfigToDisk(context);
            }

            // [1.1] Malformed feed URL
            catch (IOException e) {
                Log.d("ERROR", String.format("Malformed feed URL: %s", feedUrl));
                throw new RuntimeException(e);
            }

            // [1.2] Feed parsing error
            catch (FeedException e) {
                Log.d("ERROR", String.format("Failed parsing feed URL: %s", feedUrl));
            }

            writeLock.unlock();
        });
    }

    public List<RSSFeed> getFeeds() {
        Lock readLock = feedsLock.readLock();
        readLock.lock();
        try {
            return feeds;
        } finally {
            readLock.unlock();
        }
    }

    public ReadWriteLock getFeedsReadWriteLock() {
        return feedsLock;
    }

    public void readConfigFromDisk(Context context) {
        Lock feedWriteLock = feedsLock.writeLock();
        feedWriteLock.lock();

        executor.execute(() -> {
            String objectString = "";

            // [1] Read config file to String
            try {
                FileInputStream fis = context.openFileInput("spark-config.json");
                InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
                StringBuilder stringBuilder = new StringBuilder();
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();

                while (line != null) {
                    stringBuilder.append(line).append("\n");
                    line = reader.readLine();
                }

                objectString = stringBuilder.toString();
                fis.close();
            }

            // [1.1] Missing config file
            catch (IOException e) {
                Log.d("WARNING", "Config file not found. Generating default.");

                try {

                    objectString = defaultConfig.toString(4);

                    try (FileOutputStream fos = context.openFileOutput("spark-config.json", Context.MODE_PRIVATE)) {
                        fos.write(objectString.getBytes());
                    }

                } catch (JSONException | IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

            JSONObject object = null;


            try {
                // [2] Build a JSON object from file contents
                object = new JSONObject(objectString);

                JSONArray feedUrls = object.getJSONArray("feeds");

                // [3] Build feed list from JSONArray
                for (int i = 0; i < feedUrls.length(); i++) {
                    addFeed(context, feedUrls.getString(i));
                }
            }

            // [2.1] Malformed config file
            catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        feedWriteLock.unlock();
    }

    public void resetConfig(Context context) {
        feeds.clear();
        addFeed(context, "https://feeds.washingtonpost.com/rss/world");
        writeConfigToDisk(context);
    }

    public void writeConfigToDisk(Context context) {
        executor.execute(() -> {
            try {
                JSONObject configObject = new JSONObject();

                Lock lock = feedsLock.readLock();
                lock.lock();

                List<String> feedUrls = new ArrayList<>();
                feedUrls.add("https://pitchfork.com/feed/feed-news/rss");

                for (RSSFeed feed : feeds) {
                    feedUrls.add(feed.getUrl());
                }

                lock.unlock();

                configObject.put("feeds", new JSONArray(feedUrls));

                try (FileOutputStream fos = context.openFileOutput("spark-config.json", Context.MODE_PRIVATE)) {
                    fos.write(configObject.toString().getBytes());
                }
            } catch (JSONException | IOException e) {
                File file = new File(context.getFilesDir(), "spark-config.json");

                throw new RuntimeException(e);
            }
        });
    }

    public void dumpToLog(Context context) {
        executor.execute(() -> {
            String objectString = "";

            try {
                FileInputStream fis = context.openFileInput("spark-config.json");
                InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
                StringBuilder stringBuilder = new StringBuilder();
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();

                while (line != null) {
                    stringBuilder.append(line).append("\n");
                    line = reader.readLine();
                }

                Log.d("DEBUG", stringBuilder.toString());

                fis.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
