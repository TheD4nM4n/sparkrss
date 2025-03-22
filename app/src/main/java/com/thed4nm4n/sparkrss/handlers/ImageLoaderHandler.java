package com.thed4nm4n.sparkrss.handlers;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

public class ImageLoaderHandler {

    private static ImageLoaderHandler instance;
    private final ImageLoader loader;

    private ImageLoaderHandler(Context context) {
        loader = new ImageLoader(
                RequestQueueHandler.getInstance(context).getRequestQueue(),
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

    public static synchronized ImageLoaderHandler getInstance(Context context) {
        if (instance == null) {
            instance = new ImageLoaderHandler(context);
        }
        return instance;
    }

    public ImageLoader getImageLoader() {
        return loader;
    }
}
