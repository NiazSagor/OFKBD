package com.ofk.bd.Fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.database.DataSnapshot;
import com.ofk.bd.Adapter.ActivitySliderAdapter;
import com.ofk.bd.Adapter.CourseSliderListAdapter;
import com.ofk.bd.Adapter.VideoSliderAdapter;
import com.ofk.bd.AsyncTasks.FirebaseQuerySubSection;
import com.ofk.bd.CourseActivity;
import com.ofk.bd.DisplayCourseActivity;
import com.ofk.bd.DisplayCourseActivityAdapter.CourseListAdapter;
import com.ofk.bd.HelperClass.Activity;
import com.ofk.bd.HelperClass.Common;
import com.ofk.bd.HelperClass.Course;
import com.ofk.bd.HelperClass.DisplayCourse;
import com.ofk.bd.HelperClass.SectionVideo;
import com.ofk.bd.HelperClass.UserInfo;
import com.ofk.bd.HelperClass.Video;
import com.ofk.bd.Interface.SectionVideoLoadCallback;
import com.ofk.bd.R;
import com.ofk.bd.SearchResultActivity;
import com.ofk.bd.Utility.AlertDialogUtility;
import com.ofk.bd.Utility.StringUtility;
import com.ofk.bd.ViewModel.MainActivityViewModel;
import com.ofk.bd.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private FragmentHomeBinding binding;

    private static final int[] indicator = {R.drawable.dot, R.drawable.inactivedot};

    private CourseListAdapter recommendedSectionCourseList;

    private int customPosition = 0;

    private MainActivityViewModel mainActivityViewModel;

    // activity pics adapter
    private ActivitySliderAdapter activityAdapter;

    private VideoSliderAdapter videoSliderAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: ");

        if (mainActivityViewModel == null) {
            mainActivityViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!mainActivityViewModel.getRandomCourseLiveData(Common.courseToDisplay).hasObservers()) {
            mainActivityViewModel.getRandomCourseLiveData(Common.courseToDisplay).observe(this, recommendedCourseObserver);
        }

        if (!mainActivityViewModel.getActivityVideoLiveData().hasObservers()) {
            mainActivityViewModel.getActivityVideoLiveData().observe(this, activityVideoObserver);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");

        binding = FragmentHomeBinding.inflate(getLayoutInflater());

        binding.courseRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        // This is activity view pager where some pictures are loaded
        binding.activityViewPager.setClipToPadding(false);
        binding.activityViewPager.setPageTransformer(new MarginPageTransformer(40));

        LinearLayoutManager manager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        manager2.setItemPrefetchEnabled(true);
        manager2.setInitialPrefetchItemCount(3);

        // random course 1, 2
        binding.randomCourseRecyclerView2.setLayoutManager(manager2);
        binding.randomCourseRecyclerView2.setHasFixedSize(true);

        // this is for activity videos view pager
        binding.activityVideoViewPager.setClipToPadding(false);
        binding.activityVideoViewPager.setPageTransformer(new MarginPageTransformer(40));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // after view is created we are showing ui elements
        binding.recommendedCourseTextView2.setText(Common.courseHeadline);

        if (!mainActivityViewModel.getListMutableLiveData().hasObservers()) {
            mainActivityViewModel.getListMutableLiveData().observe(this, sectionListObserver);
            mainActivityViewModel.getUserInfoLiveData2().observe(this, userInfoObserver);
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        binding.rateOFKView.setOnClickListener(listener);
        binding.shareOKFView.setOnClickListener(listener);
        binding.goUpFloatingButton.setOnClickListener(listener);
        binding.searchButtonCardView.setOnClickListener(listener);
    }

    // when fragment is visible
    @Override
    public void onStart() {
        super.onStart();

        binding.geetingMessage.setText(StringUtility.getGreetingMessage(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)));

        if (!mainActivityViewModel.getActivityPicLiveData().hasObservers()) {
            mainActivityViewModel.getActivityPicLiveData().observe(this, activityPicObserver);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private final Observer<List<Course>> sectionListObserver = new Observer<List<Course>>() {
        @Override
        public void onChanged(List<Course> courses) {
            if (courses != null) {
                CourseSliderListAdapter courseSliderListAdapter = new CourseSliderListAdapter(getContext(), courses, "viewpager0");

                binding.courseRecyclerView.setAdapter(courseSliderListAdapter);

                courseSliderListAdapter.setOnItemClickListener(new CourseSliderListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View view) {
                        Intent intent = new Intent(getActivity(), DisplayCourseActivity.class);
                        intent.putExtra("section_name", courses.get(position).getCourseTitle());
                        intent.putExtra("section_name_bangla", courses.get(position).getCourseSubtitle());
                        getActivity().startActivity(intent);
                    }
                });
            }
        }
    };

    private final Observer<UserInfo> userInfoObserver = new Observer<UserInfo>() {
        @Override
        public void onChanged(UserInfo userInfo) {
            if (userInfo != null) {
                binding.userNameTextView.setText(userInfo.getUserName());
            }
        }
    };

    private final androidx.lifecycle.Observer<DataSnapshot> activityVideoObserver = new Observer<DataSnapshot>() {
        @Override
        public void onChanged(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {

                List<Video> activityVideoList = new ArrayList<>();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    activityVideoList.add(ds.getValue(Video.class));
                }

                videoSliderAdapter = new VideoSliderAdapter(getChildFragmentManager(), getLifecycle(), activityVideoList);
                binding.activityVideoViewPager.setAdapter(videoSliderAdapter);
            }
        }
    };

    private final androidx.lifecycle.Observer<DataSnapshot> activityPicObserver = new Observer<DataSnapshot>() {
        @Override
        public void onChanged(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {

                List<Activity> activityPicList = new ArrayList<>();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    activityPicList.add(ds.getValue(Activity.class));
                }

                activityAdapter = new ActivitySliderAdapter(activityPicList);
                binding.activityViewPager.setAdapter(activityAdapter);
                binding.activityViewPager.registerOnPageChangeCallback(onPageChangeCallback);
            }
        }
    };

    private final androidx.lifecycle.Observer<DataSnapshot> recommendedCourseObserver = new Observer<DataSnapshot>() {
        @Override
        public void onChanged(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {

                List<DisplayCourse> recommendedCourse = new ArrayList<>();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    recommendedCourse.add(ds.getValue(DisplayCourse.class));
                }
                recommendedSectionCourseList = new CourseListAdapter(recommendedCourse, "home_page");
                binding.randomCourseRecyclerView2.setAdapter(recommendedSectionCourseList);
                recommendedSectionCourseList.setOnItemClickListener(onItemClickListener);
            }
        }
    };

    private final CourseListAdapter.OnItemClickListener onItemClickListener = new CourseListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position, View view) {

            AlertDialogUtility dialog = new AlertDialogUtility();

            Intent intent = new Intent(getActivity(), CourseActivity.class);

            String courseName = recommendedSectionCourseList.getCurrentCourse(position).getCourseTitleEnglish();

            intent.putExtra("section_name", Common.courseToDisplay);
            intent.putExtra("course_name", recommendedSectionCourseList.getCurrentCourse(position).getCourseTitle());
            intent.putExtra("course_name_english", recommendedSectionCourseList.getCurrentCourse(position).getCourseTitleEnglish());
            intent.putExtra("from", "home");

            new FirebaseQuerySubSection(new SectionVideoLoadCallback() {
                @Override
                public void onSectionVideoLoadCallback(List<SectionVideo> sectionVideoList, int totalVideos) {

                    if (sectionVideoList == null || sectionVideoList.size() == 0) {
                        dialog.showAlertDialog(getContext(), "videoNotFound");
                        return;
                    }

                    Common.sectionVideoList = sectionVideoList;

                    startActivity(intent);
                }
            }, Common.courseToDisplay, courseName).execute();
        }
    };

    private final View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == binding.rateOFKView.getId()) {
                Uri uri = Uri.parse("market://details?id=com.ofk.bd");
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
                        .addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else if (view.getId() == binding.shareOKFView.getId()) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Share OFK");
                i.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.ofk.bd");// TODO ofk play store link
                startActivity(Intent.createChooser(i, "Share URL"));
            } else if (view.getId() == binding.goUpFloatingButton.getId()) {
                binding.parentScrollView.smoothScrollTo(0, 0);
            } else if (view.getId() == binding.searchButtonCardView.getId()) {
                if (!binding.searchEditText.getText().toString().equals("")) {
                    Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                    intent.putExtra("searchQuery", binding.searchEditText.getText().toString().trim());
                    startActivity(intent);
                    binding.searchEditText.setText("");
                } else {
                    binding.searchEditText.setError("Please enter something");
                }
            }
        }
    };

    private final ViewPager2.OnPageChangeCallback onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            if (customPosition > activityAdapter.getItemCount() - 1) {
                customPosition = 0;
            }
            changeIndicatorOnActivityViewPager(position++);
        }
    };

    // handles indicator in activity view pager
    private void changeIndicatorOnActivityViewPager(int currentSlidePosition) {
        if (binding.dotLayout2.getChildCount() > 0) {
            binding.dotLayout2.removeAllViews();
        }

        ImageView[] dots = new ImageView[activityAdapter.getItemCount()];

        for (int i = 0; i < activityAdapter.getItemCount(); i++) {
            dots[i] = new ImageView(getContext());

            if (i == currentSlidePosition) {
                dots[i].setImageResource(indicator[0]);
            } else {
                dots[i].setImageResource(indicator[1]);
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            layoutParams.setMargins(4, 0, 4, 0);
            binding.dotLayout2.addView(dots[i], layoutParams);
        }
    }
}