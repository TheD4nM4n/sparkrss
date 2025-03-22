package com.thed4nm4n.sparkrss.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rometools.rome.feed.synd.SyndEntry;
import com.thed4nm4n.sparkrss.R;
import com.thed4nm4n.sparkrss.RSSPostActivity;
import com.thed4nm4n.sparkrss.types.RSSPost;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class RSSPostFragment extends Fragment implements View.OnClickListener {

    private final RSSPost post;

    public RSSPostFragment(RSSPost post) {
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

        final TextView dateTextView = view.findViewById(R.id.post_date);
        dateTextView.setText(post.getPublicationDate());

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getView().getContext(), RSSPostActivity.class);
        intent.putExtra("post", post);

        this.startActivity(intent);
    }
}