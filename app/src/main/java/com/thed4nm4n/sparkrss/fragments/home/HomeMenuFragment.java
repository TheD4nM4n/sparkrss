package com.thed4nm4n.sparkrss.fragments.home;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thed4nm4n.sparkrss.R;
import com.thed4nm4n.sparkrss.fragments.HeaderFragment;
import com.thed4nm4n.sparkrss.types.Configuration;
import com.thed4nm4n.sparkrss.types.RSSFeed;
import com.thed4nm4n.sparkrss.types.RSSPost;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;

public class HomeMenuFragment extends Fragment {

    private static HomeMenuFragment instance;
    private FragmentManager fm;
    private final Configuration config;
    private SwipeRefreshLayout swipeRefreshLayout;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private HomeMenuFragment(Context context) {
        this.config = Configuration.getInstance(context);
    }

    public static HomeMenuFragment getInstance(Context context) {
        if (instance == null) {
            instance = new HomeMenuFragment(context);
        }
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = getChildFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_menu, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            clearFragments();
            regenerateFragments();
        });

        regenerateFragments();

        return view;
    }

    private void regenerateFragments() {
        executor.execute(() -> {

            getActivity().runOnUiThread(() -> swipeRefreshLayout.setRefreshing(true));

            Lock feedsLock = config.getFeedsReadWriteLock().readLock();
            feedsLock.lock();

            fm.beginTransaction().add(R.id.fragment_container, new HeaderFragment("Your latest updates")).commit();

            List<RSSFeed> feeds = config.getFeeds();


            for (RSSFeed feed : feeds) {

                FragmentTransaction transaction = fm.beginTransaction();

                List<RSSPost> posts = feed.getEntries();
                for (int i = 0; i < 10 && i < posts.size(); i++) {

                    Fragment frag = posts.get(i).getFragment();
                    transaction.add(R.id.fragment_container, frag);
                }

                transaction.commit();
            }
            feedsLock.unlock();

            getActivity().runOnUiThread(() -> swipeRefreshLayout.setRefreshing(false));
        });
    }

    private void clearFragments() {
        for (Fragment fragment : fm.getFragments()) {
            fm.beginTransaction().remove(fragment).commit();
        }
    }
}