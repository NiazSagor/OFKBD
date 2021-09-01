package com.ofk.bd;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.ofk.bd.DisplayCourseActivityAdapter.CourseListAdapter;
import com.ofk.bd.HelperClass.MyApp;
import com.ofk.bd.Model.DisplayCourse;
import com.ofk.bd.Model.UserProgress;
import com.ofk.bd.Utility.AlertDialogUtility;
import com.ofk.bd.Utility.DatabaseUtility;
import com.ofk.bd.ViewModel.DisplayCourseActivityViewModel;
import com.ofk.bd.databinding.ActivityDisplayCourseBinding;

import java.util.List;

public class DisplayCourseActivity extends AppCompatActivity implements CourseListAdapter.OnItemClickListener {

    private static final String TAG = "DisplayCourseActivity";

    private ActivityDisplayCourseBinding binding;

    private DisplayCourseActivityViewModel viewModel;

    private DisplayCourse selectedCourse;

    private final AlertDialogUtility alertDialogUtility = new AlertDialogUtility();

    private BottomSheetBehavior bottomSheetBehavior;

    private LinearLayout bottomSheetCo;

    private CourseListAdapter adapter;

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

        adapter = new CourseListAdapter(DatabaseUtility.getRecommendedCourseQuery(getIntent().getStringExtra("section_name") + " Section"), "displayCourse");
        binding.courseRecyclerView.setLayoutManager(new GridLayoutManager(DisplayCourseActivity.this, 2));
        binding.courseRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        binding.sectionHeadline.setText(new StringBuilder()
                .append(getIntent().getStringExtra("section_name_bangla"))
                .append(" কোর্স"));


        bottomSheetCo = findViewById(R.id.bottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetCo);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
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

        Button notInterestedButton = findViewById(R.id.notInterestedButton);
        notInterestedButton.setOnClickListener(notInterestedButtonClickListener);

        Button interestedButton = findViewById(R.id.interestedButton);
        interestedButton.setOnClickListener(interestedButtonClickListener);

        TextView dialogTop = findViewById(R.id.dialogTextViewTop);
        dialogTop.setOnClickListener(dialogTopClickListener);

        ImageView closeBottomSheet = findViewById(R.id.closeBottomSheet);
        closeBottomSheet.setOnClickListener(closeBottomSheetClickListener);

        if (!MyApp.IS_CONNECTED) {
            alertDialogUtility.showAlertDialog(this, "noConnection");
            return;
        }

        adapter.startListening();

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
            }
        });

        binding.courseCount.setText(
                new StringBuilder()
                        .append(adapter.getSnapshots().size())
                        .append(" টি কোর্স")
        );

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
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

    private final View.OnClickListener closeBottomSheetClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            LinearLayout ofkCertification = findViewById(R.id.ofkCertification);
            ofkCertification.setVisibility(View.GONE);
        }
    };
    private final View.OnClickListener dialogTopClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            LinearLayout ofkCertification = findViewById(R.id.ofkCertification);
            ofkCertification.setVisibility(View.VISIBLE);
        }
    };
    private final View.OnClickListener interestedButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(DisplayCourseActivity.this, "Thank you for your interest", Toast.LENGTH_SHORT).show();
        }
    };

    private final View.OnClickListener notInterestedButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
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

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(intent);
                }
            }, 700);
        }
    };

    @Override
    public void onCourseItemClick(DisplayCourse course) {
        selectedCourse = course;

        String sectionName = getIntent().getStringExtra("section_name");
        String courseName = course.getCourseTitle();
        String courseNameEnglish = course.getCourseTitleEnglish();

        enrolledCourseLiveData = viewModel.getCourseEnrolled();

        if (!enrolledCourseLiveData.hasObservers()) {
            enrolledCourseLiveData.observe(DisplayCourseActivity.this, new Observer<List<String>>() {
                @Override
                public void onChanged(List<String> strings) {
                    if (!strings.contains(courseNameEnglish)) {
                        // if not in db from before
                        Log.d(TAG, "onChanged: course is not in db");
                        enrolledCourseLiveData.removeObservers(DisplayCourseActivity.this);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
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
}