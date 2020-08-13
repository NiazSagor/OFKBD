package com.ofk.bd.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.ofk.bd.Dao.UserInfoDao;
import com.ofk.bd.Database.UserInfoDatabase;
import com.ofk.bd.HelperClass.UserInfo;

public class UserInfoRepository {
    private static final String TAG = "UserInfoRepository";

    private UserInfoDao userInfoDao;

    private int videoWatchedInTotal;// video watched in total
    private int courseCompletedInTotal;// video watched in total
    private int quizCompletedInTotal;// quiz completed in total

    private LiveData<UserInfo> userInfoLiveData;

    public UserInfoRepository(Application application) {
        UserInfoDatabase database = UserInfoDatabase.getInstance(application);
        userInfoDao = database.userInfoDao();
        userInfoLiveData = userInfoDao.getUser();
    }

    public LiveData<UserInfo> getUserInfoLiveData() {
        return userInfoLiveData;
    }

    public void insert(UserInfo userInfo) {
        new InsertUserInfoAsyncTask(userInfoDao).execute(userInfo);
    }

    public void update(UserInfo userInfo) {
        new UpdateUserInfoAsyncTask(userInfoDao).execute(userInfo);
    }

    public static class InsertUserInfoAsyncTask extends AsyncTask<UserInfo, Void, Void> {

        UserInfoDao dao;

        public InsertUserInfoAsyncTask(UserInfoDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(UserInfo... userInfos) {
            dao.insert(userInfos[0]);
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

        UserInfoDao dao;

        public UpdateVideoTotal(UserInfoDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.updateUserVideoTotal();
            return null;
        }
    }

    // update total course
    public void updateUserCourseTotal() {
        new UpdateUserCourseTotal(userInfoDao).execute();
    }

    private static class UpdateUserCourseTotal extends AsyncTask<Void, Void, Void> {

        UserInfoDao dao;

        public UpdateUserCourseTotal(UserInfoDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.updateUserCourseTotal();
            return null;
        }
    }

    // update total quiz
    public void updateUserQuizTotal() {
        new UpdateUserQuizTotal(userInfoDao).execute();
    }

    private static class UpdateUserQuizTotal extends AsyncTask<Void, Void, Void> {

        UserInfoDao dao;

        public UpdateUserQuizTotal(UserInfoDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.updateUserQuizTotal();
            return null;
        }
    }
}
