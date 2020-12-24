package com.ofk.bd.Interface;

import com.ofk.bd.Model.UserInfo;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface UserInfoApi {

    @POST("https://script.google.com/macros/s/AKfycbzcJgym5u3Z0WPG7wxuV2tvreb6YUJ3j_0rcX4sQ3IASQ6_IA/exec")
    Call<UserInfo> pushUserInfoToDb(@QueryMap Map<String, String> parameters);
}

