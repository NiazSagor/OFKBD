package com.ofk.bd.InfoActivityFragment;

import android.app.Service;
import android.graphics.Color;
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
import androidx.viewpager2.widget.ViewPager2;

import com.developer.kalert.KAlertDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.ofk.bd.Interface.CheckUserCallback;
import com.ofk.bd.Model.UserInfo;
import com.ofk.bd.Model.UserProgress;
import com.ofk.bd.R;
import com.ofk.bd.Utility.CheckUserDatabase;
import com.ofk.bd.ViewModel.InfoActivityViewModel;
import com.ofk.bd.databinding.FragmentInputNumberBinding;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InputNumberFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InputNumberFragment extends Fragment {

    private static final String TAG = "InputNumberFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InputNumberFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InputNumberFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InputNumberFragment newInstance(String param1, String param2) {
        InputNumberFragment fragment = new InputNumberFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private InfoActivityViewModel activityViewModel;
    private FirebaseAuth mAuth;
    private KAlertDialog pDialog;

    private Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null) {
            Log.d(TAG, "not authenticated");
            //Toast.makeText(getContext(), "User is not authenticated", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "authenticated" + mAuth.getCurrentUser().getPhoneNumber());
        }

        activityViewModel = ViewModelProviders.of(getActivity()).get(InfoActivityViewModel.class);
    }

    private FragmentInputNumberBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentInputNumberBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    private String userPhoneNumber = null;
    private ViewPager2 viewPager2;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager2 = getActivity().findViewById(R.id.viewpager);

        binding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.phoneNumberEditText.getText().toString().trim().isEmpty()) {

                    if (binding.phoneNumberEditText.getText().toString().trim().length() < 11) {
                        binding.phoneNumberEditText.setError("সঠিক নাম্বার দাও নি");
                    } else {
                        userPhoneNumber = "+88" + binding.phoneNumberEditText.getText().toString().trim();

                        hideKeyboardFrom();

                        showAlertDialog("start");

                        sendVerificationCode(userPhoneNumber);
                    }

                } else {
                    binding.phoneNumberEditText.setError("মোবাইল নাম্বার দাও নি");
                }
            }
        });

        binding.phoneNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 11) {
                    hideKeyboardFrom();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void sendVerificationCode(String phone) {
        Log.d(TAG, "sendVerificationCode: ");
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(requireActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallback)          // OnVerificationStateChangedCallbacks
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            //signInWithCredential(phoneAuthCredential);//TODO research
            Log.d(TAG, "onVerificationCompleted: ");
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            if (s != null) {
                Log.d(TAG, "onCodeSent: " + s);
                activityViewModel.getVerificationCode().setValue(s);
                showAlertDialog("end");
                viewPager2.setCurrentItem(1, true);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.d(TAG, "onVerificationFailed: " + e.getMessage());
        }
    };

    // auto verification
    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    showAlertDialog("done");

                    new CheckUserDatabase(new CheckUserCallback() {
                        @Override
                        public void onUserCheckCallback(boolean isExist, String message) {

                            if (isExist) {
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        showAlertDialog("end");
                                        viewPager2.setCurrentItem(3, true);
                                    }
                                }, 1500);
                            } else {
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        showAlertDialog("end");
                                        viewPager2.setCurrentItem(2, true);
                                    }
                                }, 1500);
                            }

                        }

                        @Override
                        public void onUserFoundCallback(UserInfo userInfoCloud, List<UserProgress> userProgressList) {

                        }
                    }, userPhoneNumber, "");
                } else {
                    Toast.makeText(getContext(), "Not successful " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showAlertDialog(String command) {
        switch (command) {
            case "start":
                pDialog = new KAlertDialog(getActivity(), KAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#00c1c3"));
                pDialog.setTitleText("অপেক্ষা করো");
                pDialog.setCancelable(false);
                pDialog.show();
                break;
            case "end":
                pDialog.dismissWithAnimation();
                break;
            case "done":
                pDialog.dismiss();
                pDialog = new KAlertDialog(getActivity(), KAlertDialog.SUCCESS_TYPE);
                pDialog.setTitleText("ভেরিফিকেশন সম্পন্ন হয়েছে")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                            @Override
                            public void onClick(KAlertDialog kAlertDialog) {
                                pDialog.dismissWithAnimation();
                            }
                        })
                        .show();
                break;
        }
    }

    public void hideKeyboardFrom() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.phoneNumberEditText.getWindowToken(), 0);
    }
}