package com.thed4nm4n.sparkrss;

import android.content.Context;
import android.view.PixelCopy;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestQueueSingleton {

    private static RequestQueueSingleton instance;
    private final RequestQueue queue;

    private RequestQueueSingleton(Context context) {
        queue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static RequestQueueSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new RequestQueueSingleton(context);
        }

        return instance;
    }

    public RequestQueue getRequestQueue() {
        return queue;
    }
}
