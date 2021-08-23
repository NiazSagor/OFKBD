package com.ofk.bd.CourseActivityFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ofk.bd.CourseActivityAdapter.CourseSectionAdapter;
import com.ofk.bd.Model.SectionVideo;
import com.ofk.bd.Utility.AlertDialogUtility;
import com.ofk.bd.ViewModel.CourseActivityViewModel;
import com.ofk.bd.databinding.FragmentVideoBinding;

import java.util.List;
import java.util.Objects;

public class VideoFragment extends Fragment {

    private static final String TAG = "VideoFragment";

    public VideoFragment() {
        // Required empty public constructor
    }

    private CourseActivityViewModel courseActivityViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        courseActivityViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(CourseActivityViewModel.class);
    }

    private FragmentVideoBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentVideoBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();


        courseActivityViewModel.getSectionWithVideos(
                Objects.requireNonNull(getActivity()).getIntent().getStringExtra("section_name") + " Section",
                getActivity().getIntent().getStringExtra("course_name_english")
        ).observe(this, sectionVideoObserver);

        if (getActivity().getIntent().getBooleanExtra("isNewCourse", false)) {
            courseActivityViewModel.getTotalVideosOnTakenCourse().observe(this, totalVideosOnTakenCourseObserver);
        }
    }

    private final androidx.lifecycle.Observer<List<SectionVideo>> sectionVideoObserver = new Observer<List<SectionVideo>>() {
        @Override
        public void onChanged(List<SectionVideo> sectionVideoList) {
            if (sectionVideoList != null && sectionVideoList.size() != 0) {
                courseActivityViewModel.getCurrentVideoFromList().setValue(sectionVideoList.get(0).getVideos().get(0).getUrl());
                binding.sectionListRecyclerView.setAdapter(
                        new CourseSectionAdapter(getActivity(), sectionVideoList)
                );
                binding.progressBar.setVisibility(View.GONE);
            } else {
                new AlertDialogUtility().showAlertDialog(getActivity(), "videoNotFound");
            }
        }
    };

    private final Observer<Integer> totalVideosOnTakenCourseObserver = new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            if (integer != null)
                courseActivityViewModel.updateTotalVideoCourse(
                        Objects.requireNonNull(getActivity()).getIntent().getStringExtra("course_name_english"), integer
                );
        }
    };
}