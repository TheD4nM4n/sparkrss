package com.thed4nm4n.sparkrss.fragments.settings;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thed4nm4n.sparkrss.R;
import com.thed4nm4n.sparkrss.fragments.HeaderFragment;

public class SettingsMenuFragment extends Fragment {

    public SettingsMenuFragment() {
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
        View view = inflater.inflate(R.layout.fragment_settings_menu, container, false);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        transaction.add(R.id.fragment_container, new HeaderFragment("Settings"));
        transaction.add(R.id.fragment_container, new GeneralSettingsFragment());
        transaction.add(R.id.fragment_container, new DebugFragment(this.getContext()));

        transaction.commit();

        return view;
    }
}