package com.thed4nm4n.sparkrss.fragments.settings;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.thed4nm4n.sparkrss.R;
import com.thed4nm4n.sparkrss.types.Configuration;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class DebugFragment extends Fragment {

    private Configuration config;

    public DebugFragment(Context context) {
        this.config = Configuration.getInstance(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_debug, container, false);

        config = Configuration.getInstance(view.getContext());

        MaterialButton dumpConfigButton = view.findViewById(R.id.dump_config_button);
        dumpConfigButton.setOnClickListener(v -> {
            config.dumpToLog(view.getContext());

            for (int i = 0; i < config.getFeeds().size(); i++) {
                Log.d("FEED", config.getFeeds().get(i).getName());
            }
        });

        MaterialButton resetConfigButton = view.findViewById(R.id.reset_config_button);
        resetConfigButton.setOnClickListener(v -> config.resetConfig(view.getContext()));

        MaterialButton defaultFeedButton = view.findViewById(R.id.default_feed_button);
        defaultFeedButton.setOnClickListener(v -> config.addFeed(view.getContext(), "https://pitchfork.com/feed/feed-news/rss"));

        MaterialButton writeConfigButton = view.findViewById(R.id.write_config_button);
        writeConfigButton.setOnClickListener(v -> config.writeConfigToDisk(view.getContext()));

        return view;
    }

}