package com.ofk.bd.DisplayCourseActivityAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ofk.bd.Model.DisplayCourse;
import com.ofk.bd.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.CourseListViewHolder> {

    public static final int HOME_PAGE = 1;
    public static final int DISPLAY_COURSE = 2;

    private Picasso mPicasso;

    private List<DisplayCourse> courseList;

    private static final String TAG = "CourseListAdapter";

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    private String mSender;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public CourseListAdapter(List<DisplayCourse> courseList, String sender) {
        this.courseList = courseList;
        this.mSender = sender;
        mPicasso = Picasso.get();
    }

    @NonNull
    @Override
    public CourseListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case 1:
                View view1 = inflater.inflate(R.layout.recom_course_home_layout, parent, false);
                return new CourseListViewHolder(view1, mListener);
            case 2:
                View view2 = inflater.inflate(R.layout.display_course_layout, parent, false);
                return new CourseListViewHolder(view2, mListener);

            default:
                View view3 = inflater.inflate(R.layout.section_video_item_layout, parent, false);
                return new CourseListViewHolder(view3, mListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CourseListViewHolder holder, int position) {

        holder.title.setText(courseList.get(position).getCourseTitle());
        mPicasso.load(courseList.get(position).getThumbnailURL()).into(holder.thumbnail);
        /*mPicasso.setIndicatorsEnabled(false);
        mPicasso.load(courseList.get(position).getThumbnailURL())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.thumbnail, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        mPicasso.load(courseList.get(position).getThumbnailURL())
                                .error(R.drawable.ofklogo)
                                .into(holder.thumbnail, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError(Exception e) {

                                    }
                                });
                    }
                });

         */
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

    @Override
    public int getItemViewType(int position) {
        if (mSender.equals("home_page")) {
            return HOME_PAGE;
        } else if (mSender.equals("displayCourse")) {
            return DISPLAY_COURSE;
        }
        return -1;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public DisplayCourse getCurrentCourse(int position) {
        return courseList.get(position);
    }
}
