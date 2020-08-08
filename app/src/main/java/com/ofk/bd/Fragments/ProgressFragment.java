package com.ofk.bd.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ofk.bd.Adapter.ProgressListAdapter;
import com.ofk.bd.HelperClass.BadgeUtilityClass;
import com.ofk.bd.HelperClass.EnrolledCourse;
import com.ofk.bd.HelperClass.Progress;
import com.ofk.bd.HelperClass.SectionCourseTuple;
import com.ofk.bd.HelperClass.UserInfo;
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

    private static int badge_icons[] = {R.drawable.apprentice_1, R.drawable.apprentice_2, R.drawable.apprentice_3,
            R.drawable.journeyman_1, R.drawable.journeyman_2, R.drawable.journeyman_3,
            R.drawable.master_1, R.drawable.master_2, R.drawable.master_3,
            R.drawable.grand_master_1, R.drawable.grand_master_2, R.drawable.grand_master_3,
            R.drawable.super_kids_1, R.drawable.super_kids_2, R.drawable.super_kids_3};

    private static String level_names[] = {"Apprentice 1", "Apprentice 2", "Apprentice 3",
            "Journeyman 1", "Journeyman 2", "Journeyman 3",
            "Master 1", "Master 2", "Master 3",
            "Grand Master 1", "Grand Master 2", "Grand Master 3",
            "Super Kids 1", "Super Kids 2", "Super Kids 3"};

    private static int min_require_next_level[] = {1, 2, 3, 4, 7, 9, 11, 14, 17, 20, 23, 26, 28, 33, 38};

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

        enrolledCourses = new ArrayList<>();

        mainActivityViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
    }

    private FragmentProgressBinding binding;
    long total = 0;

    List<String> stringList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProgressBinding.inflate(getLayoutInflater());

        //createDummyProgress();

        stringList = new ArrayList<>();

        mainActivityViewModel.getCombinedList().observe(this, new Observer<List<SectionCourseTuple>>() {
            @Override
            public void onChanged(List<SectionCourseTuple> sectionCourseTuples) {
                if (sectionCourseTuples.size() != 0) {
                    binding.courseProgressTextView.setVisibility(View.VISIBLE);
                    binding.subjectProgressRecyclerView.setAdapter(new ProgressListAdapter(sectionCourseTuples));
                } else {
                    binding.courseProgressTextView.setVisibility(View.GONE);
                }
            }
        });

        //new TestClass().execute();

        getLevel();

        return binding.getRoot();
    }

    private void getLevel() {

        mainActivityViewModel.getUserInfoLiveData().observe(this, new Observer<UserInfo>() {
            @Override
            public void onChanged(UserInfo userInfo) {
                int current = userInfo.getCourseCompleted();
                Log.d(TAG, "getLevel: " + current);

                if (current < 38) {
                    BadgeUtilityClass badge = new BadgeUtilityClass(current);

                    int currentBadgeIconIndex = badge.getCurrentBadgeIconIndex();
                    int nextBadgeIconIndex = currentBadgeIconIndex + 1;

                    mainActivityViewModel.getCurrentIndexOnBadge().setValue(currentBadgeIconIndex);

                    String currentLevelExtended = "Current Level : " + level_names[currentBadgeIconIndex];

                    String currentLevelOnly = level_names[currentBadgeIconIndex];

                    binding.currentLevelTextViewTop.setText(currentLevelExtended);
                    binding.currentBadgeImageViewTop.setImageResource(badge_icons[currentBadgeIconIndex]);

                    binding.currentLevelTextViewBelow.setText(currentLevelOnly);
                    binding.currentBadgeImageViewBelow.setImageResource(badge_icons[currentBadgeIconIndex]);

                    binding.nextLevelTextView.setText(level_names[nextBadgeIconIndex]);
                    binding.nextBadgeImageView.setImageResource(badge_icons[nextBadgeIconIndex]);

                    int maxProgressPossible = min_require_next_level[nextBadgeIconIndex] - min_require_next_level[currentBadgeIconIndex];

                    binding.progressBar.setMax(maxProgressPossible);

                    int currentProgress = min_require_next_level[nextBadgeIconIndex] - current;

                    Log.d(TAG, "getLevel: " + currentProgress);

                    if (maxProgressPossible - currentProgress == 0) {
                        binding.progressBar.setProgress(0);

                        String progressText = currentProgress + "/" + maxProgressPossible;

                        binding.progressTextView.setText(progressText);
                    } else {
                        int diff = min_require_next_level[nextBadgeIconIndex] - current;
                        int progress = maxProgressPossible - diff;

                        String progressText = progress + "/" + maxProgressPossible;

                        binding.progressTextView.setText(progressText);
                        binding.progressBar.setProgress(progress);
                    }
                } else if (current == 38) {
                    String currentLevel = "Current Level : Super Kids 3";
                    String currentLevelShort = "Super Kids 3";

                    binding.currentLevelTextViewTop.setText(currentLevel);
                    binding.currentLevelTextViewBelow.setText(currentLevelShort);
                    binding.currentBadgeImageViewTop.setImageResource(badge_icons[14]);
                    binding.currentBadgeImageViewBelow.setImageResource(badge_icons[14]);
                    binding.nextBadgeImageView.setImageResource(badge_icons[14]);
                    binding.nextLevelTextView.setText(level_names[14]);
                    binding.progressTextView.setVisibility(View.GONE);
                } else if (current == 0) {

                }
            }
        });
    }

    public class TestClass extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Sub Section");
            db.child("Arts Section").child("Arter Hatekhori").child("Video")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                total = total + ds.getChildrenCount();
                            }
                            Log.d(TAG, "onDataChange: " + total);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

            return null;
        }
    }
}