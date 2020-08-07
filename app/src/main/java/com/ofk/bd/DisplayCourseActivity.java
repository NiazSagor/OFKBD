package com.ofk.bd;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.ofk.bd.DisplayCourseActivityAdapter.CourseListAdapter;
import com.ofk.bd.HelperClass.DisplayCourse;
import com.ofk.bd.HelperClass.UserProgressClass;
import com.ofk.bd.ViewModel.DisplayCourseActivityViewModel;
import com.ofk.bd.databinding.ActivityDisplayCourseBinding;

import java.util.ArrayList;
import java.util.List;

public class DisplayCourseActivity extends AppCompatActivity {

    private static final String TAG = "DisplayCourseActivity";

    private ActivityDisplayCourseBinding binding;

    private DisplayCourseActivityViewModel viewModel;

    private List<DisplayCourse> courses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDisplayCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = ViewModelProviders.of(this).get(DisplayCourseActivityViewModel.class);
        viewModel.getCoursesFromDB(getIntent().getStringExtra("section_name"));

        String headline = getIntent().getStringExtra("section_name") + " Courses";
        binding.sectionHeadline.setText(headline);

        setupCourseRecyclerView();

        setUpToolbar();
    }

    private void setUpToolbar() {
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_ios_24));
    }


    private void setupCourseRecyclerView() {
        courses = new ArrayList<>();
        RecyclerView courseRecyclerView = binding.courseRecyclerView;
        courseRecyclerView.setLayoutManager(new GridLayoutManager(DisplayCourseActivity.this, 2));

        viewModel.getAvailableCourses().observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                DisplayCourse course = dataSnapshot.getValue(DisplayCourse.class);
                courses.add(course);

                CourseListAdapter adapter = new CourseListAdapter(courses, "displayCourse");
                courseRecyclerView.setAdapter(adapter);

                String count = adapter.getItemCount() + " Courses";

                binding.courseCount.setText(count);

                adapter.setOnItemClickListener(new CourseListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View view) {

                        //viewModel.getEnrolledCourses("01213123");

                        String sectionName = getIntent().getStringExtra("section_name");
                        String courseName = courses.get(position).getCourseTitle();
                        String courseNameEnglish = courses.get(position).getCourseTitleEnglish();
                        String thumbNailURL = courses.get(position).getThumbnailURL();


                        viewModel.getCourseEnrolled().observe(DisplayCourseActivity.this, new Observer<List<String>>() {
                            @Override
                            public void onChanged(List<String> strings) {
                                if (!strings.contains(courseNameEnglish)) {
                                    // if not in db from before
                                    Log.d(TAG, "onChanged: course is not in db");
                                    Log.d(TAG, "onChanged: " + thumbNailURL);
                                    showAlertDialog(sectionName, courseName, courseNameEnglish, thumbNailURL);
                                } else {
                                    // if is in db from before
                                    Log.d(TAG, "onChanged: course is in db");
                                    Log.d(TAG, "onChanged: " + courseNameEnglish);
                                    Log.d(TAG, "onChanged: " + sectionName);
                                    Log.d(TAG, "onChanged: " + thumbNailURL);
                                    proceedNormally(sectionName, courseName, courseNameEnglish);
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    private void showAlertDialog(String sectionName, String courseName, String courseNameEnglish, String thumbnailURL) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Are you sure to enroll the course?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = new Intent(DisplayCourseActivity.this, CourseActivity.class);
                        intent.putExtra("section_name", sectionName);
                        intent.putExtra("course_name", courseName);
                        intent.putExtra("course_name_english", courseNameEnglish);

                        Log.d(TAG, "onClick: course is added");
                        viewModel.insert(new UserProgressClass(sectionName, courseName, courseNameEnglish, thumbnailURL, 9, 0));

                        startActivity(intent);

                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void proceedNormally(String sectionName, String courseName, String courseNameEnglish) {
        Intent intent = new Intent(DisplayCourseActivity.this, CourseActivity.class);
        intent.putExtra("section_name", sectionName);
        intent.putExtra("course_name", courseName);
        intent.putExtra("course_name_english", courseNameEnglish);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
        courses.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        courses.clear();
    }
}