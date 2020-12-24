package com.ofk.bd.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ofk.bd.Model.DisplayCourse;
import com.ofk.bd.HelperClass.MyApp;
import com.ofk.bd.Model.UserProgress;
import com.ofk.bd.Interface.DisplayCourseLoadCallback;
import com.ofk.bd.Repository.CommonRepository;
import com.ofk.bd.Repository.UserProgressRepository;

import java.util.List;

public class DisplayCourseActivityViewModel extends AndroidViewModel {

    private static final String TAG = "DisplayCourseActivityVi";
    
    // sql lite
    private final UserProgressRepository repository;
    private final CommonRepository commonRepository;
    private final LiveData<List<String>> courseEnrolled;

    public DisplayCourseActivityViewModel(@NonNull Application application) {
        super(application);
        repository = new UserProgressRepository(application);
        commonRepository = new CommonRepository(MyApp.executorService);
        courseEnrolled = repository.getEnrolledCourseOnly();// all courses enrolled already
    }


    // add new course to local db
    public void insert(UserProgress userProgress) {
        repository.insert(userProgress);
    }


    // sql lite enrolled courses
    public LiveData<List<String>> getCourseEnrolled() {
        return courseEnrolled;
    }

    private final MutableLiveData<List<DisplayCourse>> allCoursesLiveData = new MutableLiveData<>();

    public MutableLiveData<List<DisplayCourse>> getCoursesLiveData(String sectionName) {

        commonRepository.getAllCoursesFromSection(new DisplayCourseLoadCallback() {
            @Override
            public void onLoadCallback(List<DisplayCourse> courses) {
                allCoursesLiveData.postValue(courses);
            }
        }, sectionName);

        return allCoursesLiveData;
    }
}
