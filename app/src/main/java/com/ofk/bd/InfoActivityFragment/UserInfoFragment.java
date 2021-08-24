package com.ofk.bd.InfoActivityFragment;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.ofk.bd.Adapter.AvatarListAdapter;
import com.ofk.bd.Model.UserInfo;
import com.ofk.bd.MainActivity;
import com.ofk.bd.R;
import com.ofk.bd.Utility.AlertDialogUtility;
import com.ofk.bd.ViewModel.InfoActivityViewModel;
import com.ofk.bd.databinding.FragmentUserInfoBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserInfoFragment extends Fragment implements View.OnFocusChangeListener {

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

    private FirebaseAuth mAuth;
    private InfoActivityViewModel activityViewModel;
    private SharedPreferences sharedPreferences;
    private static final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private final TextWatcher passwordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.length() == 6) {
                hideKeyboardFrom();
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private FragmentUserInfoBinding binding;

    private String userName = null;
    private String userPassword = null;
    private String userEmail = "";
    private int userAvatar = 20;
    private final AvatarListAdapter.OnItemClickListener avatarOnItemClickListener = new AvatarListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position, View view) {
            if (avatarListAdapter.isClickable) {
                userAvatar = position;
                sharedPreferences.edit().putInt("avatarIndex", position).apply();
                avatarListAdapter.isClickable = false;
            } else {
                Toast.makeText(getContext(), getResources().getString(R.string.avatarSelected), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private AvatarListAdapter avatarListAdapter;
    private Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserInfoBinding.inflate(getLayoutInflater());
        binding.yourEmailEditText.setOnFocusChangeListener(this);
        return binding.getRoot();
    }

    private AlertDialogUtility dialogUtility;
    private final View.OnClickListener progressButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mAuth.getCurrentUser() == null) {
                Toast.makeText(getContext(), getResources().getString(R.string.notVerified), Toast.LENGTH_SHORT).show();
                return;
            }

            if (!binding.yourEmailEditText.getText().toString().trim().isEmpty()) {
                String email = binding.yourEmailEditText.getText().toString().trim();
                if (email.matches(emailPattern)) {
                    userEmail = email;
                } else {
                    binding.yourEmailEditText.setError("ইমেইল এড্রেসটি সঠিক হয় নি");
                }
            }

            if (binding.yourNameEditText.getText().toString().trim().isEmpty()) {
                binding.yourNameEditText.setError("তোমার নাম লেখো নি");
            } else if (binding.password.getText().toString().trim().isEmpty()) {
                binding.password.setError("পাসওয়ার্ড দাও নি");
            } else {
                userName = binding.yourNameEditText.getText().toString().trim();
                userPassword = binding.password.getText().toString().trim();

                Log.d(TAG, "onClick: " + userName);

                if (userPassword != null && userAvatar >= 0 && userAvatar <= 10) {

                    dialogUtility.showAlertDialog(getContext(), "wait");

                    // inset user to db
                    insertUser();

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            dialogUtility.dismissAlertDialog();

                            getActivity().startActivity(new Intent(getActivity(), MainActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP));

                            getActivity().finish();
                        }
                    }, 2000);

                } else {
                    Toast.makeText(getContext(), "এভাটার সিলেক্ট করো", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mAuth = FirebaseAuth.getInstance();

        handler = new Handler();

        sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        activityViewModel = ViewModelProviders.of(this).get(InfoActivityViewModel.class);

        dialogUtility = new AlertDialogUtility();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.avatarRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));

        avatarListAdapter = new AvatarListAdapter(getContext(), "choose_avatar");

        binding.avatarRecyclerView.setAdapter(avatarListAdapter);

        avatarListAdapter.setOnItemClickListener(avatarOnItemClickListener);

        binding.password.addTextChangedListener(passwordTextWatcher);

        binding.progressButton.setOnClickListener(progressButtonListener);
    }

    private void insertUser() {
        Log.d(TAG, "insertUser: ");

        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        assert firebaseUser != null;

        UserInfo userInfo = new UserInfo(0, 0, "", "", userEmail, "", userName, userPassword, firebaseUser.getPhoneNumber(), "", 0);
        activityViewModel.insert(userInfo);
        FirebaseMessaging.getInstance().subscribeToTopic("All Students");
    }

    public void hideKeyboardFrom() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.password.getWindowToken(), 0);
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (!b) {
            hideKeyboardFrom();
        }
    }
}