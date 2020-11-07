package com.ofk.bd.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ofk.bd.Utility.AnimationUtility;
import com.ofk.bd.Utility.StringUtility;
import com.ofk.bd.ViewModel.MainActivityViewModel;
import com.ofk.bd.databinding.FragmentActivityVideoBinding;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
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
        this.getLifecycle().addObserver(binding.youtubePlayer);
        binding.youtubePlayer.addYouTubePlayerListener(youTubePlayerListener);
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
        Picasso.get().load(getArguments().getString("videoThumb")).into(binding.videoThumbNail);
        binding.videoTitle.setText(getArguments().getString("videoTitle"));
    }

    private final YouTubePlayerListener youTubePlayerListener = new YouTubePlayerListener() {
        @Override
        public void onReady(YouTubePlayer youTubePlayer) {
            viewModel.getCurrentVideoURL().observe(getActivity(), new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    youTubePlayer.loadVideo(s, 0);
                }
            });
        }

        @Override
        public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState playerState) {
            String state = StringUtility.playerStateToString(playerState);
            if (state.equals("ENDED")) {
                AnimationUtility.endAnimation(getContext(), binding.playImage);
                AnimationUtility.endAnimation(getContext(), binding.videoThumbNail);
                AnimationUtility.endAnimation(getContext(), binding.videoTitle);
                AnimationUtility.endAnimation(getContext(), binding.gradientView);
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
    };

    private final View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == binding.playImage.getId() || view.getId() == binding.videoTitle.getId()) {

                AnimationUtility.startAnimation(getContext(), binding.playImage);
                AnimationUtility.startAnimation(getContext(), binding.videoThumbNail);
                AnimationUtility.startAnimation(getContext(), binding.videoTitle);
                AnimationUtility.startAnimation(getContext(), binding.gradientView);

                viewModel.getCurrentVideoURL().postValue(getArguments() != null ? getArguments().getString("videoId") : null);
            }
        }
    };
}