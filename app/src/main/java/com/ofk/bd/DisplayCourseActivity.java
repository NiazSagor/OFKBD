package com.ofk.bd;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.ofk.bd.AsyncTasks.FirebaseQueryRandomCourse;
import com.ofk.bd.AsyncTasks.FirebaseQuerySubSection;
import com.ofk.bd.DisplayCourseActivityAdapter.CourseListAdapter;
import com.ofk.bd.Fragments.BottomDialog;
import com.ofk.bd.HelperClass.Common;
import com.ofk.bd.HelperClass.DisplayCourse;
import com.ofk.bd.HelperClass.SectionVideo;
import com.ofk.bd.HelperClass.UserProgressClass;
import com.ofk.bd.Interface.DisplayCourseLoadCallback;
import com.ofk.bd.Interface.SectionVideoLoadCallback;
import com.ofk.bd.Utility.AlertDialogUtility;
import com.ofk.bd.ViewModel.DisplayCourseActivityViewModel;
import com.ofk.bd.databinding.ActivityDisplayCourseBinding;

import java.util.List;

public class DisplayCourseActivity extends AppCompatActivity implements BottomDialog.BottomSheetListener {

    private static final String TAG = "DisplayCourseActivity";

    private ActivityDisplayCourseBinding binding;

    private DisplayCourseActivityViewModel viewModel;

    private static DisplayCourse selectedCourse;

    private AlertDialogUtility alertDialogUtility = new AlertDialogUtility();

    private final Handler handler = new Handler();

