package com.thed4nm4n.sparkrss;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apptasticsoftware.rssreader.Item;
import com.rometools.rome.feed.synd.SyndEntry;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class RSSPostFragment extends Fragment implements View.OnClickListener {

    private final SyndEntry post;

    public RSSPostFragment(SyndEntry post) {
        this.post = post;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Add post information to view
        View view = inflater.inflate(R.layout.fragment_rsspost, container, false);
        view.setOnClickListener(this);

        final TextView titleTextView = view.findViewById(R.id.post_title);
        titleTextView.setText(post.getTitle());

        final TextView authorTextView = view.findViewById(R.id.post_author);
        authorTextView.setText(post.getAuthor());

        String date = new SimpleDateFormat("MM/dd", Locale.US).format(post.getPublishedDate());
        final TextView dateTextView = view.findViewById(R.id.post_date);
        dateTextView.setText(date);

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getView().getContext(), RSSPostActivity.class);
        intent.putExtra("url", post.getUri());
        intent.putExtra("title", post.getTitle());
        if (post.getDescription() == null) {
            intent.putExtra("desc", "No description provided");
        } else {
            intent.putExtra("desc", post.getDescription().toString());
        }

        this.startActivity(intent);
    }
}