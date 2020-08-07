package com.ofk.bd;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ofk.bd.Adapter.MainActivityViewPager;
import com.ofk.bd.HelperClass.SectionCourseTuple;
import com.ofk.bd.HelperClass.ServiceResultReceiver;
import com.ofk.bd.ViewModel.MainActivityViewModel;
import com.ofk.bd.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends FragmentActivity implements ServiceResultReceiver.Receiver {

    // our result receiver from job intent
    private ServiceResultReceiver mReceiver;
    // for result receiver
    private static final int SHOW_RESULT = 123;

    private static final String TAG = "MainActivity";

    private ActivityMainBinding binding;
    private MenuItem prevMenuItem;
    private MainActivityViewPager adapter;
    private MainActivityViewModel viewModel;

    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // for every data needed in main activity child fragments
        viewModel = ViewModelProviders.of(MainActivity.this).get(MainActivityViewModel.class);

        sharedPreferences = getSharedPreferences("intent", MODE_PRIVATE);
        // initialize receiver
        mReceiver = new ServiceResultReceiver(new Handler());
        mReceiver.setReceiver(this);

        setupViewPager();

        binding.bottomNavigation.setOnNavigationItemSelectedListener(mNavListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mNavListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            binding.viewpager.setCurrentItem(0);
                            break;

                        case R.id.nav_progress:
                            binding.viewpager.setCurrentItem(1);
                            break;

                        case R.id.nav_more:
                            binding.viewpager.setCurrentItem(2);
                            break;

                        case R.id.nav_profile:
                            binding.viewpager.setCurrentItem(3);
                            break;
                    }
                    return false;
                }
            };

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void setupViewPager() {
        adapter = new MainActivityViewPager(this);

        binding.viewpager.setUserInputEnabled(false);

        binding.viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    binding.bottomNavigation.getMenu().getItem(0).setChecked(false);
                }
                binding.bottomNavigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = binding.bottomNavigation.getMenu().getItem(position);
            }
        });

        binding.viewpager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (binding.viewpager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            binding.viewpager.setCurrentItem(0);
        }
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case SHOW_RESULT:
                if (resultData != null) {
                    int count = resultData.getInt("updatedCount");
                    String course = resultData.getString("courseName");
                    //viewModel.updateVideoCount(count, course);
                    //Log.d(TAG, "onReceiveResult: " + resultData.getInt("updatedCount"));
                }
        }
    }

    private void calculateUserProgress(){
        viewModel.getCombinedList().observe(this, new Observer<List<SectionCourseTuple>>() {
            @Override
            public void onChanged(List<SectionCourseTuple> sectionCourseTuples) {
                for(SectionCourseTuple courseTuple : sectionCourseTuples){
                    if(courseTuple.getVideoWatched() == courseTuple.getTotalVideos()){
                        int currentCompletedCourse = viewModel.getCourseCompletedInTotal();
                        currentCompletedCourse++;
                        viewModel.updateUserCourseTotal(currentCompletedCourse);
                    }
                }
            }
        });
    }
}