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
import com.ofk.bd.HelperClass.UserProgressClass;
import com.ofk.bd.Repository.UserProgressRepository;

import java.util.List;

public class DisplayCourseActivityViewModel extends AndroidViewModel {

    // courses from db
    LiveData<DataSnapshot> availableCourses = new MutableLiveData<>();

    // enrolled courses from server db
    LiveData<DataSnapshot> enrolledCourses = new MutableLiveData<>();

    // sql lite
    private UserProgressRepository repository;
    private LiveData<List<String>> courseEnrolled;

    public DisplayCourseActivityViewModel(@NonNull Application application) {
        super(application);
        repository = new UserProgressRepository(application);
        courseEnrolled = repository.getEnrolledCourseOnly();// all courses enrolled already
    }

    // get available courses from bd
    public void getCoursesFromDB(String entry) {
        String entryPoint = entry + " Section";
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Section").child(entryPoint);
        db.keepSynced(true);
        availableCourses = new FirebaseQueryLiveData(db);
    }

    public void getEnrolledCourses(String userId) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("User").child(userId).child("Courses Enrolled");
        db.keepSynced(true);
        enrolledCourses = new FirebaseQueryLiveData(db);
    }

    // get available that come from db
    public LiveData<DataSnapshot> getAvailableCourses() {
        return availableCourses;
    }

    // from server db
    public LiveData<DataSnapshot> getEnrolledCourses() {
        return enrolledCourses;
    }


    /*******************************************************************************/

    // sql lite insert
    public void insert(UserProgressClass userProgressClass) {
        repository.insert(userProgressClass);
    }

    // sql lite update
    public void update(UserProgressClass userProgressClass) {
        repository.update(userProgressClass);
    }

    // sql lite enrolled courses
    public LiveData<List<String>> getCourseEnrolled() {
        return courseEnrolled;
    }
}
