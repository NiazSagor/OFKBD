package com.ofk.bd;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ofk.bd.CourseActivityAdapter.CourseViewPager;
import com.ofk.bd.HelperClass.Common;
import com.ofk.bd.HelperClass.FullScreenHelper;
import com.ofk.bd.ViewModel.CourseActivityViewModel;
import com.ofk.bd.databinding.ActivityCourseBinding;
import com.ofk.bd.extractorlibrary.ExtractorException;
import com.ofk.bd.extractorlibrary.YoutubeStreamExtractor;
import com.ofk.bd.extractorlibrary.model.YTMedia;
import com.ofk.bd.extractorlibrary.model.YTSubtitles;
import com.ofk.bd.extractorlibrary.model.YoutubeMeta;

import java.util.List;

public class CourseActivity extends AppCompatActivity {

    private static final String TAG = "VIDEO_PLAY_HOY_JEKHANE";


    private SimpleExoPlayer player;
    private PlayerView playerView;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    boolean fullscreen = false;
    int switchNumber = 0;

    private ProgressBar progressBar;
    private ImageButton playPause;
    private AppCompatTextView titleTextView;

    private ActivityCourseBinding binding;

    private MenuItem prevMenuItem;

    private CourseActivityViewModel courseActivityViewModel;

    private FullScreenHelper fullScreenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCourseBinding.inflate(getLayoutInflater());
        fullScreenHelper = new FullScreenHelper(this);
        setContentView(binding.getRoot());

        playerView = binding.getRoot().findViewById(R.id.video_player_view);
        playerView.setKeepContentOnPlayerReset(true);
        progressBar = playerView.findViewById(R.id.progress_circular);
        playPause = playerView.findViewById(R.id.exo_play_pause);
        titleTextView = playerView.findViewById(R.id.header_tv);

        // This is the course we clicked in display course activity
        binding.courseName.setText(getIntent().getStringExtra("course_name"));

        courseActivityViewModel = ViewModelProviders.of(CourseActivity.this).get(CourseActivityViewModel.class);

        binding.bottomNavigation.setOnNavigationItemSelectedListener(mNavListener);
    }

    @Override
    protected void onStart() {
        super.onStart();

        initializePlayer();

        courseActivityViewModel.getCurrentVideoFromList().observe(CourseActivity.this, observer);

        // after the activity is visible to user
        setUpViewPager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        playPause.setOnClickListener(playButtonClickListener);
        playerView.findViewById(R.id.exo_fullscreen).setOnClickListener(onFullScreenButtonClickListener);
        binding.backButton.setOnClickListener(backButtonClickListener);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");

        Common.videoId = "";
        Common.sectionVideoList.clear();
        Common.CORRECT_ANSWER_COUNT = 1;
    }

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            //  Common.videoPos = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }

    // this is for the bottom nav
    private void setUpViewPager() {

        CourseViewPager adapter = new CourseViewPager(this);

        binding.videoRecourseViewPager.setUserInputEnabled(false);

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

    private void initializePlayer() {
        player = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
        playerView.setShutterBackgroundColor(Color.TRANSPARENT);
        player.addListener(playerEventListener);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
    }

    private final View.OnClickListener backButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (getIntent().getStringExtra("from").equals("home")) {
                startActivity(new Intent(CourseActivity.this, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            } else if (getIntent().getStringExtra("from").equals("display")) {

                startActivity(new Intent(CourseActivity.this, DisplayCourseActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        }
    };

    private final View.OnClickListener onFullScreenButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (fullscreen) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                fullScreenHelper.exitFullScreen(Common.ratio);
                fullscreen = false;
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                fullScreenHelper.enterFullScreen(Common.ratio);
                fullscreen = true;
            }
        }
    };

    private final Player.EventListener playerEventListener = new Player.EventListener() {
        @Override
        public void onPlaybackStateChanged(int state) {

            playerView.showController();

            switch (state) {
                case Player.STATE_BUFFERING:
                    progressBar.setVisibility(View.VISIBLE);
                    playPause.setVisibility(View.GONE);
                    break;
                case Player.STATE_READY:
                    progressBar.setVisibility(View.GONE);
                    playerView.showController();
                    playPause.setVisibility(View.VISIBLE);


                    binding.videoFrameLayout.setVisibility(View.VISIBLE);
                    ConstraintLayout parent = findViewById(R.id.parentLayout);
                    ConstraintSet set = new ConstraintSet();
                    set.clone(parent);
                    set.constrainWidth(R.id.videoFrameLayout, ConstraintSet.WRAP_CONTENT);
                    set.constrainHeight(R.id.videoFrameLayout, ConstraintSet.WRAP_CONTENT);
                    set.applyTo(parent);

                    break;
                case Player.STATE_ENDED:
                    progressBar.setVisibility(View.GONE);
                    courseActivityViewModel.updateTotalVideoWatched();
                    courseActivityViewModel.updateVideoWatched(getIntent().getStringExtra("course_name_english"));
                    break;
                case Player.STATE_IDLE:
                    progressBar.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private final View.OnClickListener playButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            AnimatedVectorDrawableCompat drawableCompat;
            AnimatedVectorDrawable animatedVectorDrawable;
            if (switchNumber == 0) {
                playPause.setImageDrawable(ContextCompat.getDrawable(CourseActivity.this, R.drawable.pause_to_play));
                Drawable drawable = playPause.getDrawable();

                if (drawable instanceof AnimatedVectorDrawableCompat) {
                    drawableCompat = (AnimatedVectorDrawableCompat) drawable;
                    drawableCompat.start();

                } else if (drawable instanceof AnimatedVectorDrawable) {
                    animatedVectorDrawable = (AnimatedVectorDrawable) drawable;
                    animatedVectorDrawable.start();
                }
                player.pause();
                switchNumber++;
            } else {
                playPause.setImageDrawable(ContextCompat.getDrawable(CourseActivity.this, R.drawable.play_to_pause));
                Drawable drawable = playPause.getDrawable();

                if (drawable instanceof AnimatedVectorDrawableCompat) {
                    drawableCompat = (AnimatedVectorDrawableCompat) drawable;
                    drawableCompat.start();
                } else if (drawable instanceof AnimatedVectorDrawable) {
                    animatedVectorDrawable = (AnimatedVectorDrawable) drawable;
                    animatedVectorDrawable.start();
                }
                player.play();
                switchNumber--;
            }
        }
    };

    private final Observer<String> observer = new Observer<String>() {
        @SuppressLint("StaticFieldLeak")
        @Override
        public void onChanged(String s) {
            if (s != null) {
                new YoutubeStreamExtractor(new YoutubeStreamExtractor.ExtractorListner() {
                    @Override
                    public void onExtractionDone(List<YTMedia> adativeStream, final List<YTMedia> muxedStream, List<YTSubtitles> subtitles, YoutubeMeta meta) {
                        if (meta != null) {

                            titleTextView.setText(meta.getTitle());

                            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(CourseActivity.this, getResources().getString(R.string.app_name));
                            MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(muxedStream.get(0).getUrl()));

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    player.prepare(mediaSource);
                                }
                            });
                        }
                    }

                    @Override
                    public void onExtractionGoesWrong(final ExtractorException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }).useDefaultLogin().Extract(s);
            }
        }
    };

    // Bottom navigation listener
    private final BottomNavigationView.OnNavigationItemSelectedListener mNavListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
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
}