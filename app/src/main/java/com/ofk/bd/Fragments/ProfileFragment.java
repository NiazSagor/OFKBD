package com.ofk.bd.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ofk.bd.Adapter.AvatarListAdapter;
import com.ofk.bd.Adapter.ProfileListAdapter;
import com.ofk.bd.R;
import com.ofk.bd.ViewModel.MainActivityViewModel;
import com.ofk.bd.databinding.FragmentProfileBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private MainActivityViewModel mainActivityViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mainActivityViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
    }

    private FragmentProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(getLayoutInflater());

        binding.badgesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.badgesRecyclerView.setAdapter(new AvatarListAdapter("view_badge"));

        setupUserSummary();

        List<String> profileItemList = new ArrayList<>();

        profileItemList.add("Niaz Sagor");
        profileItemList.add("+8801799018683");
        profileItemList.add("niazsagor@gmail.com");
        profileItemList.add("Sign Out");

        binding.profileRecyclerView.setAdapter(new ProfileListAdapter(profileItemList));

        return binding.getRoot();
    }

    private void setupUserSummary() {
        String courseCompleted = "" + mainActivityViewModel.getCourseCompletedInTotal();
        String videoWatched = "" + mainActivityViewModel.getVideoWatchedInTotal();
        String quizCompleted = "" + mainActivityViewModel.getQuizCompletedInTotal();
        binding.totalCourseCompleted.setText(courseCompleted);
        binding.totalVideoCompleted.setText(videoWatched);
        binding.totalQuizCompleted.setText(quizCompleted);
    }
}