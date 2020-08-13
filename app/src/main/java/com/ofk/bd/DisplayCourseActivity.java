package com.ofk.bd;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
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

    private Observer<List<String>> listObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDisplayCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = ViewModelProviders.of(DisplayCourseActivity.this).get(DisplayCourseActivityViewModel.class);
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

        availableCourseLiveData = viewModel.getAvailableCourses();

        if (!availableCourseLiveData.hasObservers()) {
            availableCourseLiveData.observe(this, new Observer<DataSnapshot>() {
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

                            String sectionName = getIntent().getStringExtra("section_name");
                            String courseName = courses.get(position).getCourseTitle();
                            String courseNameEnglish = courses.get(position).getCourseTitleEnglish();
                            String thumbNailURL = courses.get(position).getThumbnailURL();

                            enrolledCourseLiveData = viewModel.getCourseEnrolled();

                            if (!enrolledCourseLiveData.hasObservers()) {
                                enrolledCourseLiveData.observe(DisplayCourseActivity.this, new Observer<List<String>>() {
                                    @Override
                                    public void onChanged(List<String> strings) {
                                        if (!strings.contains(courseNameEnglish)) {
                                            // if not in db from before
                                            Log.d(TAG, "onChanged: course is not in db");
                                            enrolledCourseLiveData.removeObservers(DisplayCourseActivity.this);
                                            showAlertDialog(sectionName, courseName, courseNameEnglish, thumbNailURL);
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
            });
        }
    }

    private void showAlertDialog(String sectionName, String courseName, String courseNameEnglish, String thumbnailURL) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("কোর্সটি শুরু করতে চাও?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = new Intent(DisplayCourseActivity.this, CourseActivity.class);
                        intent.putExtra("section_name", sectionName);
                        intent.putExtra("course_name", courseName);
                        intent.putExtra("course_name_english", courseNameEnglish);
                        intent.putExtra("from", "display");

                        viewModel.insert(new UserProgressClass(sectionName, courseName, courseNameEnglish, thumbnailURL, 9, 0));

                        startActivity(intent);

                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(DisplayCourseActivity.this, DisplayCourseActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
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
        intent.putExtra("from", "display");
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
        courses.clear();
        if (enrolledCourseLiveData.hasObservers()) {
            enrolledCourseLiveData.removeObservers(this);
        }
        if (availableCourseLiveData.hasObservers()) {
            availableCourseLiveData.removeObservers(this);
        }
    }
}