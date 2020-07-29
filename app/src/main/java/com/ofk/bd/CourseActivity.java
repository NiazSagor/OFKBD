package com.ofk.bd;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ofk.bd.CourseActivityAdapter.CourseViewPager;
import com.ofk.bd.ViewModel.VideoFromListViewModel;
import com.ofk.bd.databinding.ActivityCourseBinding;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class CourseActivity extends AppCompatActivity {

    private static final String TAG = "CourseActivity";

    private ActivityCourseBinding binding;
    private MenuItem prevMenuItem;

    private VideoFromListViewModel videoFromListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // This is the course we clicked in home fragment
        binding.courseName.setText(getIntent().getStringExtra("course_name"));

        binding.bottomNavigation.setOnNavigationItemSelectedListener(mNavListener);

        // Calling view model to get the selected video from the fragment
        videoFromListViewModel = ViewModelProviders.of(CourseActivity.this).get(VideoFromListViewModel.class);
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
        // after the activity is destroyed we release the youtube player
        binding.youtubePlayerView.release();
    }

    // Get the selected video from the list in fragment and play it in youtube player
    private void getPlayVideoFromList() {

        binding.youtubePlayerView.addYouTubePlayerListener(new YouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                videoFromListViewModel.getMutableLiveData().observe(CourseActivity.this, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        if (s != null) {
                            Log.d(TAG, "onChanged: " + s);
                            youTubePlayer.loadVideo(s, 0);
                        } else {
                            //TODO get the 1st video from the selected course and play it
                        }
                    }
                });
            }

            @Override
            public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState playerState) {
                //TODO do something if 1 video is finished watching
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

    // this is for the bottom nav
    private void setUpViewPager() {

        CourseViewPager adapter = new CourseViewPager(getSupportFragmentManager());

        binding.videoRecourseViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

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

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        binding.videoRecourseViewPager.setAdapter(adapter);
    }
}