package com.ofk.bd.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ofk.bd.HelperClass.Section;
import com.ofk.bd.HelperClass.SectionVideo;
import com.ofk.bd.HelperClass.Video;
import com.ofk.bd.Interface.SectionVideoLoadCallback;

import java.util.ArrayList;
import java.util.List;

public class FirebaseQuerySubSection extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "FirebaseQuerySubSection";

    private final DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Sub Section");

    private final String sectionName, courseName;

    private final SectionVideoLoadCallback callback;

    private List<SectionVideo> sectionVideoList;

    private List<Video> videoList;

    public FirebaseQuerySubSection(SectionVideoLoadCallback callback, String sectionName, String courseName) {
        Log.d(TAG, "FirebaseQuerySubSection: " + "constructor called");
        this.callback = callback;
        this.sectionName = sectionName + " Section";
        this.courseName = courseName;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        sectionVideoList = new ArrayList<>();

        db.child(sectionName).child(courseName).child("Section").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        Section section = ds.getValue(Section.class);

                        db.child(sectionName).child(courseName).child("Video").child(section.getSectionCode())
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.exists()) {

                                            videoList = new ArrayList<>();

                                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                Video video = ds.getValue(Video.class);
                                                videoList.add(video);
                                            }

                                            SectionVideo sectionVideo = new SectionVideo(section, videoList);
                                            sectionVideoList.add(sectionVideo);

                                            Log.d(TAG, "onDataChange: " + sectionVideo.getSectionName().getSectionName());

                                            callback.onSectionVideoLoadCallback(sectionVideoList);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
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
