package com.ofk.bd.Utility;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ofk.bd.HelperClass.UserForFirebase;

public class FirebaseWriteUtility {

    // user db address
    private static final DatabaseReference db = FirebaseDatabase.getInstance().getReference("User");

    // push user info to database
    public static void insertUser(UserForFirebase user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.child(user.getUserPhoneNumber()).setValue(user);
            }
        }).start();
    }
}
