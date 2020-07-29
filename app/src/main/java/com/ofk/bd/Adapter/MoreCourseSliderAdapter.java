package com.ofk.bd.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.ofk.bd.HelperClass.Course;
import com.ofk.bd.R;

import java.util.List;

public class MoreCourseSliderAdapter extends PagerAdapter {


    private static final String TAG = "MoreCourseSliderAdapter";

    private List<Course> courseList;
    private Context mContext;

    private String sender;

    public MoreCourseSliderAdapter(Context mContext, List<Course> courseList, String sender) {
        this.courseList = courseList;
        this.mContext = mContext;
        this.sender = sender;
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

        View view = null;

        if(sender.equals("activity")){
            view = inflater.inflate(R.layout.activity_layout, container, false);
        }else if(sender.equals("our_work")){
            view = inflater.inflate(R.layout.more_course_layout, container, false);
        }

        TextView title = view.findViewById(R.id.courseTitle);
        TextView subtitle = view.findViewById(R.id.courseSubtitle);


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
