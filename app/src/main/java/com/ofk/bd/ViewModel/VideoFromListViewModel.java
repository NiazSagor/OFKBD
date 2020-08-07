package com.ofk.bd.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class VideoFromListViewModel extends AndroidViewModel {
    private static final String TAG = "VideoFromListViewModel";

    MutableLiveData<String> mutableLiveData = new MutableLiveData<>();

    public VideoFromListViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<String> getMutableLiveData() {
        return mutableLiveData;
    }
}
