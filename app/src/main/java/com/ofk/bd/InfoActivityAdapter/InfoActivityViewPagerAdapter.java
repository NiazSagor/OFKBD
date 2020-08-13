package com.ofk.bd.InfoActivityAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ofk.bd.InfoActivityFragment.InputNumberFragment;
import com.ofk.bd.InfoActivityFragment.InputOtpFragment;
import com.ofk.bd.InfoActivityFragment.UserInfoFragment;

public class InfoActivityViewPagerAdapter extends FragmentStateAdapter {

    public InfoActivityViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new InputNumberFragment();
            case 1:
                return new InputOtpFragment();
            case 2:
                return new UserInfoFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
