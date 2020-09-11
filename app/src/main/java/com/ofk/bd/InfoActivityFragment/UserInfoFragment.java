package com.ofk.bd.InfoActivityFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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

import com.developer.kalert.KAlertDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ofk.bd.Adapter.AvatarListAdapter;
import com.ofk.bd.HelperClass.UserForFirebase;
import com.ofk.bd.HelperClass.UserInfo;
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

    private FirebaseAuth mAuth;
    private InfoActivityViewModel activityViewModel;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mAuth = FirebaseAuth.getInstance();

        sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        activityViewModel = ViewModelProviders.of(this).get(InfoActivityViewModel.class);
    }

    private FragmentUserInfoBinding binding;

    private String userName = null;
    private String userPassword = null;
    private String userEmail = "";
    private int userAvatar = 20;

    private static String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private AvatarListAdapter avatarListAdapter;
    private KAlertDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserInfoBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Handler handler = new Handler();

        binding.avatarRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));

        avatarListAdapter = new AvatarListAdapter("choose_avatar");

        binding.avatarRecyclerView.setAdapter(avatarListAdapter);

        binding.progressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

                        showAlertDialog("start");

                        // inset user to db
                        insertUser();

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showAlertDialog("end");

                                getActivity().startActivity(new Intent(getActivity(), MainActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            }
                        }, 2000);

                        // and start main activity

                    } else {
                        Toast.makeText(getContext(), "এভাটার সিলেক্ট করো", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        avatarListAdapter.setOnItemClickListener(new AvatarListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                userAvatar = position;
                Log.d(TAG, "onItemClick: " + userAvatar);
                sharedPreferences.edit().putInt("avatarIndex", position).apply();
            }
        });
    }

    private void insertUser() {
        Log.d(TAG, "insertUser: ");

        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        assert firebaseUser != null;

        UserInfo userInfo = new UserInfo(userName, firebaseUser.getPhoneNumber(), userEmail, 0, 0, 0);
        activityViewModel.insert(userInfo);

        UserForFirebase user = new UserForFirebase(userEmail, userName, userPassword, firebaseUser.getPhoneNumber());

        new InsertUserToDb(user).execute();
    }

    private static class InsertUserToDb extends AsyncTask<Void, Void, Void> {

        UserForFirebase user;

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("User");

        public InsertUserToDb(UserForFirebase user) {
            this.user = user;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            db.child(user.getUserPhoneNumber()).setValue(user);

            return null;
        }
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
}