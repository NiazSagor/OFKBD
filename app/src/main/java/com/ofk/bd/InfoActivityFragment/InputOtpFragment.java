package com.ofk.bd.InfoActivityFragment;

import android.app.Service;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

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
import com.ofk.bd.HelperClass.UserForFirebase;
import com.ofk.bd.Interface.CheckUserCallback;
import com.ofk.bd.R;
import com.ofk.bd.Utility.AlertDialogUtility;
import com.ofk.bd.Utility.CheckUserDatabase;
import com.ofk.bd.ViewModel.InfoActivityViewModel;
import com.ofk.bd.databinding.FragmentInputOtpBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InputOtpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InputOtpFragment extends Fragment {

    private static final String TAG = "InputOtpFragment";

    private Handler handler;

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
    private AlertDialogUtility dialogUtility;

    private FragmentInputOtpBinding binding;
    private ViewPager2 viewPager2;
    private KAlertDialog pDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mAuth = FirebaseAuth.getInstance();
        activityViewModel = ViewModelProviders.of(getActivity()).get(InfoActivityViewModel.class);
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentInputOtpBinding.inflate(getLayoutInflater());
        viewPager2 = getActivity().findViewById(R.id.viewpager);
        dialogUtility = new AlertDialogUtility();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.otpView.addTextChangedListener(new TextWatcher() {
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
        });

        binding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideKeyboardFrom();

                dialogUtility.showAlertDialog(getContext(), "start");

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

                    dialogUtility.dismissAlertDialog();

                    dialogUtility.showAlertDialog(getContext(), "verifyDone");

                    FirebaseUser user = mAuth.getCurrentUser();
                    assert user != null;
                    activityViewModel.getUserPhoneNumberLiveData().setValue(user.getPhoneNumber());

                    new CheckUserDatabase(new CheckUserCallback() {
                        @Override
                        public void onUserCheckCallback(boolean isExist, String message) {
                            if (isExist) {
                                // user has already an account take to login fragment
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialogUtility.dismissAlertDialog();
                                        viewPager2.setCurrentItem(3, true);
                                    }
                                }, 1500);
                            } else {
                                // user does not have an account take to user info fragment
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialogUtility.dismissAlertDialog();
                                        viewPager2.setCurrentItem(2, true);
                                    }
                                }, 1500);
                            }
                        }

                        @Override
                        public void onUserFoundCallback(UserForFirebase user) {
                            //
                        }
                    }, user.getPhoneNumber(), "").execute();

                } else {
                    dialogUtility.dismissAlertDialog();
                    dialogUtility.showAlertDialog(getContext(), "misMatchOtp");
                    binding.otpView.setText("");
                    //Toast.makeText(getContext(), "Not successful " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void hideKeyboardFrom() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.otpView.getWindowToken(), 0);
    }
}