package com.ofk.bd.Fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.database.DataSnapshot;
import com.ofk.bd.Adapter.ActivitySliderAdapter;
import com.ofk.bd.Adapter.CourseSliderAdapter;
import com.ofk.bd.Adapter.VideoSliderAdapter;
import com.ofk.bd.DisplayCourseActivityAdapter.CourseListAdapter;
import com.ofk.bd.HelperClass.Activity;
import com.ofk.bd.HelperClass.Course;
import com.ofk.bd.HelperClass.DisplayCourse;
import com.ofk.bd.HelperClass.Video;
import com.ofk.bd.R;
import com.ofk.bd.SearchResultActivity;
import com.ofk.bd.ViewModel.MainActivityViewModel;
import com.ofk.bd.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<Course> courseList = new ArrayList<>();

    private FragmentHomeBinding binding;

    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private ViewPager.OnPageChangeListener mOnPageChangeListenerForActivityViewPager;
    private ViewPager.OnPageChangeListener mOnPageChangeListenerForActivityVideoViewPager;

    private int[] indicator = {R.drawable.dot, R.drawable.inactivedot};

    private CourseSliderAdapter adapter;

    private CourseListAdapter recom_course_1;

    private CourseListAdapter recom_course_2;

    private int customPosition = 0;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    // activity pics list
    private List<Activity> activityPics;

    // activity video list
    private List<Video> videoList;

    // random course 1 display
    private List<DisplayCourse> randomCourse_1;

    // random course 2 display
    private List<DisplayCourse> randomCourse_2;

    private MainActivityViewModel mainActivityViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        activityPics = new ArrayList<>();
        randomCourse_1 = new ArrayList<>();
        randomCourse_2 = new ArrayList<>();
        videoList = new ArrayList<>();

        mainActivityViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
    }

    // activity pics adapter
    private ActivitySliderAdapter activityAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(getLayoutInflater());

        // getting course list from view model
        mainActivityViewModel.getListMutableLiveData().observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {
                courseList = courses;
                adapter = new CourseSliderAdapter(getContext(), getActivity(), courseList);
                binding.courseViewPager.setAdapter(adapter);
                changeIndicator(0);// as indicator depends on the course list, it is inside this method
            }
        });

        // This is activity view pager where some pictures are loaded
        binding.activityViewPager.setClipToPadding(false);
        binding.activityViewPager.setPageMargin(30);

        mainActivityViewModel.getActivityPicLiveData().observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                Activity activity = dataSnapshot.getValue(Activity.class);
                activityPics.add(activity);
                activityAdapter = new ActivitySliderAdapter(getContext(), activityPics, "activity");
                binding.activityViewPager.setAdapter(activityAdapter);
                changeIndicatorOnActivityViewPager(0);
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        // random course 1
        binding.randomCourseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        mainActivityViewModel.getRandomCourseLiveData_1().observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                DisplayCourse course = dataSnapshot.getValue(DisplayCourse.class);
                randomCourse_1.add(course);
                recom_course_1 = new CourseListAdapter(randomCourse_1, "home_page");
                binding.randomCourseRecyclerView.setAdapter(recom_course_1);
            }
        });

        // random course 2
        binding.randomCourseRecyclerView2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        mainActivityViewModel.getRandomCourseLiveData_2().observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                DisplayCourse course = dataSnapshot.getValue(DisplayCourse.class);
                randomCourse_2.add(course);
                recom_course_2 = new CourseListAdapter(randomCourse_2, "home_page");
                binding.randomCourseRecyclerView2.setAdapter(recom_course_2);
            }
        });

        if (mOnPageChangeListener == null) {
            mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {
                    //changeIndicator(i);
                }

                @Override
                public void onPageSelected(int i) {
                    if (customPosition > adapter.getCount() - 1) {
                        customPosition = 0;
                    }
                    changeIndicator(i++);
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            };
            binding.courseViewPager.addOnPageChangeListener(mOnPageChangeListener);
        }

        if (mOnPageChangeListenerForActivityViewPager == null) {
            mOnPageChangeListenerForActivityViewPager = new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {
                    //changeIndicator(i);
                }

                @Override
                public void onPageSelected(int i) {
                    if (customPosition > adapter.getCount() - 1) {
                        customPosition = 0;
                    }
                    changeIndicatorOnActivityViewPager(i++);
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            };
            binding.activityViewPager.addOnPageChangeListener(mOnPageChangeListenerForActivityViewPager);
        }

        // this is for activity videos view pager
        binding.activityVideoViewPager.setClipToPadding(false);
        binding.activityVideoViewPager.setPageMargin(30);

        mainActivityViewModel.getActivityVideoLiveData().observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                Video video = dataSnapshot.getValue(Video.class);
                videoList.add(video);
                binding.activityVideoViewPager.setAdapter(new VideoSliderAdapter(videoList, getContext(), getLifecycle()));
            }
        });

        if (mOnPageChangeListenerForActivityVideoViewPager == null) {
            mOnPageChangeListenerForActivityVideoViewPager = new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {
                    //changeIndicator(i);
                }

                @Override
                public void onPageSelected(int i) {

                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            };
            binding.activityVideoViewPager.addOnPageChangeListener(mOnPageChangeListenerForActivityVideoViewPager);
        }

        binding.searchButtonCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.searchEditText.getText().toString().equals("")) {
                    Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                    intent.putExtra("searchQuery", binding.searchEditText.getText().toString().trim());
                    startActivity(intent);
                } else {
                    binding.searchEditText.setError("Please enter something");
                }

            }
        });

        return binding.getRoot();
    }

    // handles indicator in courses view pager
    private void changeIndicator(int currentSlidePosition) {
        if (binding.dotLayout.getChildCount() > 0) {
            binding.dotLayout.removeAllViews();
        }

        ImageView[] dots = new ImageView[adapter.getCount()];

        for (int i = 0; i < adapter.getCount(); i++) {
            dots[i] = new ImageView(getContext());

            if (i == currentSlidePosition) {
                //dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.dot));

                dots[i].setImageResource(indicator[0]);
            } else {
                //dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.inactivedot));
                dots[i].setImageResource(indicator[1]);
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            layoutParams.setMargins(4, 0, 4, 0);
            binding.dotLayout.addView(dots[i], layoutParams);
        }
    }

    // handles indicator in activity view pager
    private void changeIndicatorOnActivityViewPager(int currentSlidePosition) {
        if (binding.dotLayout2.getChildCount() > 0) {
            binding.dotLayout2.removeAllViews();
        }

        ImageView[] dots = new ImageView[activityAdapter.getCount()];

        for (int i = 0; i < activityAdapter.getCount(); i++) {
            dots[i] = new ImageView(getContext());

            if (i == currentSlidePosition) {
                //dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.dot));

                dots[i].setImageResource(indicator[0]);
            } else {
                //dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.inactivedot));
                dots[i].setImageResource(indicator[1]);
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            layoutParams.setMargins(4, 0, 4, 0);
            binding.dotLayout2.addView(dots[i], layoutParams);
        }
    }

    public static boolean isVisible(final View view) {
        if (view == null) {
            return false;
        }
        if (!view.isShown()) {
            return false;
        }
        final Rect actualPosition = new Rect();
        view.getGlobalVisibleRect(actualPosition);
        final Rect screen = new Rect(0, 0, Resources.getSystem().getDisplayMetrics().widthPixels, Resources.getSystem().getDisplayMetrics().heightPixels);
        return actualPosition.intersect(screen);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        activityPics.clear();
        randomCourse_1.clear();
        randomCourse_2.clear();
        videoList.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        activityPics.clear();
        randomCourse_1.clear();
        randomCourse_2.clear();
        videoList.clear();
    }
}