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
import androidx.recyclerview.widget.RecyclerView;

import com.ofk.bd.CourseActivityAdapter.SectionVideoAdapter;
import com.ofk.bd.Model.Video;
import com.ofk.bd.ViewModel.FirebaseSearchViewModel;
import com.ofk.bd.databinding.FragmentSearchVideoBinding;

import java.util.List;


public class SearchVideoFragment extends Fragment {

    private static final String TAG = "SearchVideoFragment";
    private RecyclerView searchVideoRecyclerView;
    private FirebaseSearchViewModel viewModel;
    private FragmentSearchVideoBinding binding;
    private SectionVideoAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(FirebaseSearchViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchVideoBinding.inflate(getLayoutInflater());

        searchVideoRecyclerView = binding.videoSearchResultRecyclerView;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getSearchVideoResult().observe(this, new Observer<List<Video>>() {
            @Override
            public void onChanged(List<Video> videos) {
                adapter = new SectionVideoAdapter(videos, "videoSearch");
                searchVideoRecyclerView.setAdapter(adapter);
                if (videos.size() > 0) {
                    binding.notMatchTextView.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
    }
}