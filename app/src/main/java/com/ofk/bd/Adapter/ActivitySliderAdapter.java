package com.ofk.bd.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ofk.bd.Model.Activity;
import com.ofk.bd.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ActivitySliderAdapter extends RecyclerView.Adapter<ActivitySliderAdapter.ActivityViewHolder> {

    private final List<Activity> activityList;

    private final Picasso picasso;

    public ActivitySliderAdapter(List<Activity> list) {
        this.activityList = list;
        picasso = Picasso.get();
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_layout, parent, false);
        return new ActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        picasso.load(activityList.get(position).getUrl()).into(holder.activityImageView);
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    public static class ActivityViewHolder extends RecyclerView.ViewHolder {

        final ImageView activityImageView;

        public ActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            activityImageView = itemView.findViewById(R.id.activityImageView);
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
