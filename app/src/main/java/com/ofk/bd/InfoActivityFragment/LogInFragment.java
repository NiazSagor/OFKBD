package com.ofk.bd.InfoActivityFragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ofk.bd.HelperClass.UserForFirebase;
import com.ofk.bd.MainActivity;
import com.ofk.bd.ViewModel.InfoActivityViewModel;
import com.ofk.bd.databinding.FragmentLogInBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogInFragment extends Fragment {

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        activityViewModel = ViewModelProviders.of(this).get(InfoActivityViewModel.class);
    }

    private FragmentLogInBinding binding;

    private EditText phoneNumberEditText, passwordEditText;

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

        binding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.phoneNumberEditText.getText().toString().trim().isEmpty()) {

                    if (binding.phoneNumberEditText.getText().toString().trim().length() < 11) {
                        binding.phoneNumberEditText.setError("সঠিক নাম্বার দাও নি");
                    } else {
                        userPhoneNumber = binding.phoneNumberEditText.getText().toString().trim();
                    }

                } else {
                    binding.phoneNumberEditText.setError("মোবাইল নাম্বার দাও নি");
                }

                if (!binding.passwordEditText.getText().toString().trim().isEmpty()) {

                    if (binding.passwordEditText.getText().toString().trim().length() < 6) {
                        binding.passwordEditText.setError("৬ ক্যারেক্টার হয়নি");
                    } else {
                        userPassword = binding.phoneNumberEditText.getText().toString().trim();
                    }

                } else {
                    binding.passwordEditText.setError("পাসওয়ার্ড দাও নি");
                }

                if (userPassword != null && userPhoneNumber != null) {
                    checkDatabase(userPhoneNumber, userPassword);
                }
            }
        });
    }

    private void checkDatabase(String userPhoneNumber, String userPassword) {
        new CheckUserDatabase(userPhoneNumber, userPassword).execute();
    }

    private class CheckUserDatabase extends AsyncTask<Void, Void, Void> {

        private String userPhoneNumber, userPassword;

        public CheckUserDatabase(String userPhoneNumber, String userPassword) {
            this.userPhoneNumber = userPhoneNumber;
            this.userPassword = userPassword;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference("User");
            db.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (dataSnapshot.exists()) {

                        if (dataSnapshot.child(userPhoneNumber).exists()) {

                            String pass = dataSnapshot.child(userPhoneNumber).child("userPassword").getValue(String.class);

                            if (userPassword.equals(pass)) {
                                UserForFirebase user = dataSnapshot.child(userPhoneNumber).getValue(UserForFirebase.class);
                                assert user != null;
                                activityViewModel.updateUserInfo(user.getUserName(), user.getUserEmail(), user.getUserPhoneNumber());

                                getActivity().startActivity(new Intent(getActivity(), MainActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP));

                            } else {
                                Toast.makeText(getContext(), "পাসওয়ার্ড সঠিক হয় নি", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "ইউজার একাউন্ট নেই", Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            return null;
        }
    }
}