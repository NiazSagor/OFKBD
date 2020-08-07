package com.ofk.bd.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.ofk.bd.HelperClass.Activity;
import com.ofk.bd.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ActivitySliderAdapter extends PagerAdapter {


    private static final String TAG = "MoreCourseSliderAdapter";

    private List<Activity> activityList;

    private Context mContext;

    private String sender;

    private Picasso picasso;

    public ActivitySliderAdapter(Context mContext, List<Activity> courseList, String sender) {
        this.activityList = courseList;
        this.mContext = mContext;
        this.sender = sender;
        picasso = Picasso.get();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return activityList.size();
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

        if (sender.equals("activity")) {
            view = inflater.inflate(R.layout.activity_layout, container, false);
        } else if (sender.equals("our_work")) {
            view = inflater.inflate(R.layout.more_course_layout, container, false);
        }

        TextView title = view.findViewById(R.id.courseTitle);
        TextView subtitle = view.findViewById(R.id.courseSubtitle);

        ImageView activityImageView = view.findViewById(R.id.activityImageView);

        picasso.load(activityList.get(position).getUrl())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(activityImageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        picasso.load(activityList.get(position).getUrl())
                                .error(R.drawable.ofklogo)
                                .into(activityImageView, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError(Exception e) {

                                    }
                                });
                    }
                });

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((CardView) object);
    }
}
