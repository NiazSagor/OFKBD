package com.ofk.bd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ofk.bd.DisplayCourseActivityAdapter.CourseListAdapter;
import com.ofk.bd.HelperClass.DisplayCourse;
import com.ofk.bd.databinding.ActivityDisplayCourseBinding;

import java.util.ArrayList;
import java.util.List;

public class DisplayCourseActivity extends AppCompatActivity {

    private static final String TAG = "DisplayCourseActivity";

    private ActivityDisplayCourseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDisplayCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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

        List<DisplayCourse> courses = new ArrayList<>();

        courses.add(new DisplayCourse("আর্টের হাতেখড়ি", ""));
        courses.add(new DisplayCourse("সহজে সি প্রোগ্রামিং", ""));
        courses.add(new DisplayCourse("রোবটিক্সের হাতেখড়ি ( আর্ডুইনো )", ""));
        courses.add(new DisplayCourse("হাত দিয়ে যায় আঁকা ( Hand Shape Art )", ""));

        RecyclerView courseRecyclerView = binding.courseRecyclerView;
        courseRecyclerView.setLayoutManager(new GridLayoutManager(DisplayCourseActivity.this, 2));

        CourseListAdapter adapter = new CourseListAdapter(courses, "displayCourse");
        courseRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new CourseListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                startActivity(new Intent(DisplayCourseActivity.this, CourseActivity.class).putExtra("course_name", "আর্টের হাতেখড়ি"));
            }
        });
    }
}