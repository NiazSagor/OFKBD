package com.ofk.bd.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ofk.bd.HelperClass.SectionCourseTuple;
import com.ofk.bd.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProgressListAdapter extends RecyclerView.Adapter<ProgressListAdapter.ProgressListViewHolder> {

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

        //String progressPercentage = progressList.get(position).getSubjectProgress() + " %";
        //holder.totalVideosTextView.setText("Total Videos " + progressList.get(position).getTotalVideos());

        String heading = "কোর্স" + " : " + progressList.get(position).getCourseEnrolled();
        String video = "ভিডিও সম্পন্ন" + " : " + progressList.get(position).getVideoWatched();

        holder.progressBar.setMax(progressList.get(position).getTotalVideos());
        holder.progressBar.setProgress(progressList.get(position).getVideoWatched());
        holder.subjectTitle.setText(heading);
        holder.videosWatchedTextView.setText(video);

        if (progressList.get(position).getCourseThumbnailURL() != null) {
            picasso.load(progressList.get(position).getCourseThumbnailURL())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(holder.courseThumbnailImageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            picasso.load(progressList.get(position).getCourseThumbnailURL())
                                    .error(R.drawable.ofklogo)
                                    .into(holder.courseThumbnailImageView, new Callback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onError(Exception e) {

                                        }
                                    });
                        }
                    });
        }


    }

    @Override
    public int getItemCount() {
        return progressList.size();
    }

    public static class ProgressListViewHolder extends RecyclerView.ViewHolder {
        ImageView courseThumbnailImageView;
        TextView subjectTitle;
        TextView totalVideosTextView;
        TextView videosWatchedTextView;
        ProgressBar progressBar;

        public ProgressListViewHolder(@NonNull View itemView) {
            super(itemView);
            courseThumbnailImageView = itemView.findViewById(R.id.courseThumbNailImageView);
            subjectTitle = itemView.findViewById(R.id.subjectTitle);
            totalVideosTextView = itemView.findViewById(R.id.totalVideos);
            videosWatchedTextView = itemView.findViewById(R.id.videoWatched);
            progressBar = itemView.findViewById(R.id.progressBar);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}
