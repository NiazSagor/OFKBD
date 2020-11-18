package com.ofk.bd;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;

import com.developer.kalert.KAlertDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.ofk.bd.Adapter.MainActivityViewPager;
import com.ofk.bd.HelperClass.SectionCourseNameTuple;
import com.ofk.bd.HelperClass.ServiceResultReceiver;
import com.ofk.bd.JobIntentService.UpdateVideoCountService;
import com.ofk.bd.ViewModel.MainActivityViewModel;
import com.ofk.bd.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import static com.ofk.bd.JobIntentService.UpdateVideoCountService.RECEIVER;

public class MainActivity extends AppCompatActivity implements ServiceResultReceiver.Receiver {

    // our result receiver from job intent
    private ServiceResultReceiver mReceiver;
    // for result receiver
    private static final int SHOW_RESULT = 123;

    public static final String IS_COURSE_UPDATED = "is_course_updated";

    private static final String TAG = "MainActivity";

    private ActivityMainBinding binding;
    private MenuItem prevMenuItem;
    private MainActivityViewModel viewModel;
    private KAlertDialog pDialog;

    private FirebaseRemoteConfig remoteConfig;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // for every data needed in main activity child fragments
        remoteConfig = FirebaseRemoteConfig.getInstance();

        if (viewModel == null) {
            viewModel = ViewModelProviders.of(MainActivity.this).get(MainActivityViewModel.class);
        }

        sharedPreferences = getSharedPreferences("intent", MODE_PRIVATE);
        // initialize receiver
        mReceiver = new ServiceResultReceiver(new Handler());
        mReceiver.setReceiver(this);

        binding.bottomNavigation.setOnNavigationItemSelectedListener(mNavListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mNavListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            binding.viewpager.setCurrentItem(0, true);
                            break;

                        case R.id.nav_progress:
                            binding.viewpager.setCurrentItem(1, true);
                            break;

                        case R.id.nav_more:
                            binding.viewpager.setCurrentItem(2, true);
                            break;

                        case R.id.nav_profile:
                            binding.viewpager.setCurrentItem(3, true);
                            break;
                    }
                    return false;
                }
            };

    @Override
    protected void onStart() {
        super.onStart();
        setupViewPager();

        if (!isConnected()) {
            showAlertDialog("done");
            return;
        }

        if (remoteConfig.getBoolean(IS_COURSE_UPDATED)) {
            //true
            setupService();
            Log.d(TAG, "onStart: " + remoteConfig.getBoolean(IS_COURSE_UPDATED));
        }
    }

    public void setupViewPager() {
        MainActivityViewPager adapter = new MainActivityViewPager(getSupportFragmentManager(), getLifecycle());

        binding.viewpager.setUserInputEnabled(false);

        binding.viewpager.setOffscreenPageLimit(3);

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

    private void setupService() {
        ArrayList<String> sectionNameList = new ArrayList<>();
        ArrayList<String> courseNameList = new ArrayList<>();

        viewModel.getEnrolledCoursesFromOfflineDb().observe(this, new Observer<List<SectionCourseNameTuple>>() {
            @Override
            public void onChanged(List<SectionCourseNameTuple> sectionCourseNameTuples) {
                //enrolledCourse = sectionCourseNameTuples;

                for (SectionCourseNameTuple object : sectionCourseNameTuples) {
                    sectionNameList.add(object.getSectionName());
                    courseNameList.add(object.getCourseNameEnglish());
                }

                Intent serviceIntent = new Intent(MainActivity.this, UpdateVideoCountService.class);
                serviceIntent.putExtra(RECEIVER, mReceiver);
                serviceIntent.putStringArrayListExtra("sectionName", sectionNameList);
                serviceIntent.putStringArrayListExtra("courseName", courseNameList);

                UpdateVideoCountService.enqueueWork(MainActivity.this, serviceIntent);

                viewModel.getEnrolledCoursesFromOfflineDb().removeObservers(MainActivity.this);
            }
        });
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
        if (resultCode == SHOW_RESULT) {
            if (resultData != null) {

                ArrayList<String> courseName = resultData.getStringArrayList("courseName");
                ArrayList<Integer> videoCount = resultData.getIntegerArrayList("count");

                Log.d(TAG, "onReceiveResult: " + courseName.toString());
                Log.d(TAG, "onReceiveResult: " + videoCount.toString());

                for (int i = 0; i < courseName.size(); i++) {
                    String course = courseName.get(i);
                    int count = videoCount.get(i);
                    viewModel.updateTotalVideoCourse(course, count);
                }
            }
        }
    }

    private void showAlertDialog(String command) {
        if ("done".equals(command)) {
            pDialog = new KAlertDialog(this, KAlertDialog.ERROR_TYPE);
            pDialog.setTitleText("ইন্টারনেট সংযোগ নেই")
                    .setConfirmText("OK")
                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                        @Override
                        public void onClick(KAlertDialog kAlertDialog) {
                            pDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        }
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}