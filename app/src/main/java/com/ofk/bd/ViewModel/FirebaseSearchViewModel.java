package com.ofk.bd.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ofk.bd.HelperClass.FirebaseQueryLiveData;

public class FirebaseSearchViewModel extends AndroidViewModel {

    private static final String TAG = "FirebaseSearchViewModel";

    private DatabaseReference db_ref_all_videos;
    private DatabaseReference db_ref_all_courses;

    // query all videos
    FirebaseQueryLiveData searchResultForVideo;

    // query all courses
    FirebaseQueryLiveData searchResultForCourse;

    // user search input
    MutableLiveData<String> mutableLiveData = new MutableLiveData<>();

    // how many courses are found
    MutableLiveData<Integer> foundCourseCount = new MutableLiveData<>();

    // how many videos are found
    MutableLiveData<Integer> foundVideoCount = new MutableLiveData<>();

    public FirebaseSearchViewModel(@NonNull Application application) {
        super(application);
       //db_ref_all_videos = FirebaseDatabase.getInstance().getReference().child("All Video");


        //searchResultForVideo = new FirebaseQueryLiveData(db_ref_all_videos);


    }

    public void setDB() {
        db_ref_all_courses = FirebaseDatabase.getInstance().getReference().child("All Course");
        searchResultForCourse = new FirebaseQueryLiveData(db_ref_all_courses);
    }

    // get all videos list
    public LiveData<DataSnapshot> getDataSnapshotLiveData() {
        return searchResultForVideo;
    }

    // get all courses list
    public LiveData<DataSnapshot> getDataSnapshotLiveDataForCourse() {
        return searchResultForCourse;
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
}
