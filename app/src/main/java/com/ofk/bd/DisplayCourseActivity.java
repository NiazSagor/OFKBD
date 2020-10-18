package com.ofk.bd;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.kalert.KAlertDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ofk.bd.DisplayCourseActivityAdapter.CourseListAdapter;
import com.ofk.bd.HelperClass.DisplayCourse;
import com.ofk.bd.HelperClass.UserProgressClass;
import com.ofk.bd.Interface.DisplayCourseLoadCallback;
import com.ofk.bd.ViewModel.DisplayCourseActivityViewModel;
import com.ofk.bd.databinding.ActivityDisplayCourseBinding;

import java.util.ArrayList;
import java.util.List;

public class DisplayCourseActivity extends AppCompatActivity {

    private static final String TAG = "DisplayCourseActivity";

    private ActivityDisplayCourseBinding binding;

    private DisplayCourseActivityViewModel viewModel;

    private List<DisplayCourse> availableCourses;

    private KAlertDialog pDialog;

    private Handler handler;

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

        handler = new Handler();
        viewModel = ViewModelProviders.of(DisplayCourseActivity.this).get(DisplayCourseActivityViewModel.class);

        String headline = getIntent().getStringExtra("section_name_bangla") + " কোর্স";
        binding.sectionHeadline.setText(headline);
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
            showAlertDialog("done");
            return;
        }

        setupCourseRecyclerView();
    }


    private void setupCourseRecyclerView() {

        showAlertDialog("start");

        RecyclerView courseRecyclerView = binding.courseRecyclerView;

        courseRecyclerView.setLayoutManager(new GridLayoutManager(DisplayCourseActivity.this, 2));

        getData(new DisplayCourseLoadCallback() {
            @Override
            public void onLoadCallback(List<DisplayCourse> courses) {

                CourseListAdapter adapter = new CourseListAdapter(courses, "displayCourse");

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        courseRecyclerView.setAdapter(adapter);
                        String count = adapter.getItemCount() + " টি কোর্স";
                        binding.courseCount.setText(count);
                        showAlertDialog("end");
                    }
                }, 1500);

                adapter.setOnItemClickListener(new CourseListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View view) {

                        String sectionName = getIntent().getStringExtra("section_name");
                        String sectionNameBangla = getIntent().getStringExtra("section_name_bangla");
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
                                        showAlertDialog(sectionName, sectionNameBangla, courseName, courseNameEnglish, thumbNailURL);
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

    private void showAlertDialog(String sectionName, String sectionNameBangla, String courseName, String courseNameEnglish, String thumbnailURL) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("কোর্সটি শুরু করতে চাও?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "হ্যা",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = new Intent(DisplayCourseActivity.this, CourseActivity.class);
                        intent.putExtra("section_name", sectionName);
                        intent.putExtra("course_name", courseName);
                        intent.putExtra("course_name_english", courseNameEnglish);
                        intent.putExtra("from", "display");

                        viewModel.insert(new UserProgressClass(sectionName, sectionNameBangla, courseName, courseNameEnglish, thumbnailURL, 9, 0));

                        startActivity(intent);

                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "না",
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

        if (enrolledCourseLiveData.hasObservers()) {
            enrolledCourseLiveData.removeObservers(this);
        }
        if (availableCourseLiveData.hasObservers()) {
            availableCourseLiveData.removeObservers(this);
        }
    }


    private void getData(DisplayCourseLoadCallback callback) {

        availableCourses = new ArrayList<>();

        String node = getIntent().getStringExtra("section_name") + " Section";

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Section").child(node);

        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                DisplayCourse course = dataSnapshot.getValue(DisplayCourse.class);
                availableCourses.add(course);
                callback.onLoadCallback(availableCourses);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showAlertDialog(String command) {
        switch (command) {
            case "start":
                pDialog = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#00c1c3"));
                pDialog.setTitleText("লোড হচ্ছে");
                pDialog.setCancelable(true);
                pDialog.show();
                break;
            case "end":
                //pDialog.dismissWithAnimation();
                pDialog.dismiss();
                break;
            case "done":
                pDialog.dismiss();
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
                break;
        }
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}