package com.ofk.bd.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ofk.bd.Fragments.HomeFragment;
import com.ofk.bd.Fragments.MoreFragment;
import com.ofk.bd.Fragments.ProfileFragment;
import com.ofk.bd.Fragments.ProgressFragment;
import com.ofk.bd.SearchActivityFragment.CourseFragment;
import com.ofk.bd.SearchActivityFragment.SearchVideoFragment;

public class SearchAdapter extends FragmentStateAdapter {

    public SearchAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new CourseFragment();
            case 1:
                return new SearchVideoFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
