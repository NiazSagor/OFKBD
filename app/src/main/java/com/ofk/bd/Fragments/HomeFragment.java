package com.ofk.bd.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.ofk.bd.Adapter.CourseSliderAdapter;
import com.ofk.bd.Adapter.CourseSliderListAdapter;
import com.ofk.bd.HelperClass.Course;
import com.ofk.bd.R;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater());

        createDummyCourse();

        adapter = new CourseSliderAdapter(getContext(), courseList);
        binding.courseViewPager.setAdapter(adapter);

        recommendedCourseAdapter = new CourseSliderListAdapter(recommendedCourseList, "random");

        binding.randomCourseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.randomCourseRecyclerView.setAdapter(recommendedCourseAdapter);

        changeIndicator(0);

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

        final String[] list = {"প্রথম শ্রেণি", "দ্বিতীয় শ্রেণি"};

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        binding.spinnerClass.setAdapter(dataAdapter);

        binding.spinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //binding.spinnerClass.setSelection(i);
                if (i != 0) {
                    Log.d(TAG, "onItemSelected: " + i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
}