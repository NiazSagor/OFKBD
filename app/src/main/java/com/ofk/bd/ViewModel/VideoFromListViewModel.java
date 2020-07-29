package com.ofk.bd.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class VideoFromListViewModel extends AndroidViewModel {

    MutableLiveData<String> mutableLiveData = new MutableLiveData<>();

    public VideoFromListViewModel(@NonNull Application application) {
        super(application);
    }

    public void setMutableLiveData(String id){
        mutableLiveData.setValue(id);
    }

    public MutableLiveData<String> getMutableLiveData() {
        return mutableLiveData;
    }
}
