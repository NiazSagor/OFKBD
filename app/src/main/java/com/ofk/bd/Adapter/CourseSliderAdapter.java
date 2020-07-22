package com.ofk.bd.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.ofk.bd.HelperClass.Course;
import com.ofk.bd.R;

import java.util.ArrayList;
import java.util.List;

public class CourseSliderAdapter extends PagerAdapter {

    private static final String TAG = "CourseSliderAdapter";

    private Context mContext;

    private List<Course> courseList;

    private List<List<Course>> finalList = new ArrayList<>();

    public CourseSliderAdapter(Context mContext, List<Course> list) {
        this.mContext = mContext;
        this.courseList = list;
        calculate();
    }

    @Override
    public int getCount() {
        if (courseList.size() % 6 == 0 || courseList.size() < 6) {
            return 1;
        } else {
            return (courseList.size() / 6) + 1;
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (RelativeLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.course_slider_layout, container, false);

        RecyclerView courseRecyclerView = view.findViewById(R.id.courseRecyclerView);
        courseRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));

        if (courseList.size() >= 7 && courseList.size() <= 12) {
            courseRecyclerView.setAdapter(new CourseSliderListAdapter(finalList.get(position), "viewpager"));
        } else if (courseList.size() == 6 || courseList.size() < 6) {
            courseRecyclerView.setAdapter(new CourseSliderListAdapter(courseList, "viewpager"));
        }


        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }

    public void calculate() {
        if (courseList.size() == 6 || courseList.size() < 6) {

        } else if (courseList.size() >= 7 && courseList.size() <= 12) {
            List<Course> head = courseList.subList(0, 6);
            List<Course> tail = courseList.subList(6, courseList.size());
            finalList.add(head);
            finalList.add(tail);
        }
    }
}
