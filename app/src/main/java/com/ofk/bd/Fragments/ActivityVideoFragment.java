package com.ofk.bd.Fragments;

import android.annotation.SuppressLint;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.ofk.bd.Interface.YtExtractorCallback;
import com.ofk.bd.R;
import com.ofk.bd.Utility.AnimationUtility;
import com.ofk.bd.databinding.FragmentActivityVideoBinding;
import com.ofk.bd.extractorlibrary.ExtractorException;
import com.ofk.bd.extractorlibrary.YoutubeStreamExtractor;
import com.ofk.bd.extractorlibrary.model.YTMedia;
import com.ofk.bd.extractorlibrary.model.YTSubtitles;
import com.ofk.bd.extractorlibrary.model.YoutubeMeta;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ActivityVideoFragment extends Fragment implements YtExtractorCallback {

    private static final String TAG = "ActivityVideoFragment";

    private AnimatedVectorDrawableCompat drawableCompat;
    private AnimatedVectorDrawable animatedVectorDrawable;

    private SimpleExoPlayer player;
    private PlayerView playerView;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    boolean fullscreen = false;
    int switchNumber = 0;

    private ProgressBar progressBar;
    private ImageButton playPause;
    private YtExtractorCallback callback;
    private MediaSource mediaSource;
    @SuppressLint("StaticFieldLeak")

    private final View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == binding.playImage.getId() || view.getId() == binding.videoTitle.getId()) {

                AnimationUtility.startAnimation(getContext(), binding.playImage);
                AnimationUtility.startAnimation(getContext(), binding.videoThumbNail);
                AnimationUtility.startAnimation(getContext(), binding.videoTitle);
                AnimationUtility.startAnimation(getContext(), binding.gradientView);

                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        player.prepare(mediaSource);
                        player.play();
                    }
                });

            }
        }
    };

    private FragmentActivityVideoBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentActivityVideoBinding.inflate(getLayoutInflater());

        playerView = binding.getRoot().findViewById(R.id.video_player_view);
        playerView.setShowRewindButton(false);
        playerView.setShowFastForwardButton(false);
        playerView.findViewById(R.id.header_tv).setVisibility(View.INVISIBLE);
        playerView.findViewById(R.id.exo_fullscreen).setVisibility(View.INVISIBLE);
        progressBar = playerView.findViewById(R.id.progress_circular);
        playPause = playerView.findViewById(R.id.exo_play_pause);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        playPause.setOnClickListener(playButtonClickListener);
        binding.playImage.setOnClickListener(listener);
        binding.videoTitle.setOnClickListener(listener);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (Util.SDK_INT >= 24) {
            initializePlayer();
        }

        binding.videoTitle.setText(getArguments().getString("videoTitle"));
    }

    @Override
    public void onResume() {
        super.onResume();

        if ((Util.SDK_INT < 24 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
        player.pause();
    }

    private void initializePlayer() {
        player = new SimpleExoPlayer.Builder(getContext()).build();
        playerView.setPlayer(player);
        player.addListener(playerEventListener);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
    }

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }

    private final Player.EventListener playerEventListener = new Player.EventListener() {
        @Override
        public void onPlaybackStateChanged(int state) {

            switch (state) {
                case Player.STATE_BUFFERING:
                    progressBar.setVisibility(View.VISIBLE);
                    playPause.setVisibility(View.GONE);
                    break;
                case Player.STATE_READY:
                    progressBar.setVisibility(View.GONE);
                    playerView.showController();
                    playPause.setVisibility(View.VISIBLE);
                    break;
                case Player.STATE_ENDED:
                    progressBar.setVisibility(View.GONE);
                    AnimationUtility.endAnimation(getContext(), binding.playImage);
                    AnimationUtility.endAnimation(getContext(), binding.videoThumbNail);
                    AnimationUtility.endAnimation(getContext(), binding.videoTitle);
                    AnimationUtility.endAnimation(getContext(), binding.gradientView);
                    break;
                case Player.STATE_IDLE:
                    progressBar.setVisibility(View.GONE);
                    break;
            }
        }
    };

    private final View.OnClickListener playButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (switchNumber == 0) {
                playPause.setImageDrawable(getResources().getDrawable(R.drawable.pause_to_play));
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
                playPause.setImageDrawable(getResources().getDrawable(R.drawable.play_to_pause));
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            extractVideo(getArguments().getString("videoId"));
        }

        callback = this;
    }

    @Override
    public void onVideoThumbnail(String url) {
        Picasso.get().load(url).into(binding.videoThumbNail);
    }

    @Override
    public void onVideoStream(YTMedia ytMedia) {
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(requireActivity(), getResources().getString(R.string.app_name));
        mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(ytMedia.getUrl()));
    }

    private void extractVideo(String videoId) {
        new YoutubeStreamExtractor(new YoutubeStreamExtractor.ExtractorListner() {
            @Override
            public void onExtractionDone(List<YTMedia> adativeStream, final List<YTMedia> muxedStream, List<YTSubtitles> subtitles, YoutubeMeta meta) {
                if (meta != null) {

                    callback.onVideoThumbnail(meta.getThumbnail().getThumbnails().get(0).getUrl());

                    callback.onVideoStream(muxedStream.get(0));
                }
            }

            @Override
            public void onExtractionGoesWrong(final ExtractorException e) {
                Log.d(TAG, "onExtractionGoesWrong: " + e.getMessage());
            }
        }).useDefaultLogin().Extract(videoId);
    }
}