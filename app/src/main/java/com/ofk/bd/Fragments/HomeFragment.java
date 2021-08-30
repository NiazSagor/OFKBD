package com.ofk.bd.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.ofk.bd.Adapter.ActivitySliderAdapter;
import com.ofk.bd.Adapter.CourseSliderListAdapter;
import com.ofk.bd.Adapter.VideoSliderAdapter;
import com.ofk.bd.CourseActivity;
import com.ofk.bd.DisplayCourseActivity;
import com.ofk.bd.DisplayCourseActivityAdapter.CourseListAdapter;
import com.ofk.bd.HelperClass.Common;
import com.ofk.bd.Model.Activity;
import com.ofk.bd.Model.Course;
import com.ofk.bd.Model.DisplayCourse;
import com.ofk.bd.Model.UserInfo;
import com.ofk.bd.Model.Video;
import com.ofk.bd.R;
import com.ofk.bd.SearchResultActivity;
import com.ofk.bd.Utility.StringUtility;
import com.ofk.bd.ViewModel.MainActivityViewModel;
import com.ofk.bd.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private FragmentHomeBinding binding;

    private static final int[] indicator = {R.drawable.dot, R.drawable.inactivedot};

    private CourseListAdapter recommendedSectionCourseList;

    private int customPosition = 0;

    private MainActivityViewModel mainActivityViewModel;

    // activity pics adapter
    private ActivitySliderAdapter activityAdapter;

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

        if (!mainActivityViewModel.getAllCategoriesLiveData().hasObservers()) {
            mainActivityViewModel.getAllCategoriesLiveData().observe(this, sectionListObserver);
            mainActivityViewModel.getUserInfoLiveData2().observe(this, userInfoObserver);
        }

        if (!mainActivityViewModel.getRandomCourseLiveData(Common.courseToDisplay + " Section").hasObservers()) {
            mainActivityViewModel.getRandomCourseLiveData(Common.courseToDisplay + " Section").observe(this, recommendedSectionObserver);
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
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //binding.rateOFKView.setOnClickListener(listener);
        //binding.shareOKFView.setOnClickListener(listener);
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

            CourseSliderListAdapter courseSliderListAdapter = new CourseSliderListAdapter(getContext(), courses, "viewpager0");

            binding.courseRecyclerView.setAdapter(courseSliderListAdapter);

            courseSliderListAdapter.setOnItemClickListener(new CourseSliderListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, View view) {
                    Intent intent = new Intent(getActivity(), DisplayCourseActivity.class);
                    intent.putExtra("section_name", courses.get(position).getCourseTitle());
                    intent.putExtra("section_name_bangla", courses.get(position).getCourseSubtitle());
                    Objects.requireNonNull(getActivity()).startActivity(intent);
                }
            });
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

    private final Observer<List<Video>> activityVideoObserver = new Observer<List<Video>>() {
        @Override
        public void onChanged(List<Video> videoList) {
            VideoSliderAdapter videoSliderAdapter = new VideoSliderAdapter(getChildFragmentManager(), getLifecycle(), videoList);
            binding.activityVideoViewPager.setAdapter(videoSliderAdapter);
        }
    };

    private final Observer<List<Activity>> activityPicObserver = new Observer<List<Activity>>() {
        @Override
        public void onChanged(List<Activity> activities) {

            List<SlideModel> images = new ArrayList<>();

            for (Activity activity : activities) {
                images.add(new SlideModel(activity.getUrl(), "", ScaleTypes.FIT));
            }

            binding.activityViewPager.setImageList(images);

        }
    };

    private final Observer<List<DisplayCourse>> recommendedSectionObserver = new Observer<List<DisplayCourse>>() {
        @Override
        public void onChanged(List<DisplayCourse> displayCourses) {
            recommendedSectionCourseList = new CourseListAdapter(displayCourses, "home_page");
            binding.randomCourseRecyclerView2.setAdapter(recommendedSectionCourseList);
            recommendedSectionCourseList.setOnItemClickListener(onItemClickListener);
        }
    };

    private final CourseListAdapter.OnItemClickListener onItemClickListener = new CourseListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position, View view) {

            Intent intent = new Intent(getActivity(), CourseActivity.class);

            intent.putExtra("section_name", Common.courseToDisplay);
            intent.putExtra("course_name", recommendedSectionCourseList.getCurrentCourse(position).getCourseTitle());
            intent.putExtra("course_name_english", recommendedSectionCourseList.getCurrentCourse(position).getCourseTitleEnglish());
            intent.putExtra("from", "home");
            intent.putExtra("isNewCourse", false);

            startActivity(intent);
        }
    };

    private final View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == binding.goUpFloatingButton.getId()) {
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