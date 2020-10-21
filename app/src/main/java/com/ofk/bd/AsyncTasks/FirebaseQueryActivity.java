package com.ofk.bd.AsyncTasks;

import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ofk.bd.HelperClass.Activity;
import com.ofk.bd.Interface.ActivityPicLoadCallback;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FirebaseQueryActivity extends AsyncTask<Void, Void, Void> {

    private final ActivityPicLoadCallback callback;
    private final String mNode;
    private final List<Activity> activityList;
    private final Picasso mPicasso;

    public FirebaseQueryActivity(ActivityPicLoadCallback callback, String node) {
        this.callback = callback;
        this.mNode = node;
        activityList = new ArrayList<>();
        mPicasso = Picasso.get();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child(mNode);
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Activity activity = dataSnapshot.getValue(Activity.class);

                mPicasso.load(activity.getUrl()).fetch();
                activityList.add(activity);
                callback.onPicLoadCallback(activityList);
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
