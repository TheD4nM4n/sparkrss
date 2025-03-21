package com.thed4nm4n.sparkrss;

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
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.thed4nm4n.sparkrss.fragments.RSSPostFragment;
import com.thed4nm4n.sparkrss.fragments.SettingsFragment;
import com.thed4nm4n.sparkrss.fragments.HeaderFragment;
import com.thed4nm4n.sparkrss.singletons.ImageLoaderSingleton;
import com.thed4nm4n.sparkrss.types.Configuration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Fragment> activeFragments = new ArrayList<>();
    private List<Fragment> homeFragments = new ArrayList<>();
    private List<String> feeds;
    private FragmentManager fm;

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
        bottomNavigationView.setOnItemSelectedListener(navListener);
//        bottomNavigationView.setOnNavigationItemSelectedListener( item -> {
//            String fileContents = "Hello world";
//
//            try(FileOutputStream fos = this.openFileOutput("settings.json", Context.MODE_PRIVATE)) {
//                fos.write(fileContents.getBytes());
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//
//            try {
//                FileInputStream fis = this.openFileInput("settings.json");
//                InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
//                StringBuilder stringBuilder = new StringBuilder();
//                BufferedReader reader = new BufferedReader(inputStreamReader);
//                String line = reader.readLine();
//                while (line != null) {
//                    stringBuilder.append(line).append("\n");
//                    line = reader.readLine();
//                }
//                Log.d("TESTING", stringBuilder.toString());
//                fis.close();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//
//            return true;
//        });

        feeds = Configuration.getInstance().getFeeds();
        fm = getSupportFragmentManager();

        showHomeFragments();
    }

    private final NavigationBarView.OnItemSelectedListener navListener = item -> {

        int itemId = item.getItemId();
        if (itemId == R.id.home_menu) {
            clearFragments();
            showHomeFragments();
        }
        else if (itemId == R.id.settings_menu) {
            clearFragments();
            showSettingsFragments();
        }
        return true;
    };

    private void clearFragments() {
        if (!activeFragments.isEmpty()) {
            FragmentTransaction transaction = fm.beginTransaction();
            for (Fragment frag : activeFragments) {
                transaction.remove(frag);
            }
            activeFragments.clear();
            transaction.commit();
        }
    }

       private void showHomeFragments() {

           FragmentTransaction transaction = fm.beginTransaction();

           if (homeFragments.isEmpty()) {

               homeFragments.add(new HeaderFragment(getString(R.string.home_header)));

               for (int i = 0; i < feeds.size(); i++) {
                   try {
                       SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(feeds.get(i))));
                       List<SyndEntry> entries = feed.getEntries();

                       for (int x = 0; x < entries.size(); x++) {
                           Fragment frag = new RSSPostFragment(entries.get(x));
                           homeFragments.add(frag);
                       }

                   } catch (FeedException | IOException e) {
                       Log.d("ERROR", String.format("Failed to parse RSS feed: %s", feeds.get(i)));

                   }
               }
           }

           for (Fragment frag : homeFragments) {
               activeFragments.add(frag);
               transaction.add(R.id.fragment_container, frag);
           }

           transaction.commit();
       }

    private void showSettingsFragments() {

        FragmentTransaction transaction = fm.beginTransaction();

        activeFragments.add(new HeaderFragment("Settings"));
        activeFragments.add(new SettingsFragment());

        for (Fragment frag : activeFragments) {
            transaction.add(R.id.fragment_container, frag);
        }

        transaction.commit();
    }
}