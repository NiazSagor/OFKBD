package com.ofk.bd.Fragments;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.ofk.bd.Adapter.AvatarListAdapter;
import com.ofk.bd.Adapter.ProfileListAdapter;
import com.ofk.bd.HelperClass.UserInfo;
import com.ofk.bd.InfoActivity;
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
    private static final String TAG = "ProfileFragment";

    private static final int[] avatars = {R.drawable.ic_dog, R.drawable.ic_duck, R.drawable.ic_fox, R.drawable.ic_lion, R.drawable.ic_cat, R.drawable.ic_tiger, R.drawable.ic_squirrel, R.drawable.ic_giraffe, R.drawable.ic_elephant, R.drawable.ic_parrot};

    private static final int[] badge_icons = {R.drawable.apprentice_1, R.drawable.apprentice_2, R.drawable.apprentice_3,
            R.drawable.journeyman_1, R.drawable.journeyman_2, R.drawable.journeyman_3,
            R.drawable.master_1, R.drawable.master_2, R.drawable.master_3,
            R.drawable.grand_master_1, R.drawable.grand_master_2, R.drawable.grand_master_3,
            R.drawable.super_kids_1, R.drawable.super_kids_2, R.drawable.super_kids_3};
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
    private List<String> profileItemList;
    private FragmentProfileBinding binding;

    private List<Integer> acquiredBadgeIndexes;
    private SharedPreferences sharedPreferences;

    private AvatarListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        if (acquiredBadgeIndexes == null) {
            acquiredBadgeIndexes = new ArrayList<>();
        }
        if (profileItemList == null) {
            profileItemList = new ArrayList<>();
        }
        if (mainActivityViewModel == null) {
            mainActivityViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(getLayoutInflater());

        mainActivityViewModel.getUserInfoLiveData().observe(this, userInfoObserver);
        //mainActivityViewModel.getCurrentIndexOnBadge().observe(this, indexObserver);

        //binding.badgesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int index = sharedPreferences.getInt("avatarIndex", 0);

        binding.avatarImageView.setBackgroundResource(avatars[index]);

        mainActivityViewModel.getCurrentIndexOnBadge().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer != null) {
                    binding.currentBadgeImageViewTop.setImageResource(badge_icons[integer]);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
/*
        if (!mainActivityViewModel.getUserInfoLiveData2().hasObservers()) {
            mainActivityViewModel.getUserInfoLiveData2().observe(getActivity(), userInfoObserver);
        }

        if (!mainActivityViewModel.getCurrentIndexOnBadge().hasObservers()) {
            mainActivityViewModel.getCurrentIndexOnBadge().observe(getActivity(), indexObserver);
        }*/
    }

    private final Observer<UserInfo> userInfoObserver = new Observer<UserInfo>() {
        @Override
        public void onChanged(UserInfo userInfo) {
            if (userInfo != null) {

                binding.userNameTextView.setText(userInfo.getUserName());

                binding.totalCourseCompleted.setText("" + userInfo.getCourseCompleted());
                binding.totalVideoCompleted.setText("" + userInfo.getVideoCompleted());
                binding.totalQuizCompleted.setText("" + userInfo.getQuizCompleted());

                profileItemList.add("" + userInfo.getUserName());
                profileItemList.add("" + userInfo.getUserPhoneNumber());

                if (!userInfo.getUserEmail().equals("")) {
                    profileItemList.add("" + userInfo.getUserEmail());
                } else {
                    profileItemList.add("");//email
                    profileItemList.add("");//class
                    profileItemList.add("");//institute
                    profileItemList.add("");//bd
                    profileItemList.add("");//gender
                }

                profileItemList.add("Log Out");

                ProfileListAdapter adapter = new ProfileListAdapter(profileItemList);

                binding.profileRecyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(new ProfileListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View view) {
                        if (position == 7) {
                            FirebaseAuth.getInstance().signOut();

                            getActivity().startActivity(new Intent(getActivity(), InfoActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP));

                            getActivity().finish();
                        }
                    }
                });

                adapter.setOnItemEditListener(new ProfileListAdapter.OnItemEditListener() {
                    @Override
                    public void onItemClick(int position, View view, String text) {
                        hideKeyboardFrom();
                    }
                });
            }
        }
    };

    /* recycler view for acquired badges
    private final Observer<Integer> indexObserver = new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            if (integer != null) {
                for (int i = 0; i <= integer; i++) {
                    acquiredBadgeIndexes.add(i);
                }

                adapter = new AvatarListAdapter("view_badge");

                adapter.setAcquiredBadgeIndexes(acquiredBadgeIndexes);

                binding.badgesRecyclerView.setAdapter(adapter);
            }
        }
    };
     */
    public void hideKeyboardFrom() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
        View view = getActivity().getCurrentFocus();
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}