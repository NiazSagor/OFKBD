package com.ofk.bd.SearchActivityFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ofk.bd.DisplayCourseActivityAdapter.CourseListAdapter;
import com.ofk.bd.Model.DisplayCourse;
import com.ofk.bd.ViewModel.FirebaseSearchViewModel;
import com.ofk.bd.databinding.FragmentCourseBinding;

import java.util.List;

public class CourseFragment extends Fragment {

    private FragmentCourseBinding binding;
    private FirebaseSearchViewModel viewModel;
    private RecyclerView courseRecyclerView;
    private CourseListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(FirebaseSearchViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCourseBinding.inflate(getLayoutInflater());

        courseRecyclerView = binding.courseSearchResultRecyclerView;
        courseRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getSearchCourseResult().observe(this, new Observer<List<DisplayCourse>>() {
            @Override
            public void onChanged(List<DisplayCourse> displayCourses) {
                adapter = new CourseListAdapter(displayCourses, "displayCourse");
                courseRecyclerView.setAdapter(adapter);
                if (displayCourses.size() > 0) {
                    binding.notMatchTextView.setVisibility(View.GONE);
                }
            }
        });
    }
}