    private LiveData<List<String>> enrolledCourseLiveData = new LiveData<List<String>>() {
        @Override
        public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super List<String>> observer) {
            super.observe(owner, observer);
        }
    };

    private LiveData<DataSnapshot> availableCourseLiveData = new LiveData<DataSnapshot>() {
        @Override
        public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super DataSnapshot> observer) {
            super.observe(owner, observer);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDisplayCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = ViewModelProviders.of(DisplayCourseActivity.this).get(DisplayCourseActivityViewModel.class);

        String headline = getIntent().getStringExtra("section_name_bangla") + " কোর্স";
        binding.sectionHeadline.setText(headline);

        alertDialogUtility.showAlertDialog(this, "start");
    }

    private void setUpViews() {
        String section = getIntent().getStringExtra("section_name");

        switch (section) {
            case "Arts":
                binding.viewTop.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_art_top));
                binding.viewSide.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_art_side));
                break;
            case "Calligraphy":
                binding.viewTop.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_cal_top));
                binding.viewSide.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_cal_side));
                break;
            case "Case Solving":
                binding.viewTop.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_case_top));
                binding.viewSide.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_case_side));
                break;
            case "Craft":
                binding.viewTop.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_craft_top));
                binding.viewSide.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_craft_side));
                break;
            case "Critical Thinking":
                binding.viewTop.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_other_top));
                binding.viewSide.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_other_side));
                break;
            case "Digital Painting":
                binding.viewTop.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_other_top));
                binding.viewSide.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_other_side));
                break;
            case "Guitar":
                binding.viewTop.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_other_top));
                binding.viewSide.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_other_side));
                break;
            case "Programming":
                binding.viewTop.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_pro_top));
                binding.viewSide.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_pro_side));
                break;
            case "Robotics":
                binding.viewTop.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_rob_top));
                binding.viewSide.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_rob_side));
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpViews();

        if (!isConnected()) {
            alertDialogUtility.showAlertDialog(this, "noConnection");
            return;
        }

        binding.courseRecyclerView.setLayoutManager(new GridLayoutManager(DisplayCourseActivity.this, 2));

        getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (availableCourseLiveData.hasObservers()) {
            Log.d(TAG, "onStop: has observer");
            availableCourseLiveData.removeObservers(this);
        }
        if (availableCourseLiveData.hasObservers()) {
            Log.d(TAG, "onStop: has observer");
            availableCourseLiveData.removeObservers(this);
        }
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");

        if (enrolledCourseLiveData.hasObservers()) {
            enrolledCourseLiveData.removeObservers(this);
        }
        if (availableCourseLiveData.hasObservers()) {
            availableCourseLiveData.removeObservers(this);
        }
    }


    private void getData() {

        String node = getIntent().getStringExtra("section_name") + " Section";

        new FirebaseQueryRandomCourse(new DisplayCourseLoadCallback() {
            @Override
            public void onLoadCallback(List<DisplayCourse> courses) {

                CourseListAdapter adapter = new CourseListAdapter(courses, "displayCourse");

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        alertDialogUtility.dismissAlertDialog();
                        binding.courseRecyclerView.setAdapter(adapter);
                        String count = adapter.getItemCount() + " টি কোর্স";
                        binding.courseCount.setText(count);
                    }
                }, 1500);

                adapter.setOnItemClickListener(new CourseListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View view) {


                        selectedCourse = courses.get(position);


                        String sectionName = getIntent().getStringExtra("section_name");
                        String courseName = courses.get(position).getCourseTitle();
                        String courseNameEnglish = courses.get(position).getCourseTitleEnglish();

                        enrolledCourseLiveData = viewModel.getCourseEnrolled();

                        if (!enrolledCourseLiveData.hasObservers()) {
                            enrolledCourseLiveData.observe(DisplayCourseActivity.this, new Observer<List<String>>() {
                                @Override
                                public void onChanged(List<String> strings) {
                                    if (!strings.contains(courseNameEnglish)) {
                                        // if not in db from before
                                        Log.d(TAG, "onChanged: course is not in db");
                                        enrolledCourseLiveData.removeObservers(DisplayCourseActivity.this);

                                        BottomDialog bottomSheet = new BottomDialog();
                                        bottomSheet.show(getSupportFragmentManager(), "");
                                    } else {
                                        // if is in db from before
                                        Log.d(TAG, "onChanged: course is in db");
                                        enrolledCourseLiveData.removeObservers(DisplayCourseActivity.this);
                                        proceedNormally(sectionName, courseName, courseNameEnglish);
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }, node).execute();
    }

    private void proceedNormally(String sectionName, String courseName, String courseNameEnglish) {

        alertDialogUtility.showAlertDialog(this, "start");

        Intent intent = new Intent(DisplayCourseActivity.this, CourseActivity.class);
        intent.putExtra("section_name", sectionName);
        intent.putExtra("course_name", courseName);
        intent.putExtra("course_name_english", courseNameEnglish);
        intent.putExtra("from", "display");

        new FirebaseQuerySubSection(new SectionVideoLoadCallback() {
            @Override
            public void onSectionVideoLoadCallback(List<SectionVideo> sectionVideoList, int totalVideos) {

                Common.sectionVideoList = sectionVideoList;

                Log.d(TAG, "onSectionVideoLoadCallback: " + totalVideos);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        alertDialogUtility.dismissAlertDialog();
                        startActivity(intent);
                    }
                }, 1000);
            }
        }, sectionName + " Section", courseNameEnglish).execute();
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onButtonClicked(int isInterested) {
        if (isInterested == 0) {

            alertDialogUtility.showAlertDialog(this, "start");

            String sectionName = getIntent().getStringExtra("section_name");
            String sectionNameBangla = getIntent().getStringExtra("section_name_bangla");

            String courseName = selectedCourse.getCourseTitle();
            String courseNameEnglish = selectedCourse.getCourseTitleEnglish();
            String thumbNailURL = selectedCourse.getThumbnailURL();


            viewModel.insert(new UserProgressClass(sectionName, sectionNameBangla, courseName, courseNameEnglish, false, thumbNailURL, 0, 0, 0));


            Intent intent = new Intent(DisplayCourseActivity.this, CourseActivity.class);
            intent.putExtra("section_name", sectionName);
            intent.putExtra("course_name", courseName);
            intent.putExtra("course_name_english", courseNameEnglish);
            intent.putExtra("from", "display");


            new FirebaseQuerySubSection(new SectionVideoLoadCallback() {
                @Override
                public void onSectionVideoLoadCallback(List<SectionVideo> sectionVideoList, int totalVideos) {

                    Common.sectionVideoList = sectionVideoList;

                    Log.d(TAG, "onSectionVideoLoadCallback: " + totalVideos);

                    viewModel.updateTotalVideoCourse(courseNameEnglish, totalVideos);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            alertDialogUtility.dismissAlertDialog();
                            startActivity(intent);
                        }
                    }, 1000);
                }
            }, sectionName + " Section", courseNameEnglish).execute();

        } else if (isInterested == 1) {

            // if user wants to buy

        } else {
            showDialog();
        }
    }

    public void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.age_layout);
        dialog.show();
    }
}