package com.ofk.bd.Fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ofk.bd.Adapter.CourseSliderAdapter;
import com.ofk.bd.Adapter.CourseSliderListAdapter;
import com.ofk.bd.Adapter.MoreCourseSliderAdapter;
import com.ofk.bd.Adapter.VideoSliderAdapter;
import com.ofk.bd.HelperClass.Course;
import com.ofk.bd.HelperClass.Video;
import com.ofk.bd.R;
import com.ofk.bd.Interface.VideoLoadCallback;
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
    private List<Course> recommendedCourseList = new ArrayList<>();

    private FragmentHomeBinding binding;

    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private ViewPager.OnPageChangeListener mOnPageChangeListenerForActivityViewPager;
    private ViewPager.OnPageChangeListener mOnPageChangeListenerForActivityVideoViewPager;

    private int[] indicator = {R.drawable.dot, R.drawable.inactivedot};

    private CourseSliderAdapter adapter;
    private CourseSliderListAdapter recommendedCourseAdapter;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private MoreCourseSliderAdapter activityAdapter;

    private List<Course> moreCourses;
    private List<Video> activityVideos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater());

        createDummyCourse();
        createDummyActivityForViewPager();
        //createDummyActivityVideoForViewPager();

        // This is activity view pager where some pictures are loaded
        activityAdapter = new MoreCourseSliderAdapter(getActivity(), moreCourses, "activity");
        binding.activityViewPager.setClipToPadding(false);
        binding.activityViewPager.setPageMargin(30);
        binding.activityViewPager.setAdapter(activityAdapter);

        // This is below the my course, where all the categories are displayed
        adapter = new CourseSliderAdapter(getContext(), getActivity(), courseList);
        binding.courseViewPager.setAdapter(adapter);

        // This is below the activity view pager
        recommendedCourseAdapter = new CourseSliderListAdapter(recommendedCourseList, "random");

        binding.randomCourseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.randomCourseRecyclerView.setAdapter(recommendedCourseAdapter);

        // This is below recommended view pager
        binding.randomCourseRecyclerView2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.randomCourseRecyclerView2.setAdapter(recommendedCourseAdapter);

        changeIndicator(0);
        changeIndicatorOnActivityViewPager(0);

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

        // this is for activity videos
        binding.activityVideoViewPager.setClipToPadding(false);
        binding.activityVideoViewPager.setPageMargin(30);

        //new GetVideo(binding.activityVideoViewPager, getContext()).execute();

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

        return binding.getRoot();
    }

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

    private void createDummyCourse() {
        for (int i = 0; i < 8; i++) {
            courseList.add(new Course("Course " + i));
            recommendedCourseList.add(new Course("Course " + i, "Subtitle " + i));
        }
    }

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

    private void createDummyActivityForViewPager() {

        moreCourses = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            moreCourses.add(new Course("Course " + i, "Subtitle " + i));
        }
    }

    public static class GetVideo extends AsyncTask<Void, Void, Void> {

        ViewPager viewPager;
        Context context;

        List<Video> list = new ArrayList<>();

        public GetVideo(ViewPager viewPager, Context context) {
            this.viewPager = viewPager;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getData(new VideoLoadCallback() {
                @Override
                public void onLoadCallback(List<Video> list) {
                    viewPager.setAdapter(new VideoSliderAdapter(list, context));
                }
            });
            return null;
        }

        public void getData(VideoLoadCallback callback) {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Activity Videos");
            db.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Video video = dataSnapshot.getValue(Video.class);
                    list.add(video);
                    callback.onLoadCallback(list);
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
}