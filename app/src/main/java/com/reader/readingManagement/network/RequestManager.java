package com.reader.readingManagement.network;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.ResponseDelivery;
import com.android.volley.toolbox.BasicNetwork;
import com.reader.readingManagement.BookApplication;

import java.io.File;

/**
 * Created by naver on 2017. 1. 30..
 */

public class RequestManager extends RequestQueue {
    private static final String DEFAULT_CACHE_DIR = "requestCache";
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10; /* 10MB */

    private static RequestQueue mRequestQueue;

    public RequestManager(Cache cache, Network network, int threadPoolSize, ResponseDelivery delivery) {
        super(cache, network, threadPoolSize, delivery);
    }

    public static void init(BookApplication context) {
        File cacheDir = new File(context.getCacheDir(), DEFAULT_CACHE_DIR);

        Network network = new BasicNetwork(new OkHttpStack());
        PersistentImageDiskCache diskBasedCache = new PersistentImageDiskCache(cacheDir, DISK_CACHE_SIZE);
        mRequestQueue = new RequestQueue(diskBasedCache, network);
        mRequestQueue.start();
    }

    public static RequestQueue getQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("Not initialized");
        }
    }
}
