package com.ofk.bd.Task;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ofk.bd.Model.Section;
import com.ofk.bd.Model.SectionVideo;
import com.ofk.bd.Model.Video;
import com.ofk.bd.Interface.SectionVideoLoadCallback;

import java.util.ArrayList;
import java.util.List;

public class SectionVideoTask implements Runnable {

    private final String sectionName;
    private final String courseName;
    private int total = 0;

    private final List<SectionVideo> sectionWithVideos = new ArrayList<>();
    private List<Video> videoList;
    private final SectionVideoLoadCallback callback;


    public SectionVideoTask(SectionVideoLoadCallback callback, String sectionName, String courseName) {
        this.sectionName = sectionName;
        this.courseName = courseName;
        this.callback = callback;
    }

    @Override
    public void run() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Sub Section");

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
                                            sectionVideo.setTotalVideos(total);
                                            sectionWithVideos.add(sectionVideo);

                                            callback.onSectionVideoLoadCallback(sectionWithVideos, total);
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
    }
}
