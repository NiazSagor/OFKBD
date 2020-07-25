package com.ofk.bd.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.viewpager.widget.PagerAdapter;

import com.ofk.bd.HelperClass.Course;
import com.ofk.bd.R;

import java.util.List;

public class MoreCourseSliderAdapter extends PagerAdapter {


    private static final String TAG = "MoreCourseSliderAdapter";

    private List<Course> courseList;
    private Context mContext;

    public MoreCourseSliderAdapter(Context mContext, List<Course> courseList) {
        this.courseList = courseList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return courseList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (CardView) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.more_course_layout, container, false);

        TextView title = view.findViewById(R.id.courseTitle);
        TextView subtitle = view.findViewById(R.id.courseSubtitle);

        CardView exploreButton = view.findViewById(R.id.exploreCardView);

        exploreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "" + courseList.get(position).getCourseTitle(), Toast.LENGTH_SHORT).show();
            }
        });


        title.setText(courseList.get(position).getCourseTitle());
        subtitle.setText(courseList.get(position).getCourseSubtitle());

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((CardView) object);
    }
}
