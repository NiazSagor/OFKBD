package com.ofk.bd.SearchActivityFragment;

import android.content.Context;
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
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ofk.bd.CourseActivityAdapter.SectionVideoAdapter;
import com.ofk.bd.Model.Video;
import com.ofk.bd.ViewModel.FirebaseSearchViewModel;
import com.ofk.bd.databinding.FragmentSearchVideoBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchVideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchVideoFragment extends Fragment {

    private static final String TAG = "SearchVideoFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchVideoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchVideoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchVideoFragment newInstance(String param1, String param2) {
        SearchVideoFragment fragment = new SearchVideoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private SectionVideoAdapter adapter;
    private FirebaseSearchViewModel viewModel;
    private FragmentSearchVideoBinding binding;
    private DatabaseReference db_ref_all_video;
    private List<Video> videoList;
    private String searchQuery;
    private RecyclerView searchVideoRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db_ref_all_video.removeEventListener(mListener);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = ViewModelProviders.of(getActivity()).get(FirebaseSearchViewModel.class);
        viewModel.getMutableLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.d(TAG, "onChanged: " + s);
                searchQuery = s;
            }
        });

        videoList = new ArrayList<>();

        db_ref_all_video = FirebaseDatabase.getInstance().getReference().child("All Video");
        db_ref_all_video.keepSynced(true);
        db_ref_all_video.addChildEventListener(mListener);
    }

    private ChildEventListener mListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Video video = dataSnapshot.getValue(Video.class);
            if (video.getVideoTitle().toLowerCase().contains(searchQuery.toLowerCase())) {
                videoList.add(video);
                adapter = new SectionVideoAdapter(videoList, "videoSearch");
                searchVideoRecyclerView.setAdapter(adapter);

                if (adapter.getItemCount() != 0) {
                    binding.notMatchTextView.setVisibility(View.GONE);
                    viewModel.getFoundVideoCount().setValue(adapter.getItemCount());
                    Log.d(TAG, "onChildAdded: " + adapter.getItemCount());
                } else {
                    Log.d(TAG, "onChildAdded: 0");
                    binding.notMatchTextView.setVisibility(View.VISIBLE);
                    viewModel.getFoundVideoCount().setValue(0);
                }
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
}