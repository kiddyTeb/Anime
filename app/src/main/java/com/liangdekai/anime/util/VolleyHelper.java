package com.liangdekai.anime.util;

import android.util.Log;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.liangdekai.anime.MyApplication;
import com.liangdekai.anime.R;
import com.liangdekai.anime.db.CartoonDB;

import org.json.JSONObject;

public class VolleyHelper {
    public interface VolleyListener{
        void onSucceed(String jsonString);
        void onFailed();
    }

    public static void sendByVolley(String address , final VolleyListener listener){
        RequestQueue requestQueue = MyApplication.getRequestQueue() ;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(address, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("test" , jsonObject.toString());
                        listener.onSucceed(jsonObject.toString());
                    }
                }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    listener.onFailed();
                }
        });
        requestQueue.add(jsonObjectRequest) ;
    }

    public static void downloadBitmap(String url , NetworkImageView imageView){
        RequestQueue requestQueue = MyApplication.getRequestQueue() ;
        ImageLoader imageLoader = new ImageLoader(requestQueue, MyApplication.getBitmapCache()) ;
        imageView.setDefaultImageResId(R.mipmap.empty);
        imageView.setErrorImageResId(R.mipmap.empty);
        imageView.setImageUrl(url , imageLoader);
    }
}
