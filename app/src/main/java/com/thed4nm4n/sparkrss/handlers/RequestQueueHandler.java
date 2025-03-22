package com.thed4nm4n.sparkrss.handlers;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestQueueHandler {

    private static RequestQueueHandler instance;
    private final RequestQueue queue;

    private RequestQueueHandler(Context context) {
        queue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static RequestQueueHandler getInstance(Context context) {
        if (instance == null) {
            instance = new RequestQueueHandler(context);
        }

        return instance;
    }

    public RequestQueue getRequestQueue() {
        return queue;
    }
}
