package com.ofk.bd;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ofk.bd.Adapter.MainActivityViewPager;
import com.ofk.bd.ViewModel.MainActivityViewModel;
import com.ofk.bd.databinding.ActivityMainBinding;

public class MainActivity extends FragmentActivity {

    private ActivityMainBinding binding;
    private MenuItem prevMenuItem;
    private MainActivityViewPager adapter;
    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // for every data needed in main activity child fragments
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

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
}