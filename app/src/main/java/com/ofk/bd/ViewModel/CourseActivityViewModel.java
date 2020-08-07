package com.ofk.bd.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ofk.bd.Repository.UserProgressRepository;

import java.util.List;

public class CourseActivityViewModel extends AndroidViewModel {

    UserProgressRepository repository;

    MutableLiveData<String> sectionName = new MutableLiveData<>();
    MutableLiveData<String> courseName = new MutableLiveData<>();

    MutableLiveData<List<String>> combinedList = new MutableLiveData<>();

    public CourseActivityViewModel(@NonNull Application application) {
        super(application);
        repository = new UserProgressRepository(application);
    }

    public void setCombinedList(List<String> list) {
        combinedList.setValue(list);
    }

    public MutableLiveData<List<String>> getCombinedList() {
        return combinedList;
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

    public void updateVideo(int count, String courseName) {
        repository.updateVideoWatched(count, courseName);
    }

    public int getCurrentVideoWatched(String courseName) {
        return repository.getVideoWatched(courseName);
    }

    /*
     *
     *
     * *************************User Info Table Query**************************
     *
     *
     * */

    public int getVideoWatchedInTotal(){
        return repository.getVideoWatchedInTotal();
    }

    public void updateUserVideoTotal(int count){
        repository.updateUserVideoTotal(count);
    }
}
