package com.shop440.Utils;

/**
 * Created by smile on 1/12/16.
 */

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by smilecs on 6/3/15.
 */
public class VolleySingleton {
    private static VolleySingleton sInstance = null;
    private RequestQueue mRequestQueue;

    private VolleySingleton(){
        mRequestQueue = Volley.newRequestQueue(Application.getAppContext());

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
}
