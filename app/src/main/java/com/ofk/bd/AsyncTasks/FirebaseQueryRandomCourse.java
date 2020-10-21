package com.ofk.bd.AsyncTasks;

import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ofk.bd.HelperClass.DisplayCourse;
import com.ofk.bd.Interface.DisplayCourseLoadCallback;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FirebaseQueryRandomCourse extends AsyncTask<Void, Void, Void> {

    private final DisplayCourseLoadCallback callback;

    private final String mNode;

    private final List<DisplayCourse> courseList;

    private Picasso mPicasso;

    public FirebaseQueryRandomCourse(DisplayCourseLoadCallback callback, String node) {
        this.callback = callback;
        this.mNode = node;
        courseList = new ArrayList<>();
        mPicasso = Picasso.get();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Section").child(mNode);
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                DisplayCourse course = dataSnapshot.getValue(DisplayCourse.class);

                mPicasso.load(course.getThumbnailURL()).fetch();
                courseList.add(course);
                callback.onLoadCallback(courseList);
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

    private abstract static class CustomChildEventListener implements ChildEventListener {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }
    }
}
