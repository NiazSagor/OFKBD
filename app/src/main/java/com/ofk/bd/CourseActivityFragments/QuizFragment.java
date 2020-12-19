package com.ofk.bd.CourseActivityFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.database.DataSnapshot;
import com.ofk.bd.Adapter.QuizFragmentViewPager;
import com.ofk.bd.Utility.AlertDialogUtility;
import com.ofk.bd.ViewModel.CourseActivityViewModel;
import com.ofk.bd.databinding.FragmentQuizBinding;

import java.util.ArrayList;
import java.util.List;

public class QuizFragment extends Fragment {

    private static final String TAG = "QuizFragment";

    public QuizFragment() {
        // Required empty public constructor
    }

    private FragmentQuizBinding binding;

    private CourseActivityViewModel courseActivityViewModel;

    private static List<String> quizQuestionList;

    private int TOTAL_QUESTIONS = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (courseActivityViewModel == null) {
            courseActivityViewModel = ViewModelProviders.of(getActivity()).get(CourseActivityViewModel.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentQuizBinding.inflate(getLayoutInflater());

        String courseName = getActivity().getIntent().getStringExtra("course_name_english");
        String sectionName = getActivity().getIntent().getStringExtra("section_name");

        courseActivityViewModel.getQuizQuestions(courseName, sectionName).observe(getActivity(), quizQuestionsLiveData);
        courseActivityViewModel.getIsLastQuestion().observe(getActivity(), isLastQuestionLiveData);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private final Observer<DataSnapshot> quizQuestionsLiveData = new Observer<DataSnapshot>() {
        @Override
        public void onChanged(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {

                quizQuestionList = new ArrayList<>();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String question = ds.getKey();
                    quizQuestionList.add(question);
                }

                binding.progressBar.setVisibility(View.GONE);

                QuizFragmentViewPager adapter = new QuizFragmentViewPager(getChildFragmentManager(), getLifecycle(), quizQuestionList);
                binding.quizViewPager.setUserInputEnabled(true);
                binding.quizViewPager.setOffscreenPageLimit(3);
                binding.quizViewPager.setAdapter(adapter);
                binding.quizViewPager.registerOnPageChangeCallback(callback);
                TOTAL_QUESTIONS = adapter.getItemCount();
            } else {
                binding.quizViewPager.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.GONE);
                binding.errorMessage.setVisibility(View.VISIBLE);
            }
        }
    };

    private final ViewPager2.OnPageChangeCallback callback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            courseActivityViewModel.currentOptionPosition().setValue(position + 1);
        }
    };

    private final Observer<Boolean> isLastQuestionLiveData = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean aBoolean) {

            Log.d(TAG, "onChanged: " + aBoolean);

            AlertDialogUtility dialogUtility = new AlertDialogUtility();

            // if last question
            if (aBoolean) {
                courseActivityViewModel.getRightAnswerCount().observe(getActivity(), new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        dialogUtility.showQuizCompletionDialog(getContext(), integer, TOTAL_QUESTIONS);
                    }
                });
            }
        }
    };
}