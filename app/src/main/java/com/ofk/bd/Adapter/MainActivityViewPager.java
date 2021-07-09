package com.ofk.bd.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ofk.bd.Fragments.HomeFragment;
import com.ofk.bd.Fragments.MoreFragment;
import com.ofk.bd.Fragments.ProfileFragment;
import com.ofk.bd.Fragments.ProgressFragment;

public class MainActivityViewPager extends FragmentStateAdapter {


    public MainActivityViewPager(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new ProgressFragment();
            case 2:
                return new MoreFragment();
            case 3:
                return new ProfileFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
