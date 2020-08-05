package com.ofk.bd.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ofk.bd.HelperClass.Course;
import com.ofk.bd.HelperClass.FirebaseQueryLiveData;

import java.util.ArrayList;
import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {
    // course list
    MutableLiveData<List<Course>> listMutableLiveData = new MutableLiveData<>();

    // activity pic
    LiveData<DataSnapshot> activityPicLiveData = new MutableLiveData<>();

    // activity videos
    LiveData<DataSnapshot> activityVideoLiveData = new MutableLiveData<>();

    // random course to display 1
    LiveData<DataSnapshot> randomCourseLiveData_1 = new MutableLiveData<>();

    // random course to display 2
    LiveData<DataSnapshot> randomCourseLiveData_2 = new MutableLiveData<>();

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        createCourseList();
        getActivityPicFromDatabase();
        getActivityVideoFromDatabase();
        getRandomCourseToDisplay_1();
        getRandomCourseToDisplay_2();
    }

    private void getRandomCourseToDisplay_1() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Section").child("Robotics Section");
        db.keepSynced(true);
        randomCourseLiveData_1 = new FirebaseQueryLiveData(db);
    }

    private void getRandomCourseToDisplay_2() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Section").child("Programming Section");
        db.keepSynced(true);
        randomCourseLiveData_2 = new FirebaseQueryLiveData(db);
    }

    private void getActivityVideoFromDatabase() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Activity Videos");
        db.keepSynced(true);
        activityVideoLiveData = new FirebaseQueryLiveData(db);
    }

    private void getActivityPicFromDatabase() {
        DatabaseReference db_activity_pic = FirebaseDatabase.getInstance().getReference().child("Activity Pics");
        db_activity_pic.keepSynced(true);
        activityPicLiveData = new FirebaseQueryLiveData(db_activity_pic);
    }

    private void createCourseList() {
        List<Course> list = new ArrayList<>();

        list.add(new Course("Arts"));
        list.add(new Course("Calligraphy"));
        list.add(new Course("Case Solving"));
        list.add(new Course("Craft"));
        list.add(new Course("Critical Thinking"));
        list.add(new Course("Digital Painting"));
        list.add(new Course("Guitar"));
        list.add(new Course("Programming"));
        list.add(new Course("Robotics"));

        listMutableLiveData.setValue(list);
    }

    public MutableLiveData<List<Course>> getListMutableLiveData() {
        return listMutableLiveData;
    }

    public LiveData<DataSnapshot> getActivityPicLiveData() {
        return activityPicLiveData;
    }

    public LiveData<DataSnapshot> getActivityVideoLiveData() {
        return activityVideoLiveData;
    }

    public LiveData<DataSnapshot> getRandomCourseLiveData_1() {
        return randomCourseLiveData_1;
    }

    public LiveData<DataSnapshot> getRandomCourseLiveData_2() {
        return randomCourseLiveData_2;
    }
}
