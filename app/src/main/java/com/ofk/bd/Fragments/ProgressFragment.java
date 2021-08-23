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
import com.ofk.bd.HelperClass.SectionCourseTuple;
import com.ofk.bd.Utility.DrawableUtility;
import com.ofk.bd.Utility.StringUtility;
import com.ofk.bd.ViewModel.MainActivityViewModel;
import com.ofk.bd.databinding.FragmentProgressBinding;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProgressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProgressFragment extends Fragment {

    private static final String TAG = "ProgressFragment";

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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

                mainActivityViewModel.getCurrentIndexOnBadge().setValue(currentBadgeIconIndex);

                binding.currentLevelTextViewTop.setText(StringUtility.getCurrentLevelName(currentBadgeIconIndex));
                binding.currentLevelTextViewBelow.setText(StringUtility.getCurrentLevelName(currentBadgeIconIndex));
                binding.nextLevelTextViewBelow.setText(StringUtility.getCurrentLevelName(currentBadgeIconIndex + 1));

                binding.currentBadge.setImageDrawable(DrawableUtility.getDrawable(getContext(), currentBadgeIconIndex));
                binding.nextBadge.setImageDrawable(DrawableUtility.getDrawable(getContext(), currentBadgeIconIndex + 1));
            } else if (current == 38) {
                String currentLevel = " Super Kids 3";

                binding.currentLevelTextViewTop.setText(currentLevel);

                binding.currentBadge.setImageDrawable(DrawableUtility.getDrawable(getContext(), 14));

                binding.nextLevelTextViewBelow.setVisibility(View.GONE);

                binding.gotoNextLevel.setVisibility(View.GONE);

                binding.lineMiddle.setVisibility(View.GONE);

                binding.nextBadge.setVisibility(View.GONE);
            } else if (current == 0) {
                binding.line1.setVisibility(View.GONE);
                binding.line2.setVisibility(View.GONE);
                binding.earnedBadgeTextView.setVisibility(View.GONE);
                binding.earnedBadgesRecyclerView.setVisibility(View.GONE);
                setUpcomingBadgeList(0);
            } else if (current <= 4) {
                binding.line1.setVisibility(View.GONE);
                binding.line2.setVisibility(View.GONE);
                binding.earnedBadgeTextView.setVisibility(View.GONE);
                binding.earnedBadgesRecyclerView.setVisibility(View.GONE);
                setUpcomingBadgeList(0);
            }
        }
    };
    private final Observer<List<SectionCourseTuple>> listObserver = new Observer<List<SectionCourseTuple>>() {
        @Override
        public void onChanged(List<SectionCourseTuple> sectionCourseTuples) {
            if (sectionCourseTuples.size() >= 1) {
                binding.courseProgressTextView.setVisibility(View.VISIBLE);
                binding.subjectProgressRecyclerView.setVisibility(View.VISIBLE);
                binding.subjectProgressRecyclerView.setHasFixedSize(true);
                binding.subjectProgressRecyclerView.setAdapter(new ProgressListAdapter(sectionCourseTuples));
            } else {
                binding.courseProgressTextView.setVisibility(View.GONE);
                binding.subjectProgressRecyclerView.setVisibility(View.GONE);
            }
        }
    };

    private void setUpcomingBadgeList(int currentBadgeIconIndex) {
        AvatarListAdapter adapter = new AvatarListAdapter(getContext(), "view_badge");
        adapter.setCurrentBadgeIndex(currentBadgeIconIndex);
        binding.upcomingBadgesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.upcomingBadgesRecyclerView.setAdapter(adapter);
    }
}