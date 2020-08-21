package com.ofk.bd.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ofk.bd.Repository.UserInfoRepository;

public class InfoActivityViewModel extends AndroidViewModel {

    private MutableLiveData<String> userPhoneNumberLiveData = new MutableLiveData<>();

    private UserInfoRepository repository;

    public InfoActivityViewModel(@NonNull Application application) {
        super(application);
        repository = new UserInfoRepository(application);
    }

    public MutableLiveData<String> getUserPhoneNumberLiveData() {
        return userPhoneNumberLiveData;
    }

    public void updateUserInfo(String userName, String userEmail, String userPhoneNumber) {
        repository.updateUser(userName, userEmail, userPhoneNumber);
    }
}
