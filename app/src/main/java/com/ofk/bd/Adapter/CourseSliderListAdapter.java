package com.ofk.bd.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ofk.bd.HelperClass.Course;
import com.ofk.bd.R;
import com.ofk.bd.Utility.DrawableUtility;

import java.util.List;

public class CourseSliderListAdapter extends RecyclerView.Adapter<CourseSliderListAdapter.CourseSliderListViewHolder> {

    public static final int VIEW_PAGER_COURSE = 1;
    public static final int BLOG_ITEM = 3;

    private final String mSender;
    private final Context mContext;
    private final List<Course> courseList;

    private static final String TAG = "CourseSliderListAdapter";

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public CourseSliderListAdapter(Context context, List<Course> courseList, String sender) {
        this.courseList = courseList;
        this.mSender = sender;
        this.mContext = context;
    }


    @NonNull
    @Override
    public CourseSliderListAdapter.CourseSliderListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case 1:
                View sectionLayout = inflater.inflate(R.layout.course_item_layout, parent, false);
                return new CourseSliderListViewHolder(sectionLayout, mListener);

            case 3:
                View blogLayout = inflater.inflate(R.layout.blog_item_layout, parent, false);
                return new CourseSliderListViewHolder(blogLayout, mListener);

            default:
                View view = inflater.inflate(R.layout.age_layout, parent, false);
                return new CourseSliderListViewHolder(view, mListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CourseSliderListAdapter.CourseSliderListViewHolder holder, int position) {

        if (mSender.equals("blog")) {
            holder.courseTitleTextView.setText(courseList.get(position).getCourseTitle());
            holder.courseImageView.setImageDrawable(DrawableUtility.getBlogThumb(mContext, position));
        } else if (mSender.equals("viewpager0")) {
            holder.courseTitleTextView.setText(courseList.get(position).getCourseSubtitle());
            holder.courseImageView.setImageDrawable(DrawableUtility.getSectionIcon(mContext, position));
            holder.gradientView.setBackground(DrawableUtility.getGradient(mContext, position));
        }
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class CourseSliderListViewHolder extends RecyclerView.ViewHolder {

        View gradientView;
        TextView courseTitleTextView;
        ImageView courseImageView;
        TextView courseSubtitle;

        public CourseSliderListViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            gradientView = itemView.findViewById(R.id.gradientView);
            courseTitleTextView = itemView.findViewById(R.id.courseTitle);
            courseImageView = itemView.findViewById(R.id.courseImage);
            courseSubtitle = itemView.findViewById(R.id.courseSubtitle);

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
        if (mSender.equals("viewpager") || mSender.equals("viewpager0") || mSender.equals("viewpager1")) {
            return VIEW_PAGER_COURSE;
        } else if (mSender.equals("blog")) {
            return BLOG_ITEM;
        }
        return -1;
    }
}
