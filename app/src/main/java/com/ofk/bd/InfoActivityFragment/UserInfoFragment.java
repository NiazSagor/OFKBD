package com.ofk.bd.InfoActivityFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ofk.bd.Adapter.AgeListAdapter;
import com.ofk.bd.Adapter.AvatarListAdapter;
import com.ofk.bd.MainActivity;
import com.ofk.bd.ViewModel.InfoActivityViewModel;
import com.ofk.bd.databinding.FragmentUserInfoBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserInfoFragment extends Fragment {

    private static final String TAG = "UserInfoFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserInfoFragment newInstance(String param1, String param2) {
        UserInfoFragment fragment = new UserInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private InfoActivityViewModel activityViewModel;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        activityViewModel = ViewModelProviders.of(this).get(InfoActivityViewModel.class);
    }

    private FragmentUserInfoBinding binding;

    private String userName = null;
    private String userAge = null;
    private String userEmail = "";
    private int userAvatar = 10;

    private static String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private AgeListAdapter ageListAdapter;

    private AvatarListAdapter avatarListAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserInfoBinding.inflate(getLayoutInflater());

        binding.ageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        ageListAdapter = new AgeListAdapter();

        binding.ageRecyclerView.setAdapter(ageListAdapter);

        binding.avatarRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));

        avatarListAdapter = new AvatarListAdapter("choose_avatar");

        binding.avatarRecyclerView.setAdapter(avatarListAdapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.progressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.yourNameEditText.getText().toString().trim().isEmpty()) {
                    binding.yourNameEditText.setError("তোমার নাম লেখো নি");
                } else if (!binding.yourEmailEditText.getText().toString().trim().isEmpty()) {
                    String email = binding.yourEmailEditText.getText().toString().trim();
                    if (email.matches(emailPattern)) {
                        userEmail = email;
                    } else {
                        binding.yourEmailEditText.setError("ইমেইল এড্রেসটি সঠিক হয় নি");
                    }
                } else {
                    userName = binding.yourNameEditText.getText().toString().trim();

                    Log.d(TAG, "onClick: " + userName);

                    if (userAge != null && userAvatar >= 0 && userAvatar <= 7) {
                        // inset user to db
                        insertUser();

                        // and start main activity
                        getActivity().startActivity(new Intent(getActivity(), MainActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP));


                    } else {
                        Toast.makeText(getContext(), "তোমার বয়স এবং এভাটার সিলেক্ট করো", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        ageListAdapter.setOnItemClickListener(new AgeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                userAge = ageListAdapter.getSelectedAge(position);
                Log.d(TAG, "onItemClick: " + userAge);
                //sharedPreferences.edit().putInt("ageIndex", position).apply();
            }
        });

        avatarListAdapter.setOnItemClickListener(new AvatarListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                userAvatar = position;
                Log.d(TAG, "onItemClick: " + userAvatar);
                //sharedPreferences.edit().putInt("avatarIndex", position).apply();
            }
        });
    }

    private void insertUser() {
        Log.d(TAG, "insertUser: ");
        activityViewModel.updateUserInfo(userName, userEmail,"");
    }
}