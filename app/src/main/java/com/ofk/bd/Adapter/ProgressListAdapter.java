package com.ofk.bd.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ofk.bd.HelperClass.SectionCourseTuple;
import com.ofk.bd.R;
import com.ofk.bd.Utility.StringUtility;

import java.util.List;

public class ProgressListAdapter extends RecyclerView.Adapter<ProgressListAdapter.ProgressListViewHolder> {

    private static final String TAG = "ProgressListAdapter";

    private final List<SectionCourseTuple> progressList;

    public ProgressListAdapter(List<SectionCourseTuple> progressList) {
        this.progressList = progressList;
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
        holder.subjectTitle.setText(progressList.get(position).getCourseEnrolled());
        holder.progressBar.setMax(progressList.get(position).getTotalVideos());
        holder.progressBar.setProgress(progressList.get(position).getVideoWatched());
        holder.progressPercentage.setText(StringUtility.getCourseCompletionPercentage(progressList.get(position).getVideoWatched(), progressList.get(position).getTotalVideos()));
    }

    @Override
    public int getItemCount() {
        return progressList.size();
    }

    public static class ProgressListViewHolder extends RecyclerView.ViewHolder {
        TextView subjectTitle;
        TextView progressPercentage;
        ProgressBar progressBar;

        public ProgressListViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectTitle = itemView.findViewById(R.id.subjectTitle);
            progressBar = itemView.findViewById(R.id.progressBar);
            progressPercentage = itemView.findViewById(R.id.progressPercentage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO show some info regarding the course
                }
            });
        }
    }
}
