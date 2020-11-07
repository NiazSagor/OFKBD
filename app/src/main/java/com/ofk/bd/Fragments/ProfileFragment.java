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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.ofk.bd.Adapter.ProfileListAdapter;
import com.ofk.bd.HelperClass.UserInfo;
import com.ofk.bd.InfoActivity;
import com.ofk.bd.R;
import com.ofk.bd.Utility.StringUtility;
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
    private FragmentProfileBinding binding;

    private List<Integer> acquiredBadgeIndexes;
    private SharedPreferences sharedPreferences;

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
        binding.profileRecyclerView.setHasFixedSize(true);

        return binding.getRoot();
    }

    private final Observer<Integer> currentBadgeIndex = new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            if (integer != null) {
                binding.currentBadgeImageViewTop.setImageResource(badge_icons[integer]);
            }
        }
    };

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

    private final ProfileListAdapter.OnItemClickListener onItemClickListener = new ProfileListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position, View view) {
            if (position == 7) {
                FirebaseAuth.getInstance().signOut();

                getActivity().startActivity(new Intent(getActivity(), InfoActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP));

                getActivity().finish();
            }
        }
    };
    private final ProfileListAdapter.OnItemEditListener onItemEditListener = new ProfileListAdapter.OnItemEditListener() {

        @Override
        public void onItemClick(int position, View view, String text) {
            if (position == 2) {
                // email
                mainActivityViewModel.updateUserInfo(text, "email");
            } else if (position == 3) {
                // class
                mainActivityViewModel.updateUserInfo(text, "class");
            } else if (position == 4) {
                // institute
                mainActivityViewModel.updateUserInfo(text, "institute");
            } else if (position == 5) {
                // dob
                mainActivityViewModel.updateUserInfo(text, "dob");
            } else if (position == 6) {
                // gender
                mainActivityViewModel.updateUserInfo(text, "gender");
            }
            hideKeyboardFrom();
        }
    };
    private final Observer<UserInfo> userInfoObserver = new Observer<UserInfo>() {
        @Override
        public void onChanged(UserInfo userInfo) {
            if (userInfo != null) {

                binding.userNameTextView.setText(userInfo.getUserName());

                binding.totalCourseCompleted.setText(StringUtility.getSting(userInfo.getCourseCompleted()));
                binding.totalVideoCompleted.setText(StringUtility.getSting(userInfo.getVideoCompleted()));
                binding.totalQuizCompleted.setText(StringUtility.getSting(userInfo.getQuizCompleted()));

                populateUserInfoList(userInfo);
            }
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int index = sharedPreferences.getInt("avatarIndex", 0);

        binding.avatarImageView.setBackgroundResource(avatars[index]);

        mainActivityViewModel.getCurrentIndexOnBadge().observe(this, currentBadgeIndex);
    }

    private void populateUserInfoList(UserInfo userInfo) {

        List<String> profileItemList = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                profileItemList.add("" + userInfo.getUserName());
                profileItemList.add("" + userInfo.getUserPhoneNumber());

                profileItemList.add(userInfo.getUserEmail().equals("") ? "" : userInfo.getUserEmail());
                profileItemList.add(userInfo.getUserClass().equals("") ? "" : userInfo.getUserClass());
                profileItemList.add(userInfo.getUserSchool().equals("") ? "" : userInfo.getUserSchool());
                profileItemList.add(userInfo.getUserDOB().equals("") ? "" : userInfo.getUserDOB());
                profileItemList.add(userInfo.getUserGender().equals("") ? "" : userInfo.getUserGender());
                profileItemList.add("Log Out");
            }
        }).start();

        ProfileListAdapter adapter = new ProfileListAdapter(profileItemList, getContext());

        binding.profileRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(onItemClickListener);
        // editable fields in the list
        adapter.setOnItemEditListener(onItemEditListener);
    }

    public void hideKeyboardFrom() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
        View view = getActivity().getCurrentFocus();
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}