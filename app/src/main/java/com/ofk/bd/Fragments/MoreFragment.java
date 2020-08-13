package com.ofk.bd.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.database.DataSnapshot;
import com.ofk.bd.Adapter.ActivitySliderAdapter;
import com.ofk.bd.Adapter.CourseSliderListAdapter;
import com.ofk.bd.HelperClass.Activity;
import com.ofk.bd.HelperClass.Course;
import com.ofk.bd.R;
import com.ofk.bd.ViewModel.MainActivityViewModel;
import com.ofk.bd.databinding.FragmentMoreBinding;
import com.thefinestartist.finestwebview.FinestWebView;

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

    private MainActivityViewModel mainActivityViewModel;
    // activity pics list
    private List<Activity> activityPics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        activityPics = new ArrayList<>();
        mainActivityViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
    }

    private FragmentMoreBinding binding;

    private List<Course> courseResources;
    private List<Course> moreCourses;
    private List<Course> tutorials;

    private ViewPager.OnPageChangeListener mOnPageChangeListener;

    private int customPosition = 0;

    private int[] indicator = {R.drawable.dot, R.drawable.inactivedot};

    // activity pics adapter
    private ActivitySliderAdapter activityAdapter;

    private CourseSliderListAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMoreBinding.inflate(getLayoutInflater());

        //adapter = new ActivitySliderAdapter(getActivity(), moreCourses, "our_work");
        binding.activityViewPager.setClipToPadding(false);
        binding.activityViewPager.setPageMargin(20);

        mainActivityViewModel.getFieldWorkPicLiveData().observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(DataSnapshot dataSnapshot) {
                Activity activity = dataSnapshot.getValue(Activity.class);
                activityPics.add(activity);
                activityAdapter = new ActivitySliderAdapter(getContext(), activityPics, "activity");
                binding.activityViewPager.setAdapter(activityAdapter);
                changeIndicator(0);
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
                    if (customPosition > activityAdapter.getCount() - 1) {
                        customPosition = 0;
                    }

                    changeIndicator(i++);
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            };
            binding.activityViewPager.addOnPageChangeListener(mOnPageChangeListener);
        }

        binding.blogRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mainActivityViewModel.getBlogListMutableLiveData().observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {
                adapter = new CourseSliderListAdapter(courses, "blog");
                binding.blogRecyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(new CourseSliderListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View view) {
                        if (position == 0) {
                            new FinestWebView.Builder(getActivity()).show("https://ofkbd.com/inspirational/");
                        } else if (position == 1) {
                            new FinestWebView.Builder(getActivity()).show("https://ofkbd.com/story/");
                        } else if (position == 2) {
                            new FinestWebView.Builder(getActivity()).show("https://ofkbd.com/tips-and-tricks/");
                        } else if (position == 3) {
                            new FinestWebView.Builder(getActivity()).show("https://ofkbd.com/skill-development/");
                        } else if (position == 4) {
                            new FinestWebView.Builder(getActivity()).show("https://ofkbd.com/awarness/");
                        } else if (position == 5) {
                            new FinestWebView.Builder(getActivity()).show("https://ofkbd.com/english-article/");
                        }
                    }
                });
            }
        });

        return binding.getRoot();
    }

    private void changeIndicator(int currentSlidePosition) {
        if (binding.dotLayout.getChildCount() > 0) {
            binding.dotLayout.removeAllViews();
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
            binding.dotLayout.addView(dots[i], layoutParams);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        activityPics.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        activityPics.clear();
    }
}