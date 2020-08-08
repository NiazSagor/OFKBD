package com.ofk.bd.Repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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

    // total video watched in total
    public int getVideoWatchedInTotal() {
        new GetVideoWatchedInTotal().execute();
        return videoWatchedInTotal;
    }

    private class GetVideoWatchedInTotal extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            videoWatchedInTotal = userInfoDao.getCurrentCompletedVideoCount();
            return null;
        }
    }

    // total course completed
    public int getCourseCompletedInTotal() {
        new GetCourseCompletedInTotal().execute();
        return courseCompletedInTotal;
    }

    private class GetCourseCompletedInTotal extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            courseCompletedInTotal = userInfoDao.getCurrentCompletedCourseCount();
            return null;
        }
    }

    public void updateUserVideoTotal(int count) {
        Log.d(TAG, "updateUserVideoTotal: " + count);
        new UpdateUserVideoTotal(userInfoDao, count).execute();
    }

    private static class UpdateUserVideoTotal extends AsyncTask<Void, Void, Void> {

        UserInfoDao dao;
        int count;

        public UpdateUserVideoTotal(UserInfoDao dao, int count) {
            this.dao = dao;
            this.count = count;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(TAG, "doInBackground: " + count);
            dao.updateUserVideoTotal(count);
            return null;
        }
    }

    public void updateUserCourseTotal(int count) {
        Log.d(TAG, "updateUserCourseTotal: ");
        new UpdateUserCourseTotal(userInfoDao, count).execute();
    }

    private class UpdateUserCourseTotal extends AsyncTask<Void, Void, Void> {

        UserInfoDao dao;
        int count;

        public UpdateUserCourseTotal(UserInfoDao dao, int count) {
            this.dao = dao;
            this.count = count;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.updateUserCourseTotal(count);
            return null;
        }
    }

    public int getQuizCompletedInTotal() {
        new GetQuizCompletedInTotal().execute();
        return quizCompletedInTotal;
    }

    private class GetQuizCompletedInTotal extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            quizCompletedInTotal = userInfoDao.getCurrentCompletedQuizCount();
            return null;
        }
    }
}
