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

    private int total = 0;

    private final String sectionName, courseName;

    private final SectionVideoLoadCallback callback;

    private List<SectionVideo> sectionVideoList;

    private List<Video> videoList;

    public FirebaseQuerySubSection(SectionVideoLoadCallback callback, String sectionName, String courseName) {
        Log.d(TAG, "FirebaseQuerySubSection: " + "constructor called");
        this.callback = callback;
        this.sectionName = sectionName;
        this.courseName = courseName;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        sectionVideoList = new ArrayList<>();

        // parent node to child node then to section node
        db.child(sectionName).child(courseName).child("Section").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    // iterating through every section in the particular course
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        Section section = ds.getValue(Section.class);// single section

                        // now we need to know how many videos per section has
                        db.child(sectionName).child(courseName).child("Video").child(section.getSectionCode())
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.exists()) {

                                            videoList = new ArrayList<>();

                                            // iterating through every video in the particular section
                                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                Video video = ds.getValue(Video.class);// single video
                                                videoList.add(video);// adding video to that particular section
                                                total++;
                                            }

                                            // making section video object combining section and videos
                                            SectionVideo sectionVideo = new SectionVideo(section, videoList);
                                            sectionVideoList.add(sectionVideo);

                                            callback.onSectionVideoLoadCallback(sectionVideoList, total);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                    }
                } else {
                    callback.onSectionVideoLoadCallback(sectionVideoList, total);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return null;
    }
}
