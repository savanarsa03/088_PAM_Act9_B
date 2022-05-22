package com.tanjung.mysqlpro.app;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AppContoller extends Application {
    public static final String TAG = AppContoller.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private static AppContoller mInstance;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mInstance = this;
    }
    public static synchronized AppContoller getInstance() {return mInstance;}
    public RequestQueue getmRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req, String tag)
    {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getmRequestQueue().add((req));
    }
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getmRequestQueue().add(req);
    }
    public void cancelPendingRequest(Object tag) {
        if (mRequestQueue != null)
        {
            mRequestQueue.cancelAll(tag);
        }
    }
}