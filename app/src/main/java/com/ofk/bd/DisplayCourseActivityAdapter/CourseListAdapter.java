package com.ofk.bd.DisplayCourseActivityAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.ofk.bd.Model.DisplayCourse;
import com.ofk.bd.R;
import com.squareup.picasso.Picasso;

public class CourseListAdapter extends FirebaseRecyclerAdapter<DisplayCourse, CourseListAdapter.CourseListViewHolder> {

    public static final int HOME_PAGE = 1;
    public static final int DISPLAY_COURSE = 2;

    private Picasso mPicasso;

    private static final String TAG = "CourseListAdapter";

    private OnItemClickListener mListener;

    public CourseListAdapter(@NonNull FirebaseRecyclerOptions<DisplayCourse> options, String mSender) {
        super(options);
        this.mSender = mSender;
        this.mPicasso = Picasso.get();
    }

    @Override
    protected void onBindViewHolder(@NonNull CourseListViewHolder holder, int position, @NonNull DisplayCourse model) {
        holder.title.setText(model.getCourseTitle());
        mPicasso.load(model.getThumbnailURL()).into(holder.thumbnail);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onCourseItemClick(model);
            }
        });
    }

    private String mSender;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
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
    public int getItemCount() {
        return super.getItemCount();
    }

    public interface OnItemClickListener {
        void onCourseItemClick(DisplayCourse course);
    }

    public static class CourseListViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail;
        TextView title;

        public CourseListViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            title = itemView.findViewById(R.id.courseTitle);
            thumbnail = itemView.findViewById(R.id.courseThumbNailImageView);
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
}
