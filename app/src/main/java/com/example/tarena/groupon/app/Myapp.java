package com.example.tarena.groupon.app;

import android.app.Application;

/**
 * Created by tarena on 2017/6/19.
 */

public class Myapp extends Application {
    public static Myapp CONTEXT;

    @Override
    public void onCreate() {
        super.onCreate();
        CONTEXT=this;

    }
}
