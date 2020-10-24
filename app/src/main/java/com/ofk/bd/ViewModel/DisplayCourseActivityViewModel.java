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

    // sql lite
    private final UserProgressRepository repository;
    private final LiveData<List<String>> courseEnrolled;

    public DisplayCourseActivityViewModel(@NonNull Application application) {
        super(application);
        repository = new UserProgressRepository(application);
        courseEnrolled = repository.getEnrolledCourseOnly();// all courses enrolled already
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

    // updates total videos of a particular course
    public void updateTotalVideoCourse(String courseName, int count){
        repository.updateVideoCount(count, courseName);
    }

    // sql lite enrolled courses
    public LiveData<List<String>> getCourseEnrolled() {
        return courseEnrolled;
    }
}
