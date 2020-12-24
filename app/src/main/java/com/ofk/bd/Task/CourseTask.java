package com.ofk.bd.Task;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ofk.bd.Model.DisplayCourse;
import com.ofk.bd.Interface.DisplayCourseLoadCallback;

import java.util.ArrayList;
import java.util.List;

public class CourseTask implements Runnable {


    private final List<DisplayCourse> recommendedCourse = new ArrayList<>();
    private final String sectionName;
    private final DisplayCourseLoadCallback callback;

    public CourseTask(DisplayCourseLoadCallback callback, String sectionName) {
        this.sectionName = sectionName;
        this.callback = callback;
    }

    @Override
    public void run() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Section").child(sectionName);

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        recommendedCourse.add(ds.getValue(DisplayCourse.class));
                    }
                    callback.onLoadCallback(recommendedCourse);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
