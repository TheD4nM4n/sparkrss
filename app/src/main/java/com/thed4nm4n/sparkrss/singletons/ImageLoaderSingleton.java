package com.thed4nm4n.sparkrss.singletons;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

public class ImageLoaderSingleton {

    private static ImageLoaderSingleton instance;
    private final ImageLoader loader;

    private ImageLoaderSingleton(Context context) {
        loader = new ImageLoader(
                RequestQueueSingleton.getInstance(context).getRequestQueue(),
                new ImageLoader.ImageCache() {

                    private final LruCache<String, Bitmap> cache = new LruCache<>(20);
                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static synchronized ImageLoaderSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new ImageLoaderSingleton(context);
        }
        return instance;
    }

    public ImageLoader getImageLoader() {
        return loader;
    }
}
