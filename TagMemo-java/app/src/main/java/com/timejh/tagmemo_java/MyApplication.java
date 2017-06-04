package com.timejh.tagmemo_java;

import android.app.Application;
import android.content.Context;

import io.realm.Realm;

/**
 * Created by tokijh on 2017. 4. 29..
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Realm.init(this);
    }

    public static Context getContext() {
        return context;
    }
}
