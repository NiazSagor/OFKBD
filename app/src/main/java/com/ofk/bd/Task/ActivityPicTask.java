package com.ofk.bd.Task;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ofk.bd.Model.Activity;
import com.ofk.bd.Interface.ActivityPicLoadCallback;

import java.util.ArrayList;
import java.util.List;

public class ActivityPicTask implements Runnable {

    private static final String TAG = "ActivityPicTask";

    private final List<Activity> activityPhotos = new ArrayList<>();
    private final ActivityPicLoadCallback callback;
    private final String entryNode;

    public ActivityPicTask(ActivityPicLoadCallback callback, String entryNode) {
        this.entryNode = entryNode;
        this.callback = callback;
    }

    @Override
    public void run() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference(entryNode);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        activityPhotos.add(ds.getValue(Activity.class));
                    }

                    callback.onPicLoadCallback(activityPhotos);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
