package com.ofk.bd.AsyncTasks;

import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ofk.bd.HelperClass.Video;
import com.ofk.bd.Interface.VideoLoadCallback;

import java.util.ArrayList;
import java.util.List;

public class FirebaseQueryVideo extends AsyncTask<Void, Void, Void> {

    private VideoLoadCallback callback;
    private List<Video> activityList;
    private String mNode;

    public FirebaseQueryVideo(VideoLoadCallback callback, String mNode) {
        this.callback = callback;
        this.mNode = mNode;
        activityList = new ArrayList<>();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child(mNode);
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Video video = dataSnapshot.getValue(Video.class);
                activityList.add(video);
                callback.onLoadCallback(activityList);
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
