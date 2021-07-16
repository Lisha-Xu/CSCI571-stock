package com.example.stocktradingapp.common;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MyApplication extends Application {
    private static Context context;
    public static RequestQueue requestQueue;

    public static Context getAppContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        // Instead of creating a RequestQueue object for each HTTP request, it is recommended that it be initialized in the Application
        requestQueue = Volley.newRequestQueue(this);
    }
}
