package com.ofk.bd.Repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.ofk.bd.Dao.UserInfoDao;
import com.ofk.bd.Database.UserInfoDatabase;
import com.ofk.bd.HelperClass.UserInfo;
import com.ofk.bd.Interface.UserInfoApi;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserInfoRepository {
    private static final String TAG = "UserInfoRepository";

    private static final DatabaseReference USER_REF = FirebaseDatabase.getInstance().getReference("User");

    private final UserInfoDao userInfoDao;

    private final LiveData<UserInfo> userInfoLiveData;

    private final LiveData<Integer> userCurrentCourseCompleted;

    public UserInfoRepository(Application application) {
        UserInfoDatabase database = UserInfoDatabase.getInstance(application);
        userInfoDao = database.userInfoDao();
        userInfoLiveData = userInfoDao.getUser();
        userCurrentCourseCompleted = userInfoDao.getCurrentCompletedCourseCount();
    }

    public LiveData<UserInfo> getUserInfoLiveData() {
        return userInfoLiveData;
    }

    public LiveData<Integer> getUserCurrentCourseCompleted() {
        return userCurrentCourseCompleted;
    }

    // insert new user to local and cloud db
    public void insert(UserInfo userInfo) {
        new InsertUserInfoAsyncTask(userInfoDao).execute(userInfo);
    }

    public void update(UserInfo userInfo) {
        new UpdateUserInfoAsyncTask(userInfoDao).execute(userInfo);
    }

    public static class InsertUserInfoAsyncTask extends AsyncTask<UserInfo, Void, Void> {

        private final UserInfoDao dao;

        public InsertUserInfoAsyncTask(UserInfoDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(UserInfo... userInfos) {
            dao.insert(userInfos[0]);
            USER_REF.child(userInfos[0].getUserPhoneNumber()).setValue(userInfos[0]);
            return null;
        }
    }

    public static class UpdateUserInfoAsyncTask extends AsyncTask<UserInfo, Void, Void> {

        UserInfoDao dao;

        public UpdateUserInfoAsyncTask(UserInfoDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(UserInfo... userInfos) {
            dao.update(userInfos[0]);
            return null;
        }
    }


    // update total video
    public void updateUserVideoTotal() {
        new UpdateVideoTotal(userInfoDao).execute();
    }

    private static class UpdateVideoTotal extends AsyncTask<Void, Void, Void> {

        private final UserInfoDao dao;

        public UpdateVideoTotal(UserInfoDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.updateUserVideoTotal();
            updateCounter("videoCompleted");
            return null;
        }
    }

    // TODO update total course
    public void updateUserCourseTotal() {
        new UpdateUserCourseTotal(userInfoDao).execute();
    }

    private static class UpdateUserCourseTotal extends AsyncTask<Void, Void, Void> {

        private UserInfoDao dao;

        public UpdateUserCourseTotal(UserInfoDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.updateUserCourseTotal();
            updateCounter("courseCompleted");
            return null;
        }
    }

    // TODO update total quiz
    public void updateUserQuizTotal() {
        new UpdateUserQuizTotal(userInfoDao).execute();
    }

    private static class UpdateUserQuizTotal extends AsyncTask<Void, Void, Void> {

        private final UserInfoDao dao;

        public UpdateUserQuizTotal(UserInfoDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.updateUserQuizTotal();
            updateCounter("quizCompleted");
            return null;
        }
    }

    // if user updates info later on such as dob, gender, class, school, email
    public void updateUserInfo(String text, String entry) {
        new UpdateUserInfo(userInfoDao, entry, text).execute();
    }

    private static class UpdateUserInfo extends AsyncTask<Void, Void, Void> {

        private final UserInfoDao dao;
        private final String entry;
        private final String text;

        public UpdateUserInfo(UserInfoDao dao, String entry, String text) {
            this.dao = dao;
            this.entry = entry;
            this.text = text;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            switch (entry) {
                case "email":
                    dao.updateUserEmail(text);
                    update("userEmail", text);
                    break;
                case "class":
                    dao.updateUserClass(text);
                    update("userClass", text);
                    break;
                case "institute":
                    dao.updateUserSchool(text);
                    update("userSchool", text);
                    break;
                case "gender":
                    dao.updateUserGender(text);
                    update("userGender", text);
                    break;
                case "dob":
                    dao.updateUserDOB(text);
                    update("userDOB", text);
                    break;
            }

            return null;
        }

        private void update(String node, String text) {
            USER_REF.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                    .child(node)
                    .setValue(text);
        }
    }

    // post data to google sheets
    public void postData(UserInfo userInfo) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserInfoApi userInfoApi = retrofit.create(UserInfoApi.class);

        Map<String, String> map = new HashMap<>();

        map.put("uid", userInfo.getFirebaseUid());
        map.put("name", userInfo.getUserName());
        map.put("number", userInfo.getUserPhoneNumber());
        map.put("email", userInfo.getUserEmail());
        map.put("standard", userInfo.getUserClass());
        map.put("institute", userInfo.getUserSchool());
        map.put("dob", userInfo.getUserDOB());
        map.put("gender", userInfo.getUserGender());

        Call<UserInfo> call = userInfoApi.pushUserInfoToDb(map);

        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {

                if (!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: unsuccessful");
                } else {
                    Log.d(TAG, "onResponse: " + response.body());
                }

            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private static void updateCounter(String node) {
        USER_REF.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                .child(node)
                .runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {

                        long currentCount = (long) mutableData.getValue();
                        currentCount++;
                        mutableData.setValue(currentCount);

                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                        Log.d(TAG, "onComplete: " + databaseError.getMessage());
                    }
                });
    }
}
