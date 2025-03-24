package com.thed4nm4n.sparkrss;

import android.os.Bundle;
import android.os.StrictMode;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.thed4nm4n.sparkrss.fragments.home.HomeMenuFragment;
import com.thed4nm4n.sparkrss.fragments.settings.SettingsMenuFragment;
import com.thed4nm4n.sparkrss.handlers.ImageLoaderHandler;

public class MainActivity extends AppCompatActivity {
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

        this.getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finishAffinity();
            }
        });

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build();
        StrictMode.setThreadPolicy(policy);

        // Setting up Volley queue and image loader
        ImageLoaderHandler.getInstance(this);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navListener);

        fm = getSupportFragmentManager();

        fm.beginTransaction().add(R.id.fragment_container, HomeMenuFragment.getInstance(this)).commit();
    }

    private final NavigationBarView.OnItemSelectedListener navListener = item -> {

        for (Fragment fragment : fm.getFragments()) {
            fm.beginTransaction().remove(fragment).commit();
        }

        int itemId = item.getItemId();
        if (itemId == R.id.home_menu) {
            fm.beginTransaction().add(R.id.fragment_container, HomeMenuFragment.getInstance(this)).commit();
        } else if (itemId == R.id.settings_menu) {
            fm.beginTransaction().add(R.id.fragment_container, new SettingsMenuFragment()).commit();
        }
        return true;
    };
}