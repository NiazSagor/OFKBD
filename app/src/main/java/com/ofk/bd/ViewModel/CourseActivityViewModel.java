package com.ofk.bd.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ofk.bd.Repository.UserInfoRepository;
import com.ofk.bd.Repository.UserProgressRepository;

public class CourseActivityViewModel extends AndroidViewModel {
    private static final String TAG = "CourseActivityViewModel";

    UserProgressRepository userProgressRepository;

    UserInfoRepository userInfoRepository;

    MutableLiveData<String> sectionName = new MutableLiveData<>();
    MutableLiveData<String> courseName = new MutableLiveData<>();
    LiveData<Long> currentVideoPosition = new MutableLiveData<>();

    public CourseActivityViewModel(@NonNull Application application) {
        super(application);
        userProgressRepository = new UserProgressRepository(application);
        userInfoRepository = new UserInfoRepository(application);
        currentVideoPosition = userProgressRepository.getCurrentVideoPosition();
    }

    public MutableLiveData<String> getSectionName() {
        return sectionName;
    }

    public LiveData<Long> getCurrentVideoPosition() {
        return currentVideoPosition;
    }

    public void setCurrentVideoPosition(long currentVideoPosition) {
        Log.d(TAG, "setCurrentVideoPosition: " + currentVideoPosition);
        userProgressRepository.insertCurrentVideoPosition(currentVideoPosition);
    }

    public void setSectionName(String s) {
        sectionName.setValue(s);
    }

    public void setCourseName(String s) {
        courseName.setValue(s);
    }

    public MutableLiveData<String> getCourseName() {
        return courseName;
    }

    public void updateVideoWatched(String courseName){
        userProgressRepository.updateVideoWatched(courseName);
    }
}
