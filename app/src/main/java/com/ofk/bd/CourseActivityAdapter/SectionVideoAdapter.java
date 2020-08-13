package com.ofk.bd.CourseActivityAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ofk.bd.HelperClass.Video;
import com.ofk.bd.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SectionVideoAdapter extends RecyclerView.Adapter<SectionVideoAdapter.SectionVideoListViewHolder> {

    public static final int SECTION = 1;
    public static final int SEARCH = 2;

    private List<Video> videoList;
    private String mSender;
    private Picasso mPicasso;

    private OnItemClickListener mListener;

    int[] thumb_image = {R.drawable.kumra_thumb, R.drawable.banana_thumb, R.drawable.mango_thumb};

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    public SectionVideoAdapter(List<Video> videoList, String sender) {
        this.videoList = videoList;
        this.mSender = sender;
        mPicasso = Picasso.get();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public SectionVideoAdapter.SectionVideoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case 1:
                View sectionVideoView = inflater.inflate(R.layout.section_video_item_layout, parent, false);
                return new SectionVideoListViewHolder(sectionVideoView, mListener);

            case 2:
                View searchVideoView = inflater.inflate(R.layout.search_video_layout, parent, false);
                return new SectionVideoListViewHolder(searchVideoView, mListener);

            default:
                View view = inflater.inflate(R.layout.recom_course_home_layout, parent, false);
                return new SectionVideoListViewHolder(view, mListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull SectionVideoAdapter.SectionVideoListViewHolder holder, int position) {
        holder.videoTitle.setText(videoList.get(position).getVideoTitle());
/*
        mPicasso.load(videoList.get(position).getVideoThumbNail())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.videoThumbNail, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        mPicasso.load(videoList.get(position).getVideoThumbNail())
                                .error(R.drawable.ofklogo)
                                .into(holder.videoThumbNail, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError(Exception e) {

                                    }
                                });
                    }
                });


 */
        holder.videoThumbNail.setImageResource(thumb_image[position]);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public static class SectionVideoListViewHolder extends RecyclerView.ViewHolder {

        ImageView videoThumbNail;
        TextView videoTitle;

        public SectionVideoListViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            videoThumbNail = itemView.findViewById(R.id.videoThumbNail);
            videoTitle = itemView.findViewById(R.id.videoTitle);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();

                    if (listener != null) {
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position, view);
                        }
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mSender.equals("sectionVideo")) {
            return SECTION;
        } else if (mSender.equals("videoSearch")) {
            return SEARCH;
        }
        return -1;
    }
}
