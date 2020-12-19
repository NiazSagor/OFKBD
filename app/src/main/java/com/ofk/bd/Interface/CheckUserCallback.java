package com.ofk.bd.Interface;

import com.ofk.bd.HelperClass.UserForFirebase;
import com.ofk.bd.HelperClass.UserInfo;
import com.ofk.bd.HelperClass.UserProgressClass;

import java.util.List;

public interface CheckUserCallback {
    void onUserCheckCallback(boolean isExist, String message);
    void onUserFoundCallback(UserInfo userInfoCloud, List<UserProgressClass> userProgressClassList);
}
