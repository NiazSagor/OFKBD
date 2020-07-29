package com.ofk.bd.CourseActivityAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ofk.bd.CourseActivityFragments.QuizFragment;
import com.ofk.bd.CourseActivityFragments.ResourceFragment;
import com.ofk.bd.CourseActivityFragments.VideoFragment;

public class CourseViewPager extends FragmentPagerAdapter {


    public CourseViewPager(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new VideoFragment();
            case 1:
                return new QuizFragment();
            case 2:
                return new ResourceFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
