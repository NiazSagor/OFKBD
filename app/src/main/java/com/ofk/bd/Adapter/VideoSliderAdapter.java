package com.ofk.bd.Adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ofk.bd.Fragments.ActivityVideoFragment;
import com.ofk.bd.Model.Video;

import java.util.List;

public class VideoSliderAdapter extends FragmentStateAdapter {

    private static final String TAG = "VideoSliderAdapter";

    private final List<Video> videos;

    public VideoSliderAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<Video> videoList) {
        super(fragmentManager, lifecycle);
        this.videos = videoList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("videoId", videos.get(position).getUrl());
        ActivityVideoFragment fragment = new ActivityVideoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }
}
