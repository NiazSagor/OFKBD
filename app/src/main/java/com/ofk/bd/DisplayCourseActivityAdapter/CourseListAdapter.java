package com.ofk.bd.DisplayCourseActivityAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ofk.bd.Adapter.CourseSliderListAdapter;
import com.ofk.bd.HelperClass.DisplayCourse;
import com.ofk.bd.R;

import java.util.List;

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.CourseListViewHolder> {

    private List<DisplayCourse> courseList;

    private static final String TAG = "CourseListAdapter";

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public CourseListAdapter(List<DisplayCourse> courseList) {
        this.courseList = courseList;
        Log.d(TAG, "CourseListAdapter: " + courseList.get(0).getCourseTitle());
    }

    @NonNull
    @Override
    public CourseListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.display_course_layout, parent, false);
        return new CourseListViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseListViewHolder holder, int position) {
        //holder.thumbnail.setImageResource();
        holder.title.setText(courseList.get(position).getCourseTitle());
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class CourseListViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail;
        TextView title;

        public CourseListViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            title = itemView.findViewById(R.id.courseTitle);
            thumbnail = itemView.findViewById(R.id.courseThumbNailImageView);

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
