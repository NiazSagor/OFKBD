package com.ofk.bd.InfoActivityFragment;

import android.app.Service;
import android.graphics.Color;
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
import androidx.viewpager2.widget.ViewPager2;

import com.developer.kalert.KAlertDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mukesh.OnOtpCompletionListener;
import com.ofk.bd.R;
import com.ofk.bd.ViewModel.InfoActivityViewModel;
import com.ofk.bd.databinding.FragmentInputOtpBinding;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InputOtpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InputOtpFragment extends Fragment {

    private static final String TAG = "InputOtpFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InputOtpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InputOtpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InputOtpFragment newInstance(String param1, String param2) {
        InputOtpFragment fragment = new InputOtpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private InfoActivityViewModel activityViewModel;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mAuth = FirebaseAuth.getInstance();
        activityViewModel = ViewModelProviders.of(getActivity()).get(InfoActivityViewModel.class);
    }

    private FragmentInputOtpBinding binding;
    private ViewPager2 viewPager2;
    private KAlertDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentInputOtpBinding.inflate(getLayoutInflater());
        viewPager2 = getActivity().findViewById(R.id.viewpager);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideKeyboardFrom();

                showAlertDialog("start");

                String otp = binding.otpView.getText().toString();

                activityViewModel.getVerificationCode().observe(getActivity(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        Log.d(TAG, "onChanged: " + s);
                        Log.d(TAG, "onChanged: " + otp);
                        verifyCode(s, otp);
                    }
                });
            }
        });
    }

    private void verifyCode(String verificationID, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    showAlertDialog("done");
                    FirebaseUser user = mAuth.getCurrentUser();
                    assert user != null;
                    activityViewModel.getUserPhoneNumberLiveData().setValue(user.getPhoneNumber());
                    viewPager2.setCurrentItem(2, true);
                    //Toast.makeText(getContext(), "Successful" + user.getPhoneNumber(), Toast.LENGTH_SHORT).show();
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
        imm.hideSoftInputFromWindow(binding.otpView.getWindowToken(), 0);
    }
}