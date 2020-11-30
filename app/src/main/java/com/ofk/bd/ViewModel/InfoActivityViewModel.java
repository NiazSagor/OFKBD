package com.ofk.bd.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ofk.bd.HelperClass.UserInfo;
import com.ofk.bd.Repository.UserInfoRepository;

public class InfoActivityViewModel extends AndroidViewModel {

    private final MutableLiveData<String> userPhoneNumberLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> verificationCode = new MutableLiveData<>();

    private final UserInfoRepository repository;

    private LiveData<UserInfo> userInfoMutableLiveData = new MutableLiveData<>();

    public InfoActivityViewModel(@NonNull Application application) {
        super(application);
        repository = new UserInfoRepository(application);
        userInfoMutableLiveData = repository.getUserInfoLiveData();
    }

    public MutableLiveData<String> getUserPhoneNumberLiveData() {
        return userPhoneNumberLiveData;
    }

    public MutableLiveData<String> getVerificationCode() {
        return verificationCode;
    }

    // insert new user to local db
    public void insert(UserInfo userInfo) {
        repository.insert(userInfo);
    }

    public LiveData<UserInfo> getUserInfoMutableLiveData() {
        return userInfoMutableLiveData;
    }
}
