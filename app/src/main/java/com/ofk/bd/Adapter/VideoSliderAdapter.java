package com.ofk.bd.Adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.PagerAdapter;

import com.ofk.bd.HelperClass.Video;
import com.ofk.bd.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.view.View.GONE;

public class VideoSliderAdapter extends PagerAdapter {

    private boolean doNotifyDataSetChangedOnce = false;

    private static final String TAG = "VideoSliderAdapter";

    private YouTubePlayerView youTubePlayerView;
    private View gradientView;
    private ImageView thumbNail;
    private LinearLayout layout;
    private ProgressBar progressBar;

    private Picasso picasso;

    private List<Video> videoList;
    private Context mContext;

    private Lifecycle mLifeCycle;

    public VideoSliderAdapter(List<Video> videoList, Context context, Lifecycle lifecycle) {
        this.videoList = videoList;
        this.mContext = context;
        this.mLifeCycle = lifecycle;
        doNotifyDataSetChangedOnce = true;
        picasso = Picasso.get();
    }

    @Override
    public int getCount() {

        if (doNotifyDataSetChangedOnce) {
            doNotifyDataSetChangedOnce = false;
            notifyDataSetChanged();
        }

        return videoList.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (CardView) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.video_paly_layout, container, false);

        TextView title = view.findViewById(R.id.videoTitle);
        title.setText(videoList.get(position).getVideoTitle());

        progressBar = view.findViewById(R.id.progressBar);

        gradientView = view.findViewById(R.id.gradientView);

        thumbNail = view.findViewById(R.id.videoThumbNail);

        picasso.setIndicatorsEnabled(false);
        picasso.load(videoList.get(position).getVideoThumbNail())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(thumbNail, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        picasso.load(videoList.get(position).getVideoThumbNail())
                                .error(R.drawable.ofklogo)
                                .into(thumbNail, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError(Exception e) {

                                    }
                                });
                    }
                });

        layout = view.findViewById(R.id.videoPlayLayout);

        youTubePlayerView = view.findViewById(R.id.youtube_player_view);

        mLifeCycle.addObserver(youTubePlayerView);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String id = videoList.get(position).getVideoURL();
                new AddListener(youTubePlayerView, id).execute();
            }
        });

        container.addView(view);

        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((CardView) object);
    }

    private class AddListener extends AsyncTask<Void, Void, Void> {

        YouTubePlayerView youTubePlayerView;
        String mUrl;
        int position;

        public AddListener(YouTubePlayerView youTubePlayerView, String url) {
            this.youTubePlayerView = youTubePlayerView;
            mUrl = url;
            this.position = position;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            youTubePlayerView.addYouTubePlayerListener(new YouTubePlayerListener() {
                @Override
                public void onReady(YouTubePlayer youTubePlayer) {
                    youTubePlayer.loadVideo(mUrl, 0);
                }

                @Override
                public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState playerState) {
                    if (playerState == PlayerConstants.PlayerState.PLAYING) {
                        progressBar.setVisibility(GONE);
                        layout.setVisibility(GONE);
                        gradientView.setVisibility(GONE);
                        thumbNail.setVisibility(GONE);
                    } else if (playerState == PlayerConstants.PlayerState.ENDED) {
                        layout.setVisibility(View.VISIBLE);
                        gradientView.setVisibility(View.VISIBLE);
                        thumbNail.setVisibility(View.VISIBLE);
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
            return null;
        }
    }
}
