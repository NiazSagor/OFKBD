package com.ofk.bd;

import android.app.Dialog;
import android.content.Intent;
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

import com.ofk.bd.DisplayCourseActivityAdapter.CourseListAdapter;
import com.ofk.bd.Fragments.BottomDialog;
import com.ofk.bd.Model.DisplayCourse;
import com.ofk.bd.HelperClass.MyApp;
import com.ofk.bd.Model.UserProgress;
import com.ofk.bd.Utility.AlertDialogUtility;
import com.ofk.bd.ViewModel.DisplayCourseActivityViewModel;
import com.ofk.bd.databinding.ActivityDisplayCourseBinding;

import java.util.List;

public class DisplayCourseActivity extends AppCompatActivity implements BottomDialog.BottomSheetListener {

    private static final String TAG = "DisplayCourseActivity";

    private ActivityDisplayCourseBinding binding;

    private DisplayCourseActivityViewModel viewModel;

    private DisplayCourse selectedCourse;

    private final AlertDialogUtility alertDialogUtility = new AlertDialogUtility();

    private final BottomDialog bottomSheet = new BottomDialog();

    private final Handler handler = new Handler();

    private LiveData<List<String>> enrolledCourseLiveData = new LiveData<List<String>>() {
        @Override
        public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super List<String>> observer) {
            super.observe(owner, observer);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDisplayCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (viewModel == null) {
            viewModel = ViewModelProviders.of(DisplayCourseActivity.this).get(DisplayCourseActivityViewModel.class);
        }


        if (!viewModel.getCoursesLiveData(getIntent().getStringExtra("section_name") + " Section").hasObservers()) {
            viewModel.getCoursesLiveData(getIntent().getStringExtra("section_name") + " Section").observe(this, allCoursesObserver);
        }

        binding.sectionHeadline.setText(new StringBuilder()
                .append(getIntent().getStringExtra("section_name_bangla"))
                .append(" কোর্স"));
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

        if (!MyApp.IS_CONNECTED) {
            alertDialogUtility.showAlertDialog(this, "noConnection");
            return;
        }

        binding.courseRecyclerView.setLayoutManager(new GridLayoutManager(DisplayCourseActivity.this, 2));

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");

        if (enrolledCourseLiveData.hasObservers()) {
            enrolledCourseLiveData.removeObservers(this);
        }
    }

    private void proceedNormally(String sectionName, String courseName, String courseNameEnglish) {

        Intent intent = new Intent(DisplayCourseActivity.this, CourseActivity.class);
        intent.putExtra("section_name", sectionName);
        intent.putExtra("course_name", courseName);
        intent.putExtra("course_name_english", courseNameEnglish);
        intent.putExtra("from", "display");
        intent.putExtra("isNewCourse", false);
        startActivity(intent);
    }

    @Override
    public void onButtonClicked(int isInterested) {
        if (isInterested == 0) {

            String sectionName = getIntent().getStringExtra("section_name");
            String sectionNameBangla = getIntent().getStringExtra("section_name_bangla");

            String courseName = selectedCourse.getCourseTitle();
            String courseNameEnglish = selectedCourse.getCourseTitleEnglish();
            String thumbNailURL = selectedCourse.getThumbnailURL();


            viewModel.insert(new UserProgress(courseName, courseNameEnglish, thumbNailURL, false, sectionName, sectionNameBangla, 0, 0));


            Intent intent = new Intent(DisplayCourseActivity.this, CourseActivity.class);
            intent.putExtra("section_name", sectionName);
            intent.putExtra("course_name", courseName);
            intent.putExtra("course_name_english", courseNameEnglish);
            intent.putExtra("from", "display");
            intent.putExtra("isNewCourse", true);

            bottomSheet.dismiss();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(intent);
                }
            }, 700);

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

    private final Observer<List<DisplayCourse>> allCoursesObserver = new Observer<List<DisplayCourse>>() {
        @Override
        public void onChanged(List<DisplayCourse> courses) {

            binding.progressBar.setVisibility(View.GONE);

            if (courses == null || courses.size() == 0) {
                Log.d(TAG, "onChanged: " + courses.size());
                alertDialogUtility.showAlertDialog(DisplayCourseActivity.this, "courseNotFound");
                return;
            }

            CourseListAdapter adapter = new CourseListAdapter(courses, "displayCourse");

            binding.courseRecyclerView.setAdapter(adapter);
            binding.courseCount.setText(
                    new StringBuilder()
                            .append(adapter.getItemCount())
                            .append(" টি কোর্স")
            );

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
    };
}