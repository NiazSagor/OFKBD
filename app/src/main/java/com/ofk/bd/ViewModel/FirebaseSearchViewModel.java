package com.ofk.bd.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ofk.bd.Model.DisplayCourse;
import com.ofk.bd.Model.Video;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FirebaseSearchViewModel extends AndroidViewModel {

    private static final String TAG = "FirebaseSearchViewModel";

    // user search input
    MutableLiveData<String> mutableLiveData = new MutableLiveData<>();

    // how many courses are found
    MutableLiveData<Integer> foundCourseCount = new MutableLiveData<>();

    // how many videos are found
    MutableLiveData<Integer> foundVideoCount = new MutableLiveData<>();

    MutableLiveData<List<Integer>> count = new MutableLiveData<>();

    public FirebaseSearchViewModel(@NonNull Application application) {
        super(application);
    }

    // get user search input
    public MutableLiveData<String> getMutableLiveData() {
        return mutableLiveData;
    }

    // get how many courses were matched
    public MutableLiveData<Integer> getFoundCourseCount() {
        return foundCourseCount;
    }

    // get how any videos were matched
    public MutableLiveData<Integer> getFoundVideoCount() {
        return foundVideoCount;
    }

    public MutableLiveData<List<Integer>> getCount() {
        return count;
    }

    private MutableLiveData<List<Video>> searchVideoResult = new MutableLiveData<>();
    private MutableLiveData<List<DisplayCourse>> searchCourseResult = new MutableLiveData<>();

    public void getVideoResult() {
        FirebaseDatabase.getInstance().getReference().child("All Video")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            List<Video> videos = new ArrayList<>();
                            for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                                Video video = snapShot.getValue(Video.class);
                                if (Objects.requireNonNull(video).getTitle().toLowerCase().contains(mutableLiveData.getValue().toLowerCase())) {
                                    videos.add(video);
                                }
                            }

                            searchVideoResult.setValue(videos);
                            getFoundVideoCount().setValue(videos.size());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public MutableLiveData<List<Video>> getSearchVideoResult() {
        return searchVideoResult;
    }

    public void getCourseResult() {
        FirebaseDatabase.getInstance().getReference().child("All Course")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            List<DisplayCourse> courses = new ArrayList<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                DisplayCourse course = snapshot.getValue(DisplayCourse.class);
                                if (course.getCourseTitleEnglish().toLowerCase().contains(mutableLiveData.getValue().toLowerCase())) {
                                    courses.add(course);
                                }

                            }
                            searchCourseResult.setValue(courses);
                            getFoundCourseCount().setValue(courses.size());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public MutableLiveData<List<DisplayCourse>> getSearchCourseResult() {
        return searchCourseResult;
    }
}
