package com.ofk.bd;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ofk.bd.CourseActivityAdapter.CourseViewPager;
import com.ofk.bd.ViewModel.CourseActivityViewModel;
import com.ofk.bd.ViewModel.VideoFromListViewModel;
import com.ofk.bd.databinding.ActivityCourseBinding;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

public class CourseActivity extends FragmentActivity {

    private static final String TAG = "CourseActivity";

    private ActivityCourseBinding binding;

    private MenuItem prevMenuItem;

    private VideoFromListViewModel videoFromListViewModel;

    private CourseActivityViewModel courseActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // This is the course we clicked in display course activity
        binding.courseName.setText(getIntent().getStringExtra("course_name"));

        courseActivityViewModel = ViewModelProviders.of(CourseActivity.this).get(CourseActivityViewModel.class);

        List<String> list = new ArrayList<>();
        list.add(getIntent().getStringExtra("section_name"));
        list.add(getIntent().getStringExtra("course_name_english"));

        courseActivityViewModel.setCombinedList(list);

        // Calling view model to get the selected video from the fragment
        videoFromListViewModel = ViewModelProviders.of(CourseActivity.this).get(VideoFromListViewModel.class);

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CourseActivity.this, DisplayCourseActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });

        binding.bottomNavigation.setOnNavigationItemSelectedListener(mNavListener);
    }

    // Bottom navigation listener
    private BottomNavigationView.OnNavigationItemSelectedListener mNavListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_video:
                    binding.videoRecourseViewPager.setCurrentItem(0);
                    break;
                case R.id.nav_quiz:
                    binding.videoRecourseViewPager.setCurrentItem(1);
                    break;
                case R.id.nav_resources:
                    binding.videoRecourseViewPager.setCurrentItem(2);
                    break;
            }
            return false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        // after the activity is visible to user
        getPlayVideoFromList();
        setUpViewPager();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // Get the selected video from the list in fragment and play it in youtube player
    private void getPlayVideoFromList() {

        YouTubePlayerView youTubePlayerView = binding.youtubePlayerView;

        getLifecycle().addObserver(youTubePlayerView);

        new Thread(new Runnable() {
            @Override
            public void run() {
                youTubePlayerView.addYouTubePlayerListener(new YouTubePlayerListener() {
                    @Override
                    public void onReady(YouTubePlayer youTubePlayer) {
                        videoFromListViewModel.getMutableLiveData().observe(CourseActivity.this, new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                if (s != null) {
                                    youTubePlayer.loadVideo(s, 0);
                                }
                            }
                        });
                    }

                    @Override
                    public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState playerState) {
                        //TODO do something if 1 video is finished watching
                        if (playerState == PlayerConstants.PlayerState.ENDED) {

                            String courseName = getIntent().getStringExtra("course_name_english");

                            Log.d(TAG, "onStateChange: " + courseName);

                            int i = courseActivityViewModel.getCurrentVideoWatched(courseName);// course wise video watched

                            i++;

                            courseActivityViewModel.updateVideo(i, courseName);// course wise video update

                            int j = courseActivityViewModel.getVideoWatchedInTotal();// in total video watched
                            Log.d(TAG, "onStateChange: " + j);
                            j++;

                            courseActivityViewModel.updateUserVideoTotal(j);// in total video watched update
                        }
                    }

                    @Override
                    public void onPlaybackQualityChange(YouTubePlayer youTubePlayer, PlayerConstants.PlaybackQuality playbackQuality) {

                    }

                    @Override
                    public void onPlaybackRateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlaybackRate playbackRate) {

                    }

                    @Override
                    public void onError(YouTubePlayer youTubePlayer, PlayerConstants.PlayerError playerError) {

                    }

                    @Override
                    public void onCurrentSecond(YouTubePlayer youTubePlayer, float v) {

                    }

                    @Override
                    public void onVideoDuration(YouTubePlayer youTubePlayer, float v) {

                    }

                    @Override
                    public void onVideoLoadedFraction(YouTubePlayer youTubePlayer, float v) {

                    }

                    @Override
                    public void onVideoId(YouTubePlayer youTubePlayer, String s) {

                    }

                    @Override
                    public void onApiChange(YouTubePlayer youTubePlayer) {

                    }
                });
            }
        }).start();
    }

    // this is for the bottom nav
    private void setUpViewPager() {

        CourseViewPager adapter = new CourseViewPager(this);

        binding.videoRecourseViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    binding.bottomNavigation.getMenu().getItem(0).setChecked(false);
                }
                binding.bottomNavigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = binding.bottomNavigation.getMenu().getItem(position);
            }
        });

        binding.videoRecourseViewPager.setAdapter(adapter);
    }
}