package com.thed4nm4n.sparkrss;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.apptasticsoftware.rssreader.Item;
import com.apptasticsoftware.rssreader.RssReader;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Setting up Volley queue and image loader
        ImageLoaderSingleton.getInstance(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener( item -> {
            String fileContents = "Hello world";

            try(FileOutputStream fos = this.openFileOutput("settings.json", Context.MODE_PRIVATE)) {
                fos.write(fileContents.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                FileInputStream fis = this.openFileInput("settings.json");
                InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
                StringBuilder stringBuilder = new StringBuilder();
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    stringBuilder.append(line).append("\n");
                    line = reader.readLine();
                }
                Log.d("TESTING", stringBuilder.toString());
                fis.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return true;
        });

        List<String> feeds = Configuration.getInstance().getFeeds();
        FragmentManager fm = getSupportFragmentManager();

        fm.beginTransaction()
                .add(R.id.fragment_container, new WelcomeFragment())
                .commit();

        for (int i = 0; i < feeds.size(); i++) {
            try {
                SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(feeds.get(i))));
                List<SyndEntry> entries = feed.getEntries();

                for (int x = 0; x < entries.size(); x++) {
                    Fragment frag = new RSSPostFragment(entries.get(x));
                    fm.beginTransaction()
                            .add(R.id.fragment_container, frag)
                            .commit();

                }
            } catch (FeedException | IOException e) {
                Log.d("ERROR", "Failed to parse RSS feed.");

            }
        }
    }
}