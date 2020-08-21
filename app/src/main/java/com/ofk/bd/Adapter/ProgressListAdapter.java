package com.ofk.bd.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ofk.bd.HelperClass.SectionCourseTuple;
import com.ofk.bd.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProgressListAdapter extends RecyclerView.Adapter<ProgressListAdapter.ProgressListViewHolder> {

    private static final String TAG = "ProgressListAdapter";

    private List<SectionCourseTuple> progressList;

    private Picasso picasso;

    public ProgressListAdapter(List<SectionCourseTuple> progressList) {
        this.progressList = progressList;
        picasso = Picasso.get();
    }

    @NonNull
    @Override
    public ProgressListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.progress_layout, parent, false);
        return new ProgressListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgressListViewHolder holder, int position) {

        Log.d(TAG, "onBindViewHolder: " + progressList.get(0).getVideoWatched());
        Log.d(TAG, "onBindViewHolder: " + progressList.get(0).getTotalVideos());

        double amount = (double) progressList.get(position).getVideoWatched() / progressList.get(position).getTotalVideos();
        double res = amount * 100;

        String percentage = (int) res + "%";

        Log.d(TAG, "onBindViewHolder: " + percentage);

        String heading = "" + progressList.get(position).getCourseEnrolled();
        String video = "" + progressList.get(position).getVideoWatched();
        String totalVideo = "" + progressList.get(position).getTotalVideos();

        //holder.sectionName.setText(progressList.get(position).getSectionNameBangla());
        holder.subjectTitle.setText(heading);
        //holder.videosWatchedTextView.setText(video);
        //holder.totalVideosTextView.setText(totalVideo);
        holder.progressBar.setMax(progressList.get(position).getTotalVideos());
        holder.progressBar.setProgress(progressList.get(position).getVideoWatched());
        holder.progressPercentage.setText(percentage);
    }

    @Override
    public int getItemCount() {
        return progressList.size();
    }

    public static class ProgressListViewHolder extends RecyclerView.ViewHolder {
        //ImageView courseThumbnailImageView;
        TextView subjectTitle;
        //TextView sectionName;
        TextView totalVideosTextView;
        TextView videosWatchedTextView;
        TextView progressPercentage;
        ProgressBar progressBar;

        public ProgressListViewHolder(@NonNull View itemView) {
            super(itemView);
            //courseThumbnailImageView = itemView.findViewById(R.id.courseThumbNailImageView);
            subjectTitle = itemView.findViewById(R.id.subjectTitle);
            //totalVideosTextView = itemView.findViewById(R.id.totalVideos);
            //videosWatchedTextView = itemView.findViewById(R.id.videoWatched);
            progressBar = itemView.findViewById(R.id.progressBar);
            progressPercentage = itemView.findViewById(R.id.progressPercentage);
            //sectionName = itemView.findViewById(R.id.sectionName);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}
