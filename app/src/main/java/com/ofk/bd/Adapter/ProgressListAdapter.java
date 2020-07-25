package com.ofk.bd.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ofk.bd.HelperClass.Progress;
import com.ofk.bd.R;

import java.util.List;

public class ProgressListAdapter extends RecyclerView.Adapter<ProgressListAdapter.ProgressListViewHolder> {

    List<Progress> progressList;

    public ProgressListAdapter(List<Progress> progressList) {
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

        String progressPercentage = progressList.get(position).getSubjectProgress() + " %";

        holder.progressSeekBar.setMax(100);
        holder.progressSeekBar.setProgress(progressList.get(position).getSubjectProgress());
        holder.subjectTitle.setText(progressList.get(position).getSubjectTitle());
        holder.progressPercentage.setText(progressPercentage);
    }

    @Override
    public int getItemCount() {
        return progressList.size();
    }

    public static class ProgressListViewHolder extends RecyclerView.ViewHolder {

        TextView subjectTitle;
        TextView progressPercentage;
        SeekBar progressSeekBar;

        public ProgressListViewHolder(@NonNull View itemView) {
            super(itemView);

            subjectTitle = itemView.findViewById(R.id.subjectTitle);
            progressPercentage = itemView.findViewById(R.id.progressPercentage);
            progressSeekBar = itemView.findViewById(R.id.progressSeekBar);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}
