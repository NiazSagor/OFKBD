package com.ofk.bd;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;

import com.ofk.bd.InfoActivityAdapter.InfoActivityViewPagerAdapter;
import com.ofk.bd.ViewModel.InfoActivityViewModel;
import com.ofk.bd.databinding.ActivityInfoBinding;

public class InfoActivity extends AppCompatActivity {

    private ActivityInfoBinding binding;

    private ViewPager2 viewPager2;

    private InfoActivityViewModel activityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activityViewModel = ViewModelProviders.of(this).get(InfoActivityViewModel.class);

        binding.viewpager.setUserInputEnabled(false);
        binding.viewpager.setAdapter(new InfoActivityViewPagerAdapter(this));
        binding.viewpager.setOffscreenPageLimit(2);
    }
}