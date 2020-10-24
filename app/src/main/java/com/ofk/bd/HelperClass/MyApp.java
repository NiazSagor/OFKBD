package com.ofk.bd.HelperClass;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;
import com.ofk.bd.Database.UserInfoDatabase;
import com.ofk.bd.Database.UserProgressDatabase;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class MyApp extends Application {

    private static final String TAG = "MyApp";
    
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        Log.d(TAG, "onCreate: ");
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(false);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
        UserInfoDatabase.getInstance(this);
        UserProgressDatabase.getInstance(this);
    }
}
