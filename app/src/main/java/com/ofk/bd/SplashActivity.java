package com.ofk.bd;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

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
import com.ofk.bd.databinding.ActivitySplashBinding;

import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressBar = binding.progressBar;

        //new ShowProgressBar(SplashActivity.this).execute(3);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // all these queries needs to be in splash activity

        new FirebaseQueryRandomCourse(new DisplayCourseLoadCallback() {
            @Override
            public void onLoadCallback(List<DisplayCourse> courses) {

                Common.randomCourses = courses;
            }
        }, "Robotics Section").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        new FirebaseQueryRandomCourse(new DisplayCourseLoadCallback() {
            @Override
            public void onLoadCallback(List<DisplayCourse> courses) {

                Common.randomCourses2 = courses;
            }
        }, "Arts Section").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        new FirebaseQueryActivity(new ActivityPicLoadCallback() {
            @Override
            public void onPicLoadCallback(List<Activity> activityPics) {

                Common.activityList = activityPics;

            }
        }, "Activity Pics").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        new FirebaseQueryVideo(new VideoLoadCallback() {
            @Override
            public void onLoadCallback(List<Video> list) {

                Common.activityVideoList = list;

                binding.progressBar.setVisibility(View.GONE);

                startActivity(new Intent(SplashActivity.this, InfoActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP));

                finish();
            }
        }, "Activity Videos").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}