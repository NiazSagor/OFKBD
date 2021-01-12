package com.ofk.bd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;

import com.developer.kalert.KAlertDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.ofk.bd.Adapter.MainActivityViewPager;
import com.ofk.bd.Fragments.ProfileBottomSheet;
import com.ofk.bd.HelperClass.MyApp;
import com.ofk.bd.HelperClass.SectionCourseNameTuple;
import com.ofk.bd.HelperClass.ServiceResultReceiver;
import com.ofk.bd.JobIntentService.UpdateVideoCountService;
import com.ofk.bd.Utility.AlertDialogUtility;
import com.ofk.bd.ViewModel.MainActivityViewModel;
import com.ofk.bd.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import static com.ofk.bd.JobIntentService.UpdateVideoCountService.RECEIVER;

public class MainActivity extends AppCompatActivity implements ServiceResultReceiver.Receiver, ProfileBottomSheet.BottomSheetListener {

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

        if (!MyApp.IS_CONNECTED) {
            new AlertDialogUtility().showAlertDialog(this, "noConnection");
            return;
        }

        if (remoteConfig.getBoolean(IS_COURSE_UPDATED)) {
            // if true then fetch the new count for videos of courses
            startService();
            Log.d(TAG, "onStart: " + remoteConfig.getBoolean(IS_COURSE_UPDATED));
        }

        viewModel.isFirstCourseCompleted().observe(this, isFirstCourseCompletedObserver);
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

    private void startService() {
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

                for (int i = 0; i < (courseName != null ? courseName.size() : 0); i++) {
                    String course = courseName.get(i);
                    int count = videoCount.get(i);
                    viewModel.updateTotalVideoCourse(course, count);
                }
            }
        }
    }

    @Override
    public void onButtonClicked(String text) {

        if (text.length() == 1 || text.length() == 2) {

            //class
            viewModel.updateUserInfo("Class " + text, "class");
        } else if (text.length() == 4 || text.length() == 6) {

            // gender
            viewModel.updateUserInfo(text, "gender");
        } else {
            //dob
            viewModel.updateUserInfo(text, "dob");
        }
    }

    private final Observer<Boolean> isFirstCourseCompletedObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean aBoolean) {
            if (aBoolean != null && aBoolean) {
                ReviewManager manager = ReviewManagerFactory.create(MainActivity.this);
                Task<ReviewInfo> request = manager.requestReviewFlow();
                request.addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
                    @Override
                    public void onComplete(@NonNull Task<ReviewInfo> task) {
                        if (task.isSuccessful()) {

                            ReviewInfo reviewInfo = task.getResult();

                            Task<Void> flow = manager.launchReviewFlow(MainActivity.this, reviewInfo);

                            flow.addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG, "onComplete: ");
                                }
                            });
                        }
                    }
                });
            } else {
                Log.d(TAG, "onChanged: course is not completed");
            }
        }
    };
}