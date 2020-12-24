package com.ofk.bd.Fragments;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.ofk.bd.Adapter.ProfileListAdapter;
import com.ofk.bd.Model.UserInfo;
import com.ofk.bd.InfoActivity;
import com.ofk.bd.Utility.DrawableUtility;
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
public class ProfileFragment extends Fragment implements ProfileBottomSheet.BottomSheetListener {
    private static final String TAG = "ProfileFragment";


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private final ProfileBottomSheet bottomSheet = new ProfileBottomSheet();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int mYear, mMonth, mDay;

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
                binding.currentBadgeImageViewTop.setImageDrawable(DrawableUtility.getDrawable(getContext(), integer));
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
            } else if (position == 5) {
                //dob
                Bundle bundle = new Bundle();
                bundle.putString("type", "date");
                bottomSheet.setArguments(bundle);
                bottomSheet.show(getFragmentManager(), "");
            } else if (position == 3) {
                //class
                Bundle bundle = new Bundle();
                bundle.putString("type", "class");
                bottomSheet.setArguments(bundle);
                bottomSheet.show(getFragmentManager(), "");
            } else if (position == 6) {
                Bundle bundle = new Bundle();
                bundle.putString("type", "gender");
                bottomSheet.setArguments(bundle);
                bottomSheet.show(getFragmentManager(), "");
            }
        }
    };
    private final ProfileListAdapter.OnItemEditListener onItemEditListener = new ProfileListAdapter.OnItemEditListener() {

        @Override
        public void onItemClick(int position, View view, String text) {
            if (position == 2) {
                // email
                mainActivityViewModel.updateUserInfo(text, "email");
            } else if (position == 4) {
                // institute
                mainActivityViewModel.updateUserInfo(text, "institute");
            }
            hideKeyboardFrom();
        }
    };
    private final Observer<UserInfo> userInfoObserver = new Observer<UserInfo>() {
        @Override
        public void onChanged(UserInfo userInfo) {
            if (userInfo != null) {

                binding.userNameTextView.setText(userInfo.getUserName());

                binding.totalCourseCompleted.setText(StringUtility.getString(userInfo.getCourseCompleted()));
                binding.totalVideoCompleted.setText(StringUtility.getString(userInfo.getVideoCompleted()));
                binding.totalQuizCompleted.setText(StringUtility.getString(userInfo.getQuizCompleted()));

                populateUserInfoList(userInfo);

                if(StringUtility.isUserInfoComplete(userInfo) && !sharedPreferences.getBoolean("isPosted", false)){
                    // call to api to push data to google sheets
                    userInfo.setFirebaseUid(FirebaseAuth.getInstance().getUid());
                    mainActivityViewModel.postData(userInfo);
                    sharedPreferences.edit().putBoolean("isPosted", true).apply();
                }
            }
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int index = sharedPreferences.getInt("avatarIndex", 0);

        binding.avatarImageView.setImageDrawable(DrawableUtility.getAvatarDrawable(getContext(), index));

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

    @Override
    public void onButtonClicked(String text) {
        Log.d(TAG, "onButtonClicked: ");
        switch (bottomSheet.getArguments().getString("type")) {
            case "gender":
                // gender
                Toast.makeText(getContext(), "" + text, Toast.LENGTH_SHORT).show();
                //mainActivityViewModel.updateUserInfo(text, "gender");
                break;
            case "date":
                //dob
                Toast.makeText(getContext(), "" + text, Toast.LENGTH_SHORT).show();
                //mainActivityViewModel.updateUserInfo(text, "dob");
                break;
            case "class":
                //class
                Toast.makeText(getContext(), "" + text, Toast.LENGTH_SHORT).show();
                //mainActivityViewModel.updateUserInfo(text, "class");
                break;
        }
    }
}