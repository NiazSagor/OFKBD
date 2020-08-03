package com.ofk.bd.CourseActivityAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ofk.bd.CourseActivityFragments.QuizFragment;
import com.ofk.bd.CourseActivityFragments.ResourceFragment;
import com.ofk.bd.CourseActivityFragments.VideoFragment;

public class CourseViewPager extends FragmentStateAdapter {

    public CourseViewPager(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
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
    public int getItemCount() {
        return 3;
    }
}
