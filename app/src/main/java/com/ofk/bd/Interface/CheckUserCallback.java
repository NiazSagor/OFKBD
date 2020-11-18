package com.ofk.bd.Interface;

import com.ofk.bd.HelperClass.UserForFirebase;

public interface CheckUserCallback {
    void onUserCheckCallback(boolean isExist, String message);
    void onUserFoundCallback(UserForFirebase user);
}
