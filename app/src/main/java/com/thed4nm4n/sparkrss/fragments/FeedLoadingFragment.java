package com.thed4nm4n.sparkrss.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thed4nm4n.sparkrss.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedLoadingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedLoadingFragment extends Fragment {


    public FeedLoadingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed_loading, container, false);
    }
}