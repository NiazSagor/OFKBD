package com.ofk.bd.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.ofk.bd.Adapter.CourseSliderListAdapter;
import com.ofk.bd.Adapter.MoreCourseSliderAdapter;
import com.ofk.bd.HelperClass.Course;
import com.ofk.bd.R;
import com.ofk.bd.databinding.FragmentMoreBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoreFragment extends Fragment {

    private static final String TAG = "MoreFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MoreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MoreFragment newInstance(String param1, String param2) {
        MoreFragment fragment = new MoreFragment();
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

    private FragmentMoreBinding binding;

    private List<Course> courseResources;
    private List<Course> moreCourses;
    private List<Course> tutorials;

    private ViewPager.OnPageChangeListener mOnPageChangeListener;

    private int customPosition = 0;

    private int[] indicator = {R.drawable.dot, R.drawable.inactivedot};

    private MoreCourseSliderAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMoreBinding.inflate(getLayoutInflater());

        createDummyCourseResources();

        adapter = new MoreCourseSliderAdapter(getActivity(), moreCourses);
        binding.courseViewPager.setClipToPadding(false);
        binding.courseViewPager.setPageMargin(20);
        binding.courseViewPager.setAdapter(adapter);

        binding.resourceRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        binding.resourceRecyclerView.setAdapter(new CourseSliderListAdapter(courseResources, "course_resource"));

        binding.tutorialsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.tutorialsRecyclerView.setAdapter(new CourseSliderListAdapter(tutorials, "tutorial"));

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

    private void createDummyCourseResources() {

        courseResources = new ArrayList<>();

        moreCourses = new ArrayList<>();

        tutorials = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            courseResources.add(new Course("Resource " + i));
            moreCourses.add(new Course("Course " + i, "Subtitle " + i));
            tutorials.add(new Course("Tutorial " + i, "Subtitle " + i));
        }
    }
}