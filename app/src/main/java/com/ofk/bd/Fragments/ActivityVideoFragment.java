package com.ofk.bd.Fragments;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;
import com.ofk.bd.CourseActivity;
import com.ofk.bd.HelperClass.Common;
import com.ofk.bd.R;
import com.ofk.bd.Utility.AnimationUtility;
import com.ofk.bd.ViewModel.MainActivityViewModel;
import com.ofk.bd.databinding.FragmentActivityVideoBinding;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ActivityVideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActivityVideoFragment extends Fragment {

    private static final String TAG = "ActivityVideoFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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

    public ActivityVideoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ActivityVideoFragment newInstance(String param1, String param2) {
        ActivityVideoFragment fragment = new ActivityVideoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private MainActivityViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        if (viewModel == null) {
            viewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
        }
    }

    private FragmentActivityVideoBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentActivityVideoBinding.inflate(getLayoutInflater());

        playerView = binding.getRoot().findViewById(R.id.video_player_view);
        progressBar = binding.getRoot().findViewById(R.id.progress_circular);
        playPause = binding.getRoot().findViewById(R.id.exo_play_pause);
        AppCompatTextView headerTextView = binding.getRoot().findViewById(R.id.header_tv);
        headerTextView.setVisibility(View.INVISIBLE);
        ImageButton fullScreenButton = binding.getRoot().findViewById(R.id.exo_fullscreen);
        fullScreenButton.setVisibility(View.INVISIBLE);
        playPause.setOnClickListener(playButtonClickListener);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.playImage.setOnClickListener(listener);
        binding.videoTitle.setOnClickListener(listener);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (Util.SDK_INT >= 24) {
            initializePlayer();
        }

        Picasso.get().load(getArguments().getString("videoThumb")).into(binding.videoThumbNail);
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


    private final View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == binding.playImage.getId() || view.getId() == binding.videoTitle.getId()) {

                AnimationUtility.startAnimation(getContext(), binding.playImage);
                AnimationUtility.startAnimation(getContext(), binding.videoThumbNail);
                AnimationUtility.startAnimation(getContext(), binding.videoTitle);
                AnimationUtility.startAnimation(getContext(), binding.gradientView);

                Common.videoId = getArguments().getString("videoId");

                getActivity().startActivity(new Intent(getActivity(), CourseActivity.class).putExtra("from", "home"));
            }
        }
    };
}