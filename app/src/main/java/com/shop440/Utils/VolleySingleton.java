package com.shop440.Utils;

/**
 * Created by smile on 1/12/16.
 */

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by smilecs on 6/3/15.
 */
public class VolleySingleton {
    private static VolleySingleton sInstance = null;
    private RequestQueue mRequestQueue;
    private ImageLoader imageLoader;

    private VolleySingleton(){
        mRequestQueue = Volley.newRequestQueue(Application.getAppContext());
        imageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(200);


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
    public static VolleySingleton getsInstance(){
        if(sInstance == null){
            sInstance = new VolleySingleton();
        }
        return sInstance;
    }
    public RequestQueue getmRequestQueue(){
        return mRequestQueue;
    }
    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}
