package com.ofk.bd.Fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

import com.ofk.bd.Adapter.ActivitySliderAdapter;
import com.ofk.bd.Adapter.CourseSliderListAdapter;
import com.ofk.bd.Adapter.VideoSliderAdapter;
import com.ofk.bd.AsyncTasks.FirebaseQuerySubSection;
import com.ofk.bd.CourseActivity;
import com.ofk.bd.DisplayCourseActivity;
import com.ofk.bd.DisplayCourseActivityAdapter.CourseListAdapter;
import com.ofk.bd.HelperClass.Common;
import com.ofk.bd.HelperClass.Course;
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

    // activity video list
    private List<Video> videoList;

    private MainActivityViewModel mainActivityViewModel;

    private final Observer<List<Course>> sectionListObserver = new Observer<List<Course>>() {
        @Override
        public void onChanged(List<Course> courses) {
            if (courses != null) {
                courseSliderListAdapter = new CourseSliderListAdapter(getContext(), courses, "viewpager0");

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
        }
    };

    private Handler handler;
    private final Observer<UserInfo> userInfoObserver = new Observer<UserInfo>() {
        @Override
        public void onChanged(UserInfo userInfo) {
            if (userInfo != null) {
                binding.userNameTextView.setText(userInfo.getUserName());
            }
        }
    };

    // activity pics adapter
    private ActivitySliderAdapter activityAdapter;
    private final View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == binding.rateOFKView.getId()) {
                Uri uri = Uri.parse("market://details?id=com.angik.duodevloopers.food");
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
                        .addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=com.angik.duodevloopers.food")));// TODO ofk play store link
                }
            } else if (view.getId() == binding.shareOKFView.getId()) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Share OFK");
                i.putExtra(Intent.EXTRA_TEXT, "http://www.google.com");// TODO ofk play store link
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
    private final CourseListAdapter.OnItemClickListener onItemClickListener = new CourseListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position, View view) {

            AlertDialogUtility dialog = new AlertDialogUtility();

            dialog.showAlertDialog(getContext(), "start");

            Intent intent = new Intent(getActivity(), CourseActivity.class);

            String courseName = recom_course_2.getCurrentCourse(position).getCourseTitleEnglish();

            intent.putExtra("section_name", Common.courseToDisplay);
            intent.putExtra("course_name", recom_course_2.getCurrentCourse(position).getCourseTitle());
            intent.putExtra("course_name_english", recom_course_2.getCurrentCourse(position).getCourseTitleEnglish());
            intent.putExtra("from", "home");

            new FirebaseQuerySubSection(new SectionVideoLoadCallback() {
                @Override
                public void onSectionVideoLoadCallback(List<SectionVideo> sectionVideoList, int totalVideos) {

                    if (sectionVideoList == null) {
                        dialog.showAlertDialog(getContext(), "done");
                        return;
                    }

                    Common.sectionVideoList = sectionVideoList;

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismissAlertDialog();
                            startActivity(intent);
                        }
                    }, 2300);
                }
            }, Common.courseToDisplay, courseName).execute();
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: ");

        if (mainActivityViewModel == null) {
            mainActivityViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
        }

        if (!mainActivityViewModel.getUserInfoLiveData2().hasObservers()) {
            mainActivityViewModel.getUserInfoLiveData2().observe(this, userInfoObserver);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Log.d(TAG, "onCreate: ");

        handler = new Handler();

        if (videoList == null) {
            videoList = new ArrayList<>();
        }

        if (!mainActivityViewModel.getListMutableLiveData().hasObservers()) {
            mainActivityViewModel.getListMutableLiveData().observe(this, sectionListObserver);
        }


        if (recom_course_2 == null) {
            recom_course_2 = new CourseListAdapter(Common.randomCourses2, "home_page");
        }

        if (activityAdapter == null) {
            activityAdapter = new ActivitySliderAdapter(Common.activityList);
        }
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
        binding.randomCourseRecyclerView2.setAdapter(recom_course_2);
        binding.activityViewPager.setAdapter(activityAdapter);
        VideoSliderAdapter adapter = new VideoSliderAdapter(getFragmentManager(), getLifecycle(), Common.activityVideoList);
        binding.activityVideoViewPager.setAdapter(adapter);
    }

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
        // fragment is visible, now we attach interaction listeners
        recom_course_2.setOnItemClickListener(onItemClickListener);
        changeIndicatorOnActivityViewPager(0);
    }

    @Override
    public void onResume() {
        super.onResume();

        binding.activityViewPager.registerOnPageChangeCallback(onPageChangeCallback);
    }
}