package com.ofk.bd.Adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.ofk.bd.HelperClass.Video;
import com.ofk.bd.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
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

    private Picasso picasso;

    private List<Video> videoList;
    private Context mContext;

    public VideoSliderAdapter(List<Video> videoList, Context context) {
        this.videoList = videoList;
        this.mContext = context;
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

        gradientView = view.findViewById(R.id.gradientView);

        thumbNail = view.findViewById(R.id.videoThumbNail);
        picasso.load(videoList.get(position).getVideoThumbNail()).into(thumbNail);

        layout = view.findViewById(R.id.videoPlayLayout);

        youTubePlayerView = view.findViewById(R.id.youtube_player_view);

        //TODO try to do this in background thread
        youTubePlayerView.addYouTubePlayerListener(new YouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                Log.d(TAG, "onReady: ");
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String id = videoList.get(position).getVideoURL();
                        youTubePlayer.loadVideo(id, 0);
                    }
                });
            }

            @Override
            public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState playerState) {
                if (playerState == PlayerConstants.PlayerState.PLAYING) {
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

        container.addView(view);

        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((CardView) object);
    }

    private class AddListener extends AsyncTask<Void, Void, Void> {

        YouTubePlayerView youTubePlayerView;
        int position;

        public AddListener(YouTubePlayerView youTubePlayerView, int position) {
            this.youTubePlayerView = youTubePlayerView;
            this.position = position;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            youTubePlayerView.addYouTubePlayerListener(new YouTubePlayerListener() {
                @Override
                public void onReady(YouTubePlayer youTubePlayer) {
                    String id = videoList.get(position).getVideoURL();
                    youTubePlayer.loadVideo(id, 0);
                }

                @Override
                public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState playerState) {
                    if (playerState == PlayerConstants.PlayerState.PLAYING) {
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
                    Log.d(TAG, "onVideoId: " + s);
                }

                @Override
                public void onApiChange(YouTubePlayer youTubePlayer) {

                }
            });
            return null;
        }
    }
}
