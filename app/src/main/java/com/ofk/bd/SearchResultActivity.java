package com.ofk.bd;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.ofk.bd.Adapter.SearchAdapter;
import com.ofk.bd.ViewModel.FirebaseSearchViewModel;
import com.ofk.bd.databinding.ActivitySearchResultBinding;

public class SearchResultActivity extends FragmentActivity {

    private static final String TAG = "SearchResultActivity";

    private FirebaseSearchViewModel viewModel;
    private ActivitySearchResultBinding binding;
    private String count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();

        viewModel = ViewModelProviders.of(this).get(FirebaseSearchViewModel.class);
        viewModel.getMutableLiveData().setValue(intent.getStringExtra("searchQuery"));

        setUpTabLayoutWithViewpager();
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.getCourseResult();
        viewModel.getVideoResult();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setSearchResult();
            }
        }, 1500);
    }

    private void setSearchResult() {

        viewModel.getFoundVideoCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.d(TAG, "onChanged: " + integer);
                String searchResultVideo = integer + " ভিডিও";
                binding.videoCountTextView.setText(searchResultVideo);
            }
        });

        viewModel.getFoundCourseCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.d(TAG, "onChanged: " + integer);
                String searchResultCourse = integer + " কোর্স";
                binding.courseCountTextView.setText(searchResultCourse);
            }
        });
    }

    private void setUpTabLayoutWithViewpager() {

        ViewPager2 viewPager2 = binding.pager;

        TabLayout tabLayout = binding.tabLayout;

        tabLayout.addTab(tabLayout.newTab().setText("Course"));
        tabLayout.addTab(tabLayout.newTab().setText("Video"));

        SearchAdapter adapter = new SearchAdapter(SearchResultActivity.this);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    viewPager2.setCurrentItem(0);
                } else if (tab.getPosition() == 1) {
                    viewPager2.setCurrentItem(1);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        viewPager2.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewModel.getMutableLiveData().setValue(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.getMutableLiveData().setValue(null);
    }
}