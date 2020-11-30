package com.ofk.bd;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.developer.kalert.KAlertDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.ofk.bd.AsyncTasks.FirebaseQueryActivity;
import com.ofk.bd.AsyncTasks.FirebaseQueryRandomCourse;
import com.ofk.bd.AsyncTasks.FirebaseQueryVideo;
import com.ofk.bd.HelperClass.Activity;
import com.ofk.bd.HelperClass.Common;
import com.ofk.bd.HelperClass.DisplayCourse;
import com.ofk.bd.HelperClass.Video;
import com.ofk.bd.Interface.ActivityPicLoadCallback;
import com.ofk.bd.Interface.DisplayCourseLoadCallback;
import com.ofk.bd.Interface.VideoLoadCallback;
import com.ofk.bd.Utility.StringUtility;
import com.ofk.bd.databinding.ActivitySplashBinding;

import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    public static final String COURSE_TO_DISPLAY = "course_to_display";

    public static final int FETCH_INTERVAL = 172800;

    private FirebaseAuth mAuth;

    private ActivitySplashBinding binding;

    private FirebaseRemoteConfig remoteConfig;

    private KAlertDialog pDialog;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("isVerified", MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();
        remoteConfig = FirebaseRemoteConfig.getInstance();

        // activating previously saved config
        remoteConfig.activate();

        initRemoteConfig();

        //new ShowProgressBar(SplashActivity.this).execute(3);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!isConnected()) {
            showAlertDialog("done");
            return;
        }

        // all these queries needs to be in splash activity
        new FirebaseQueryActivity(new ActivityPicLoadCallback() {
            @Override
            public void onPicLoadCallback(List<Activity> activityPics) {

                Common.activityList = activityPics;

                Log.d(TAG, "onPicLoadCallback: ");

            }
        }, "Activity Pics").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        new FirebaseQueryActivity(new ActivityPicLoadCallback() {
            @Override
            public void onPicLoadCallback(List<Activity> activityPics) {

                Common.fieldActivityList = activityPics;
            }
        }, "Field Work Pics").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        new FirebaseQueryVideo(new VideoLoadCallback() {
            @Override
            public void onLoadCallback(List<Video> list) {

                Common.activityVideoList = list;
            }
        }, "Activity Videos").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        new FirebaseQueryRandomCourse(new DisplayCourseLoadCallback() {
            @Override
            public void onLoadCallback(List<DisplayCourse> courses) {

                Common.randomCourses2 = courses;

                binding.progressBar.setVisibility(View.GONE);

                if (mAuth.getCurrentUser() == null) {

                    if (sharedPreferences.getBoolean("isVerified", false)) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    } else {
                        startActivity(new Intent(SplashActivity.this, InfoActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }

                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP));

                }
                finish();
            }
        }, remoteConfig.getString(COURSE_TO_DISPLAY)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void initRemoteConfig() {
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(FETCH_INTERVAL)
                .build();

        remoteConfig.setConfigSettingsAsync(configSettings);
        remoteConfig.setDefaultsAsync(R.xml.remote_default_values);

        remoteConfig.fetch();

        // getting the course name from server
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: " + remoteConfig.getString(COURSE_TO_DISPLAY));
                Common.courseToDisplay = remoteConfig.getString(COURSE_TO_DISPLAY);
                Common.courseHeadline = new StringUtility(getApplicationContext())
                        .getSectionHeadline(remoteConfig.getString(COURSE_TO_DISPLAY));
            }
        }).start();
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    private void showAlertDialog(String command) {
        if ("done".equals(command)) {
            pDialog = new KAlertDialog(this, KAlertDialog.ERROR_TYPE);
            pDialog.setTitleText("ইন্টারনেট সংযোগ নেই")
                    .setConfirmText("OK")
                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                        @Override
                        public void onClick(KAlertDialog kAlertDialog) {
                            pDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        }
    }
}