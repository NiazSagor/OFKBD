package com.ofk.bd.Utility;

import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ofk.bd.HelperClass.Common;
import com.ofk.bd.HelperClass.UserForFirebase;
import com.ofk.bd.Interface.CheckUserCallback;

public class CheckUserDatabase extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "CheckUserDatabase";
    
    private final CheckUserCallback callback;
    private final String userPhoneNumber;
    private final String userPassword;
    public static final DatabaseReference db = FirebaseDatabase.getInstance().getReference("User");

    public CheckUserDatabase(CheckUserCallback callback, String userPhoneNumber, String userPassword) {
        this.callback = callback;
        this.userPhoneNumber = userPhoneNumber;
        this.userPassword = userPassword;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    if (dataSnapshot.child(userPhoneNumber).exists()) {

                        String pass = dataSnapshot.child(userPhoneNumber).child("userPassword").getValue(String.class);

                        UserForFirebase user = dataSnapshot.child(userPhoneNumber).getValue(UserForFirebase.class);

                        if (userPassword.equals(pass)) {
                            callback.onUserCheckCallback(true, "match");
                            callback.onUserFoundCallback(user);
                        } else {
                            callback.onUserCheckCallback(true, "misMatch");
                        }
                    } else {
                        callback.onUserCheckCallback(false, "null");
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return null;
    }
}
