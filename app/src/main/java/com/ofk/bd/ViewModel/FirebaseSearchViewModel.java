package com.ofk.bd.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

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
}
