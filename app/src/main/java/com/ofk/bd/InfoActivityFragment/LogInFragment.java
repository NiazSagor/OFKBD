package com.ofk.bd.InfoActivityFragment;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ofk.bd.Model.UserInfo;
import com.ofk.bd.Model.UserProgress;
import com.ofk.bd.Interface.CheckUserCallback;
import com.ofk.bd.MainActivity;
import com.ofk.bd.Utility.AlertDialogUtility;
import com.ofk.bd.Utility.CheckUserDatabase;
import com.ofk.bd.ViewModel.InfoActivityViewModel;
import com.ofk.bd.ViewModel.MainActivityViewModel;
import com.ofk.bd.databinding.FragmentLogInBinding;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogInFragment extends Fragment {

    private static final String TAG = "LogInFragment";

    private SharedPreferences sharedPreferences;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LogInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LogInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LogInFragment newInstance(String param1, String param2) {
        LogInFragment fragment = new LogInFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private InfoActivityViewModel activityViewModel;
    private MainActivityViewModel mainActivityViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        sharedPreferences = getActivity().getSharedPreferences("isVerified", Context.MODE_PRIVATE);

        activityViewModel = ViewModelProviders.of(getActivity()).get(InfoActivityViewModel.class);
    }

    private FragmentLogInBinding binding;

    private AlertDialogUtility dialogUtility = new AlertDialogUtility();

    private String userPhoneNumber = null;
    private String userPassword = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLogInBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activityViewModel.getUserPhoneNumberLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    Log.d(TAG, "onChanged: " + s);
                    binding.phoneNumberEditText.setText(s);
                }
            }
        });

        binding.passwordEditText.addTextChangedListener(watcher);

        binding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.phoneNumberEditText.getText().toString().trim().isEmpty()) {

                    if (binding.phoneNumberEditText.getText().toString().trim().length() < 11) {
                        binding.phoneNumberEditText.setError("সঠিক নাম্বার দাও নি");
                    } else {
                        if (!binding.phoneNumberEditText.getText().toString().trim().contains("+88")) {
                            userPhoneNumber = "+88" + binding.phoneNumberEditText.getText().toString().trim();
                        } else {
                            userPhoneNumber = binding.phoneNumberEditText.getText().toString().trim();
                        }
                    }

                } else {
                    binding.phoneNumberEditText.setError("মোবাইল নাম্বার দাও নি");
                }

                //Log.d(TAG, "onClick: " + userPhoneNumber);

                if (!binding.passwordEditText.getText().toString().trim().isEmpty()) {

                    if (binding.passwordEditText.getText().toString().trim().length() < 6) {
                        binding.passwordEditText.setError("৬ ক্যারেক্টার হয়নি");
                    } else {
                        userPassword = binding.passwordEditText.getText().toString().trim();
                    }

                } else {
                    binding.passwordEditText.setError("পাসওয়ার্ড দাও নি");
                }

                if (userPassword != null && userPhoneNumber != null) {
                    dialogUtility.showAlertDialog(getContext(), "start");
                    checkDatabase(userPhoneNumber, userPassword);
                }
            }
        });
    }

    private void checkDatabase(String userPhoneNumber, String userPassword) {
        new CheckUserDatabase(new CheckUserCallback() {
            @Override
            public void onUserCheckCallback(boolean isExist, String message) {

                dialogUtility.dismissAlertDialog();

                if (isExist && message.equals("match")) {

                    sharedPreferences.edit().putBoolean("isVerified", true).apply();

                    getActivity().startActivity(new Intent(getActivity(), MainActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    getActivity().finish();

                } else if (isExist && message.equals("misMatch")) {
                    Toast.makeText(getContext(), "পাসওয়ার্ড সঠিক হয় নি", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "ইউজার একাউন্ট নেই", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onUserFoundCallback(UserInfo userInfoCloud, List<UserProgress> userProgressList) {

                Toast.makeText(getContext(), "Welcome Back!", Toast.LENGTH_SHORT).show();

                activityViewModel.getUserInfoMutableLiveData().observe(getActivity(), new Observer<UserInfo>() {
                    @Override
                    public void onChanged(UserInfo userInfo) {
                        if (userInfo == null) {

                            activityViewModel.insert(userInfoCloud);

                            if (userProgressList != null && userProgressList.size() != 0) {
                                for (UserProgress userProgress : userProgressList) {
                                    activityViewModel.insertUserProgress(userProgress);
                                }
                            }
                        }
                    }
                });
            }
        }, userPhoneNumber, userPassword).execute();
    }

    private final TextWatcher watcher = new TextWatcher() {
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

    public void hideKeyboardFrom() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.phoneNumberEditText.getWindowToken(), 0);
    }

    /* allowBackUp will be true so we don't need to fetch user data from firebase//TODO
    UserForFirebase user = dataSnapshot.child(userPhoneNumber).getValue(UserForFirebase.class);
    assert user != null;
                    activityViewModel.updateUserInfo(user.getUserName(), user.getUserEmail(), user.getUserPhoneNumber());*/
}