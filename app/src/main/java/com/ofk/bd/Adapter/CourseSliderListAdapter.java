package com.ofk.bd.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ofk.bd.HelperClass.Course;
import com.ofk.bd.R;

import java.util.List;

public class CourseSliderListAdapter extends RecyclerView.Adapter<CourseSliderListAdapter.CourseSliderListViewHolder> {

    private static final String TAG = "CourseSliderListAdapter";

    private int[] avatars = {R.drawable.dog, R.drawable.duck, R.drawable.fox, R.drawable.lion, R.drawable.lion, R.drawable.lion1, R.drawable.squirrel, R.drawable.duck};

    private List<Course> courseList;

    public CourseSliderListAdapter(List<Course> courseList) {
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public CourseSliderListAdapter.CourseSliderListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.course_item_layout, parent, false);
        return new CourseSliderListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseSliderListAdapter.CourseSliderListViewHolder holder, int position) {
        holder.courseTitleTextView.setText(courseList.get(position).getCourseTitle());
        holder.courseImageView.setImageResource(avatars[position]);
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class CourseSliderListViewHolder extends RecyclerView.ViewHolder {

        TextView courseTitleTextView;
        ImageView courseImageView;

        public CourseSliderListViewHolder(@NonNull View itemView) {
            super(itemView);

            courseTitleTextView = itemView.findViewById(R.id.courseTitle);
            courseImageView = itemView.findViewById(R.id.courseImage);

            //TODO set on click
        }
    }
}
