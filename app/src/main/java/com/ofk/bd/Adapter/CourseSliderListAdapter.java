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

    public static final int VIEW_PAGER_COURSE = 1;
    public static final int RANDOM_COURSE = 2;
    public static final int BLOG_ITEM = 3;
    private static final int TUTORIAL = 4;

    private String mSender;

    private static final String TAG = "CourseSliderListAdapter";

    private static int[] firstSixIcons = {R.drawable.art, R.drawable.calligraphy, R.drawable.case_solve, R.drawable.craft, R.drawable.critical, R.drawable.digital,
            R.drawable.guitar, R.drawable.programming, R.drawable.robotics};
    private static int[] secondFiveIcons = {R.drawable.guitar, R.drawable.programming, R.drawable.robotics};
    private static int[] firstSixGradients = {R.drawable.gradient_pink, R.drawable.gradient_cyan, R.drawable.gradient_purple,
            R.drawable.gradient_blue, R.drawable.gradient_purple_pink, R.drawable.gradient_pink_yellow,
            R.drawable.gradient_pink, R.drawable.gradient_cyan, R.drawable.gradient_purple};

    private static int[] blogThumbs = {R.drawable.mental, R.drawable.story, R.drawable.video, R.drawable.skill, R.drawable.awarness, R.drawable.fiction};

    private List<Course> courseList;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public CourseSliderListAdapter(List<Course> courseList, String sender) {
        this.courseList = courseList;
        this.mSender = sender;
    }


    @NonNull
    @Override
    public CourseSliderListAdapter.CourseSliderListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case 1:
                View viewPagerCourseLayout = inflater.inflate(R.layout.course_item_layout, parent, false);
                return new CourseSliderListViewHolder(viewPagerCourseLayout, mListener);

            case 2:
                View randomCourseLayout = inflater.inflate(R.layout.random_course_layout, parent, false);
                return new CourseSliderListViewHolder(randomCourseLayout, mListener);

            case 3:
                View resourceLayout = inflater.inflate(R.layout.blog_item_layout, parent, false);
                return new CourseSliderListViewHolder(resourceLayout, mListener);
            case 4:
                View moreCourseLayout = inflater.inflate(R.layout.display_course_layout, parent, false);
                return new CourseSliderListViewHolder(moreCourseLayout, mListener);

            default:
                View view = inflater.inflate(R.layout.age_layout, parent, false);
                return new CourseSliderListViewHolder(view, mListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CourseSliderListAdapter.CourseSliderListViewHolder holder, int position) {

        if (mSender.equals("random")) {
            holder.courseTitleTextView.setText(courseList.get(position).getCourseTitle());
            holder.courseSubtitle.setText(courseList.get(position).getCourseSubtitle());
            //holder.courseImageView.setImageResource(avatars[position]);
        } else if (mSender.equals("blog")) {
            holder.courseTitleTextView.setText(courseList.get(position).getCourseTitle());
            holder.courseImageView.setBackgroundResource(blogThumbs[position]);
        } else if (mSender.equals("viewpager")) {
            holder.courseTitleTextView.setText(courseList.get(position).getCourseTitle());
            holder.courseImageView.setImageResource(firstSixIcons[position]);
        } else if (mSender.equals("course_resource")) {
            holder.courseTitleTextView.setText(courseList.get(position).getCourseTitle());
            holder.courseImageView.setImageResource(firstSixIcons[position]);
        } else if (mSender.equals("viewpager0")) {
            holder.courseTitleTextView.setText(courseList.get(position).getCourseSubtitle());
            holder.courseImageView.setImageResource(firstSixIcons[position]);
            holder.gradientView.setBackgroundResource(firstSixGradients[position]);
        } else if (mSender.equals("viewpager1")) {
            holder.courseTitleTextView.setText(courseList.get(position).getCourseSubtitle());
            holder.courseImageView.setImageResource(secondFiveIcons[position]);
            holder.gradientView.setBackgroundResource(firstSixGradients[position]);
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
        } else if (mSender.equals("random")) {
            return RANDOM_COURSE;
        } else if (mSender.equals("blog")) {
            return BLOG_ITEM;
        } else if (mSender.equals("tutorial")) {
            return TUTORIAL;
        }
        return -1;
    }
}
