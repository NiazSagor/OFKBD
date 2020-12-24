package com.ofk.bd.Interface;

import com.ofk.bd.Model.UserInfo;
import com.ofk.bd.Model.UserProgress;

import java.util.List;

public interface CheckUserCallback {
    void onUserCheckCallback(boolean isExist, String message);
    void onUserFoundCallback(UserInfo userInfoCloud, List<UserProgress> userProgressList);
}
