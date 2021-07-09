package com.ofk.bd.CourseActivityFragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;

import com.ofk.bd.Adapter.QuizFragmentViewPager;
import com.ofk.bd.Utility.AlertDialogUtility;
import com.ofk.bd.ViewModel.CourseActivityViewModel;
import com.ofk.bd.databinding.FragmentQuizBinding;

import java.util.List;

public class QuizFragment extends Fragment {

    private static final String TAG = "QuizFragment";

    private Handler handler;

    public QuizFragment() {
        // Required empty public constructor
    }

    private FragmentQuizBinding binding;

    private FragmentManager fragmentManager;

    private CourseActivityViewModel courseActivityViewModel;

    private int TOTAL_QUESTIONS = 0;
    private final ViewPager2.OnPageChangeCallback callback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            Log.d(TAG, "onPageSelected: ");
            if (position == TOTAL_QUESTIONS - 1) {
                Log.d(TAG, "onPageSelected: last question");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        courseActivityViewModel.getRightAnswerCount().observe(getActivity(), new Observer<Integer>() {
                            @Override
                            public void onChanged(Integer integer) {
                                Log.d(TAG, "onChanged: dialog");
                                new AlertDialogUtility().showQuizCompletionDialog(getContext(), integer, TOTAL_QUESTIONS);
                            }
                        });
                    }
                }, 3000);
            }

            courseActivityViewModel.currentOptionPosition().setValue(position + 1);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentQuizBinding.inflate(getLayoutInflater());

        fragmentManager = getChildFragmentManager();

        String courseName = getActivity().getIntent().getStringExtra("course_name_english");
        String sectionName = getActivity().getIntent().getStringExtra("section_name");

        courseActivityViewModel.getQuizQuestions(courseName, sectionName).observe(getActivity(), quizQuestionsLiveData);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private final Observer<List<String>> quizQuestionsLiveData = new Observer<List<String>>() {
        @Override
        public void onChanged(List<String> strings) {
            if (strings != null && strings.size() != 0) {
                binding.progressBar.setVisibility(View.GONE);

                QuizFragmentViewPager adapter = new QuizFragmentViewPager(fragmentManager, getLifecycle(), strings);
                binding.quizViewPager.setUserInputEnabled(true);
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new Handler();

        if (courseActivityViewModel == null) {
            courseActivityViewModel = ViewModelProviders.of(getActivity()).get(CourseActivityViewModel.class);
        }
    }
}