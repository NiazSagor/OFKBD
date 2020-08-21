package com.ofk.bd.Fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.ofk.bd.Adapter.ActivitySliderAdapter;
import com.ofk.bd.Adapter.CourseSliderListAdapter;
import com.ofk.bd.Adapter.VideoSliderAdapter;
import com.ofk.bd.CourseActivity;
import com.ofk.bd.DisplayCourseActivity;
import com.ofk.bd.DisplayCourseActivityAdapter.CourseListAdapter;
import com.ofk.bd.HelperClass.Activity;
import com.ofk.bd.HelperClass.Common;
import com.ofk.bd.HelperClass.Course;
import com.ofk.bd.HelperClass.DisplayCourse;
import com.ofk.bd.HelperClass.Video;
import com.ofk.bd.R;
import com.ofk.bd.SearchResultActivity;
import com.ofk.bd.ViewModel.MainActivityViewModel;
import com.ofk.bd.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Calendar;
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

    private FragmentHomeBinding binding;

    private ViewPager.OnPageChangeListener mOnPageChangeListenerForActivityVideoViewPager;

    private static int[] indicator = {R.drawable.dot, R.drawable.inactivedot};

    private CourseSliderListAdapter courseSliderListAdapter;

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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: ");
    }

    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Log.d(TAG, "onCreate: ");

        if (videoList == null) {
            videoList = new ArrayList<>();
        }

        if (mainActivityViewModel == null) {
            mainActivityViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
        }

        handler = new Handler(Looper.getMainLooper());
    }

    // activity pics adapter
    private ActivitySliderAdapter activityAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");

        binding = FragmentHomeBinding.inflate(getLayoutInflater());

        binding.courseRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        // This is activity view pager where some pictures are loaded
        binding.activityViewPager.setClipToPadding(false);
        binding.activityViewPager.setPageTransformer(new MarginPageTransformer(40));

        // random course 1, 2
        binding.randomCourseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.randomCourseRecyclerView2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // this is for activity videos view pager
        binding.activityVideoViewPager.setClipToPadding(false);
        binding.activityVideoViewPager.setPageMargin(30);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (recom_course_1 == null) {
            recom_course_1 = new CourseListAdapter(Common.randomCourses, "home_page");
            binding.randomCourseRecyclerView.setAdapter(recom_course_1);

            recom_course_1.setOnItemClickListener(new CourseListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, View view) {
                    Intent intent = new Intent(getActivity(), CourseActivity.class);
                    intent.putExtra("section_name", "Robotics");
                    intent.putExtra("course_name", recom_course_1.getCurrentCourse(position).getCourseTitle());
                    intent.putExtra("course_name_english", recom_course_1.getCurrentCourse(position).getCourseTitleEnglish());
                    intent.putExtra("from", "home");
                    startActivity(intent);
                }
            });
        }

        if (recom_course_2 == null) {
            recom_course_2 = new CourseListAdapter(Common.randomCourses2, "home_page");
            binding.randomCourseRecyclerView2.setAdapter(recom_course_2);

            recom_course_2.setOnItemClickListener(new CourseListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, View view) {
                    Intent intent = new Intent(getActivity(), CourseActivity.class);
                    intent.putExtra("section_name", "Arts");
                    intent.putExtra("course_name", recom_course_2.getCurrentCourse(position).getCourseTitle());
                    intent.putExtra("course_name_english", recom_course_2.getCurrentCourse(position).getCourseTitleEnglish());
                    intent.putExtra("from", "home");
                    startActivity(intent);
                }
            });
        }

        if (activityAdapter == null) {
            activityAdapter = new ActivitySliderAdapter(Common.activityList);
            binding.activityViewPager.setAdapter(activityAdapter);
            changeIndicatorOnActivityViewPager(0);
        }

        binding.searchButtonCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.searchEditText.getText().toString().equals("")) {
                    Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                    intent.putExtra("searchQuery", binding.searchEditText.getText().toString().trim());
                    startActivity(intent);
                    binding.searchEditText.setText("");
                } else {
                    binding.searchEditText.setError("Please enter something");
                }
            }
        });

        binding.rateOFKView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("market://details?id=com.angik.duodevloopers.food");
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
                        .addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=com.angik.duodevloopers.food")));
                }
            }
        });

        binding.shareOKFView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Share OFK");
                i.putExtra(Intent.EXTRA_TEXT, "http://www.google.com");
                startActivity(Intent.createChooser(i, "Share URL"));
            }
        });

        binding.goUpFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.parentScrollView.smoothScrollTo(0, 0);
            }
        });
    }

    // when fragment is visible
    @Override
    public void onStart() {
        super.onStart();

        Log.d(TAG, "onStart: ");

        if (!mainActivityViewModel.getListMutableLiveData().hasObservers()) {
            mainActivityViewModel.getListMutableLiveData().observe(this, sectionListObserver);
        }

        binding.activityVideoViewPager.setAdapter(new VideoSliderAdapter(Common.activityVideoList, getContext(), getLifecycle()));
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "onResume: ");

        binding.activityViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (customPosition > activityAdapter.getItemCount() - 1) {
                    customPosition = 0;
                }
                changeIndicatorOnActivityViewPager(position++);
            }
        });

        handler.post(new Runnable() {
            @Override
            public void run() {
                Calendar c = Calendar.getInstance();

                int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

                if (timeOfDay >= 0 && timeOfDay < 12) {
                    binding.geetingMessage.setText("Good Morning");
                } else if (timeOfDay >= 12 && timeOfDay < 16) {
                    binding.geetingMessage.setText("Good Afternoon");
                } else if (timeOfDay >= 16 && timeOfDay < 21) {
                    binding.geetingMessage.setText("Good Evening");
                } else if (timeOfDay >= 21 && timeOfDay < 24) {
                    binding.geetingMessage.setText("Good Night");
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    private final Observer<List<Course>> sectionListObserver = new Observer<List<Course>>() {
        @Override
        public void onChanged(List<Course> courses) {
            courseSliderListAdapter = new CourseSliderListAdapter(courses, "viewpager0");

            binding.courseRecyclerView.setAdapter(courseSliderListAdapter);

            courseSliderListAdapter.setOnItemClickListener(new CourseSliderListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, View view) {
                    Intent intent = new Intent(getActivity(), DisplayCourseActivity.class);
                    intent.putExtra("section_name", courses.get(position).getCourseTitle());
                    intent.putExtra("section_name_bangla", courses.get(position).getCourseSubtitle());
                    getActivity().startActivity(intent);
                    Log.d(TAG, "onItemClick: " + courses.get(position).getCourseTitle());
                }
            });
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
}