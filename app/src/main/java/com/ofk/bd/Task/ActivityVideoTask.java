package com.ofk.bd.Task;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ofk.bd.Interface.VideoLoadCallback;
import com.ofk.bd.Model.Video;

import java.util.ArrayList;
import java.util.List;

public class ActivityVideoTask implements Runnable {

    private final List<Video> activityVideos = new ArrayList<>();
    private final VideoLoadCallback callback;

    public ActivityVideoTask(VideoLoadCallback callback) {
        this.callback = callback;
    }

    @Override
    public void run() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Activity Videos");

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        activityVideos.add(ds.getValue(Video.class));
                    }

                    callback.onLoadCallback(activityVideos);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
