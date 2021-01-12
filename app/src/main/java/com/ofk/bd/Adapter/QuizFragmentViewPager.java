package com.ofk.bd.Adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ofk.bd.QuizFragmentFragment.QuizQuestionFragment;

import java.util.List;

public class QuizFragmentViewPager extends FragmentStateAdapter {

    private final List<String> questions;

    public QuizFragmentViewPager(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<String> questions) {
        super(fragmentManager, lifecycle);
        this.questions = questions;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("question", questions.get(position));
        bundle.putInt("total_questions", questions.size());
        bundle.putInt("currentPosition", position);
        Fragment quizFragment = new QuizQuestionFragment();
        quizFragment.setArguments(bundle);
        return quizFragment;
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }
}
