package com.ofk.bd.Utility;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ofk.bd.Model.UserInfo;
import com.ofk.bd.Model.UserProgress;
import com.ofk.bd.Interface.CheckUserCallback;

import java.util.ArrayList;
import java.util.List;

public class CheckUserDatabase extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "CheckUserDatabase";

    private final CheckUserCallback callback;
    private final String userPhoneNumber;
    private final String userPassword;
    private List<UserProgress> userProgressList;

    public static final DatabaseReference USER_REF = FirebaseDatabase.getInstance().getReference("User");
    public static final DatabaseReference USER_PROGRESS_REF = FirebaseDatabase.getInstance().getReference("User Progress");

    public CheckUserDatabase(CheckUserCallback callback, String userPhoneNumber, String userPassword) {
        this.callback = callback;
        this.userPhoneNumber = userPhoneNumber;
        this.userPassword = userPassword;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        userProgressList = new ArrayList<>();

        USER_REF.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    if (dataSnapshot.child(userPhoneNumber).exists()) {

                        String pass = dataSnapshot.child(userPhoneNumber).child("userPassword").getValue(String.class);

                        UserInfo user = dataSnapshot.child(userPhoneNumber).getValue(UserInfo.class);

                        if (userPassword.equals(pass)) {

                            callback.onUserCheckCallback(true, "match");

                            USER_PROGRESS_REF.child(userPhoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            UserProgress userProgress = snapshot.getValue(UserProgress.class);
                                            userProgressList.add(userProgress);
                                        }
                                        callback.onUserFoundCallback(user, userProgressList);
                                    } else {
                                        callback.onUserFoundCallback(user, userProgressList);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        } else {
                            callback.onUserCheckCallback(true, "misMatch");
                        }
                    } else {
                        callback.onUserCheckCallback(false, "null");
                    }
                } else {
                    callback.onUserCheckCallback(false, "null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return null;
    }
}
