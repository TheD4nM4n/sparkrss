package com.thed4nm4n.sparkrss.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thed4nm4n.sparkrss.R;

public class HeaderFragment extends Fragment {

    private final String text;
    public HeaderFragment(String text) {
        this.text = text;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

        TextView tv = view.findViewById(R.id.header_text);
        tv.setText(text);

        return view;
    }
}