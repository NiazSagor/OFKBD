package com.ofk.bd.QuizFragmentFragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ofk.bd.HelperClass.Common;
import com.ofk.bd.R;
import com.ofk.bd.Utility.DrawableUtility;
import com.ofk.bd.ViewModel.CourseActivityViewModel;
import com.ofk.bd.databinding.FragmentQuizQuestionBinding;

import java.util.ArrayList;
import java.util.List;

public class QuizQuestionFragment extends Fragment {

    public QuizQuestionFragment() {
        // Required empty public constructor
    }

    private static final String TAG = "QuizQuestionFragment";

    public boolean CLICKABLE = true;
    public int CORRECT_INDEX = 0;


    private FragmentQuizQuestionBinding binding;
    private List<String> options;
    private CourseActivityViewModel courseActivityViewModel;
    private final TextView[] textViews = new TextView[4];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        courseActivityViewModel = ViewModelProviders.of(getActivity()).get(CourseActivityViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentQuizQuestionBinding.inflate(getLayoutInflater());

        binding.question.setText(getArguments().getString("question"));

        textViews[0] = binding.choice1;
        textViews[1] = binding.choice2;
        textViews[2] = binding.choice3;
        textViews[3] = binding.choice4;

        courseActivityViewModel.currentOptionPosition().observe(getActivity(), currentPositionObserver);

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        courseActivityViewModel.getQuizOptions(
                getActivity().getIntent().getStringExtra("course_name_english"),
                getActivity().getIntent().getStringExtra("section_name"),
                getArguments().getString("question")
        ).observe(getActivity(), quizOptionLiveData);
    }

    @Override
    public void onResume() {
        super.onResume();

        courseActivityViewModel.getQuizOptions(
                getActivity().getIntent().getStringExtra("course_name_english"),
                getActivity().getIntent().getStringExtra("section_name"),
                getArguments().getString("question")
        ).observe(getActivity(), quizOptionLiveData);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private final Observer<Integer> currentPositionObserver = new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            StringBuilder stringBuilder = new StringBuilder();

            int totalQuestions = getArguments().getInt("total_questions");

            if (integer < totalQuestions) {
                stringBuilder.append("Swipe up for next question ");
            }

            if (integer == totalQuestions) {
                stringBuilder.append("Last question of the quiz ");
            }

            stringBuilder.append(integer).append("/").append(totalQuestions);
            binding.questionRemainingTextView.setText(stringBuilder);
        }
    };


    private final Observer<List<String>> quizOptionLiveData = new Observer<List<String>>() {
        @Override
        public void onChanged(List<String> strings) {
            if (strings != null && strings.size() != 0) {

                options = new ArrayList<>(strings);

                for (int i = 0; i < 4; i++) {
                    if (options.get(4).equals(options.get(i))) {
                        CORRECT_INDEX = i;
                    }
                    textViews[i].setText(options.get(i));
                    textViews[i].setOnClickListener(optionOnClickListener);
                }

                binding.progressBar.setVisibility(View.GONE);
            }
        }
    };

    private final View.OnClickListener optionOnClickListener = new View.OnClickListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {

            String correctOption = options.get(4);

            switch (view.getId()) {

                case R.id.choice1:
                    if (CLICKABLE) {
                        textViews[0].setTextColor(Color.WHITE);

                        if (textViews[0].getText().equals(correctOption)) {
                            textViews[0].setBackground(DrawableUtility.getQuizOptionRightDrawable(getContext()));
                            courseActivityViewModel.getRightAnswerCount().setValue(Common.CORRECT_ANSWER_COUNT++);
                        } else {
                            textViews[0].setBackground(DrawableUtility.getQuizOptionWrongDrawable(getContext()));
                            textViews[CORRECT_INDEX].setBackground(DrawableUtility.getQuizOptionRightDrawable(getContext()));
                            textViews[CORRECT_INDEX].setTextColor(Color.WHITE);
                        }

                        CLICKABLE = false;
                    }
                    break;

                case R.id.choice2:
                    if (CLICKABLE) {

                        textViews[1].setTextColor(Color.WHITE);
                        if (textViews[1].getText().equals(correctOption)) {
                            textViews[1].setBackground(DrawableUtility.getQuizOptionRightDrawable(getContext()));
                            courseActivityViewModel.getRightAnswerCount().setValue(Common.CORRECT_ANSWER_COUNT++);
                        } else {
                            textViews[1].setBackground(DrawableUtility.getQuizOptionWrongDrawable(getContext()));
                            textViews[CORRECT_INDEX].setBackground(DrawableUtility.getQuizOptionRightDrawable(getContext()));
                            textViews[CORRECT_INDEX].setTextColor(Color.WHITE);
                        }

                        CLICKABLE = false;
                    }
                    break;

                case R.id.choice3:
                    if (CLICKABLE) {

                        textViews[2].setTextColor(Color.WHITE);
                        if (textViews[2].getText().equals(correctOption)) {
                            textViews[2].setBackground(DrawableUtility.getQuizOptionRightDrawable(getContext()));
                            courseActivityViewModel.getRightAnswerCount().setValue(Common.CORRECT_ANSWER_COUNT++);
                        } else {
                            textViews[2].setBackground(DrawableUtility.getQuizOptionWrongDrawable(getContext()));
                            textViews[CORRECT_INDEX].setBackground(DrawableUtility.getQuizOptionRightDrawable(getContext()));
                            textViews[CORRECT_INDEX].setTextColor(Color.WHITE);
                        }

                        CLICKABLE = false;
                    }
                    break;

                case R.id.choice4:
                    if (CLICKABLE) {

                        textViews[3].setTextColor(Color.WHITE);
                        if (textViews[3].getText().equals(correctOption)) {
                            textViews[3].setBackground(DrawableUtility.getQuizOptionRightDrawable(getContext()));
                            courseActivityViewModel.getRightAnswerCount().setValue(Common.CORRECT_ANSWER_COUNT++);
                        } else {
                            textViews[3].setBackground(DrawableUtility.getQuizOptionWrongDrawable(getContext()));
                            textViews[CORRECT_INDEX].setBackground(DrawableUtility.getQuizOptionRightDrawable(getContext()));
                            textViews[CORRECT_INDEX].setTextColor(Color.WHITE);
                        }

                        CLICKABLE = false;
                    }
                    break;
            }
        }
    };
}