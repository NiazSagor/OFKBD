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

    private List<Video> videoList;

    private Picasso mPicasso;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    public SectionVideoAdapter(List<Video> videoList) {
        this.videoList = videoList;
        mPicasso = Picasso.get();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public SectionVideoAdapter.SectionVideoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.section_video_item_layout, parent, false);
        return new SectionVideoListViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionVideoAdapter.SectionVideoListViewHolder holder, int position) {
        holder.videoTitle.setText(videoList.get(position).getVideoTitle());
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
}
