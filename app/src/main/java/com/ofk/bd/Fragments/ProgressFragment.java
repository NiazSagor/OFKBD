package com.ofk.bd.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ofk.bd.Adapter.AvatarListAdapter;
import com.ofk.bd.Adapter.ProgressListAdapter;
import com.ofk.bd.HelperClass.BadgeUtilityClass;
import com.ofk.bd.HelperClass.EnrolledCourse;
import com.ofk.bd.HelperClass.SectionCourseTuple;
import com.ofk.bd.R;
import com.ofk.bd.ViewModel.MainActivityViewModel;
import com.ofk.bd.databinding.FragmentProgressBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProgressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProgressFragment extends Fragment {

    private static final String TAG = "ProgressFragment";

    private static final int[] badge_icons = {R.drawable.apprentice_1, R.drawable.apprentice_2, R.drawable.apprentice_3,
            R.drawable.journeyman_1, R.drawable.journeyman_2, R.drawable.journeyman_3,
            R.drawable.master_1, R.drawable.master_2, R.drawable.master_3,
            R.drawable.grand_master_1, R.drawable.grand_master_2, R.drawable.grand_master_3,
            R.drawable.super_kids_1, R.drawable.super_kids_2, R.drawable.super_kids_3};

    private static final String[] level_names = {"Apprentice 1", "Apprentice 2", "Apprentice 3",
            "Journeyman 1", "Journeyman 2", "Journeyman 3",
            "Master 1", "Master 2", "Master 3",
            "Grand Master 1", "Grand Master 2", "Grand Master 3",
            "Super Kids 1", "Super Kids 2", "Super Kids 3"};

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProgressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProgressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProgressFragment newInstance(String param1, String param2) {
        ProgressFragment fragment = new ProgressFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private MainActivityViewModel mainActivityViewModel;
    private List<EnrolledCourse> enrolledCourses;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        if (enrolledCourses == null) {
            enrolledCourses = new ArrayList<>();
        }
        if (mainActivityViewModel == null) {
            mainActivityViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
        }
    }

    private FragmentProgressBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProgressBinding.inflate(getLayoutInflater());
        //new TestClass().execute();

        return binding.getRoot();
    }


    @Override
    public void onStart() {
        super.onStart();

        if (!mainActivityViewModel.getUserCourseCompleted().hasObservers()) {
            mainActivityViewModel.getUserCourseCompleted().observe(this, userCourseCompletedObserver);
        }

        if (!mainActivityViewModel.getCombinedList().hasObservers()) {
            mainActivityViewModel.getCombinedList().observe(this, listObserver);
        }
    }

    private final Observer<Integer> userCourseCompletedObserver = new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            int current;
            if (integer == null) {
                current = 0;
            } else {
                current = integer;
            }

            if (current < 38) {
                BadgeUtilityClass badge = new BadgeUtilityClass(current);

                int currentBadgeIconIndex = badge.getCurrentBadgeIconIndex();

                setUpcomingBadgeList(currentBadgeIconIndex);

                int nextBadgeIconIndex = currentBadgeIconIndex + 1;

                mainActivityViewModel.getCurrentIndexOnBadge().setValue(currentBadgeIconIndex);

                String currentLevelExtended = " " + level_names[currentBadgeIconIndex];

                String currentLevelOnly = level_names[currentBadgeIconIndex];
                String nextLevelOnly = level_names[nextBadgeIconIndex];

                binding.currentLevelTextViewTop.setText(currentLevelExtended);
                binding.currentLevelTextViewBelow.setText(currentLevelOnly);
                binding.nextLevelTextViewBelow.setText(nextLevelOnly);

                binding.currentBadge.setImageResource(badge_icons[currentBadgeIconIndex]);
                binding.nextBadge.setImageResource(badge_icons[nextBadgeIconIndex]);

                if (level_names[currentBadgeIconIndex].contains("Apprentice")) {

                } else if (level_names[currentBadgeIconIndex].contains("Journeyman")) {

                } else if (level_names[currentBadgeIconIndex].contains("Master")) {

                } else if (level_names[currentBadgeIconIndex].contains("Grandmaster")) {
                } else if (level_names[currentBadgeIconIndex].contains("Super Kids")) {
                }

            } else if (current == 38) {
                String currentLevel = " Super Kids 3";

                binding.currentLevelTextViewTop.setText(currentLevel);

                binding.currentBadge.setImageResource(badge_icons[14]);

            } else if (current == 0) {
                binding.line1.setVisibility(View.GONE);
                binding.earnedBadgeTextView.setVisibility(View.GONE);
                binding.earnedBadgesRecyclerView.setVisibility(View.GONE);
                setUpcomingBadgeList(0);
            } else if (current <= 4) {
                binding.line1.setVisibility(View.GONE);
                binding.earnedBadgeTextView.setVisibility(View.GONE);
                binding.earnedBadgesRecyclerView.setVisibility(View.GONE);
                setUpcomingBadgeList(0);
            }
        }
    };

    private void setUpcomingBadgeList(int currentBadgeIconIndex) {
        AvatarListAdapter adapter = new AvatarListAdapter("view_badge");
        adapter.setCurrentBadgeIndex(currentBadgeIconIndex);
        binding.upcomingBadgesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.upcomingBadgesRecyclerView.setAdapter(adapter);
    }

    private final Observer<List<SectionCourseTuple>> listObserver = new Observer<List<SectionCourseTuple>>() {
        @Override
        public void onChanged(List<SectionCourseTuple> sectionCourseTuples) {
            if (sectionCourseTuples != null) {
                binding.courseProgressTextView.setVisibility(View.VISIBLE);
                binding.subjectProgressRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                binding.subjectProgressRecyclerView.setAdapter(new ProgressListAdapter(sectionCourseTuples));
            } else {
                binding.courseProgressTextView.setVisibility(View.GONE);
            }
        }
    };
}