package com.ofk.bd.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ofk.bd.HelperClass.FirebaseQueryLiveData;
import com.ofk.bd.HelperClass.UserProgressClass;
import com.ofk.bd.Repository.UserProgressRepository;

import java.util.List;

public class DisplayCourseActivityViewModel extends AndroidViewModel {

    // sql lite
    private final UserProgressRepository repository;
    private final LiveData<List<String>> courseEnrolled;

    public DisplayCourseActivityViewModel(@NonNull Application application) {
        super(application);
        repository = new UserProgressRepository(application);
        courseEnrolled = repository.getEnrolledCourseOnly();// all courses enrolled already
    }

    /*******************************************************************************/

    // add new course to local db
    public void insert(UserProgressClass userProgressClass) {
        repository.insert(userProgressClass);
    }


    // updates total videos of a particular course
    public void updateTotalVideoCourse(String courseName, int count) {
        repository.updateVideoCount(count, courseName);
    }

    // sql lite enrolled courses
    public LiveData<List<String>> getCourseEnrolled() {
        return courseEnrolled;
    }

    // get available courses in a particular section
    public LiveData<DataSnapshot> getCourses(String sectionName){
        DatabaseReference AVAILABLE_COURSES_REF = FirebaseDatabase.getInstance().getReference("Section").child(sectionName);
        return new FirebaseQueryLiveData(AVAILABLE_COURSES_REF);
    }
}
