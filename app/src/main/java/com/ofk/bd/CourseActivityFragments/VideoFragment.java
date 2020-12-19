package com.ofk.bd.CourseActivityFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.ofk.bd.CourseActivityAdapter.CourseSectionAdapter;
import com.ofk.bd.HelperClass.Common;
import com.ofk.bd.ViewModel.CourseActivityViewModel;
import com.ofk.bd.databinding.FragmentVideoBinding;

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
        // Inflate the layout for this fragment
        binding = FragmentVideoBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (Common.sectionVideoList != null && Common.sectionVideoList.size() != 0) {
            courseActivityViewModel.getCurrentVideoFromList().setValue(Common.sectionVideoList.get(0).getVideos().get(0).getVideoURL());
            CourseSectionAdapter adapter = new CourseSectionAdapter(getActivity(), Common.sectionVideoList);
            binding.sectionListRecyclerView.setAdapter(adapter);
            binding.progressBar.setVisibility(View.GONE);
        }
    }
}