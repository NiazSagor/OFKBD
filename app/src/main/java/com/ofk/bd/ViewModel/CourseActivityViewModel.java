package com.ofk.bd.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ofk.bd.Repository.UserInfoRepository;
import com.ofk.bd.Repository.UserProgressRepository;

public class CourseActivityViewModel extends AndroidViewModel {
    private static final String TAG = "CourseActivityViewModel";

    private final UserProgressRepository userProgressRepository;

    private final UserInfoRepository userInfoRepository;

    private final MutableLiveData<String> sectionName = new MutableLiveData<>();
    private final MutableLiveData<String> courseName = new MutableLiveData<>();

    public CourseActivityViewModel(@NonNull Application application) {
        super(application);
        userProgressRepository = new UserProgressRepository(application);
        userInfoRepository = new UserInfoRepository(application);
    }

    public MutableLiveData<String> getSectionName() {
        return sectionName;
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

    public void updateVideoWatched(String courseName) {
        userProgressRepository.updateVideoWatched(courseName);
    }

    public void updateTotalVideoWatched() {
        userInfoRepository.updateUserVideoTotal();
    }
}
