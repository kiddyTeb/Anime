package com.liangdekai.anime;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.liangdekai.anime.util.BitmapCache;

/**
 * Created by asus on 2016/8/8.
 */
public class MyApplication extends Application {
    private static Context context;
    private static RequestQueue requestQueue;
    private static BitmapCache bitmapCache ;

    @Override
    public void onCreate() {
        super.onCreate();//?attr/colorPrimary
        context = getApplicationContext();
        requestQueue = Volley.newRequestQueue(this , null);
        bitmapCache = new BitmapCache();
    }

    public static Context getContext() {
        return context;
    }

    public static BitmapCache getBitmapCache(){
        return bitmapCache;
    }

    public static RequestQueue getRequestQueue(){
        return requestQueue;
    }
}
