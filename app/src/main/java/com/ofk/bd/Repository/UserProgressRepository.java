package com.ofk.bd.Repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.ofk.bd.Dao.UserInfoDao;
import com.ofk.bd.Dao.UserProgressDao;
import com.ofk.bd.Database.UserProgressDatabase;
import com.ofk.bd.HelperClass.SectionCourseTuple;
import com.ofk.bd.HelperClass.UserInfo;
import com.ofk.bd.HelperClass.UserProgressClass;

import java.util.List;

public class UserProgressRepository {
    private static final String TAG = "UserProgressRepository";
    private UserProgressDao userProgressDao;

    /*
     *
     * User progress table variables
     *
     * */

    private LiveData<List<UserProgressClass>> allProgress;// all progress
    private LiveData<List<String>> courseEnrolled;// already enrolled courses
    private LiveData<List<SectionCourseTuple>> combinedSectionCourseList;//section name and course name combined list
    private int videoWatchedPerCourse;// video watched course wise

    /*
     *
     * User info table variables
     *
     * */


    public UserProgressRepository(Application application) {
        UserProgressDatabase database = UserProgressDatabase.getInstance(application);
        userProgressDao = database.userProgressDao();
        allProgress = userProgressDao.getAllProgress();
        courseEnrolled = userProgressDao.getAllEnrolledCourses();
        combinedSectionCourseList = userProgressDao.loadFullName();
    }

    /**************************User progress table*********************************/
    public LiveData<List<UserProgressClass>> getAllProgress() {
        return allProgress;
    }

    public LiveData<List<String>> getCourseEnrolled() {
        return courseEnrolled;
    }

    public LiveData<List<SectionCourseTuple>> getCombinedSectionCourseList() {
        return combinedSectionCourseList;
    }

    public int getVideoWatched(String courseName) {
        new GetVideoWatchedCount(courseName).execute();
        return videoWatchedPerCourse;
    }

    public void insert(UserProgressClass progressClass) {
        new InsertUserProgressAsyncTask(userProgressDao).execute(progressClass);
    }

    public void update(UserProgressClass progressClass) {
        new UpdateUserProgressAsyncTask(userProgressDao).execute(progressClass);
    }

    // update total video of a course method
    public void updateVideoCount(int videoCount, String courseName) {
        new UpdateVideoCountAsyncTask(userProgressDao, videoCount, courseName).execute();
    }

    // update total video watched of a course method
    public void updateVideoWatched(int videoCount, String courseName) {
        new UpdateVideoWatched(userProgressDao, videoCount, courseName).execute();
    }

    // update total video count of a course
    private static class UpdateVideoCountAsyncTask extends AsyncTask<Void, Void, Void> {

        UserProgressDao dao;
        int count;
        String course;

        public UpdateVideoCountAsyncTask(UserProgressDao dao, int count, String course) {
            this.dao = dao;
            this.count = count;
            this.course = course;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.upDateTotalVideo(count, course);
            return null;
        }
    }

    // update video watch count on a specific course
    private static class UpdateVideoWatched extends AsyncTask<Void, Void, Void> {

        UserProgressDao dao;
        int count;
        String course;

        public UpdateVideoWatched(UserProgressDao dao, int count, String course) {
            this.dao = dao;
            this.count = count;
            this.course = course;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.upDateVideoWatched(count, course);
            return null;
        }
    }

    // get video watched on a specific course
    private class GetVideoWatchedCount extends AsyncTask<Void, Integer, Void> {

        String courseName;

        public GetVideoWatchedCount(String courseName) {
            this.courseName = courseName;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            videoWatchedPerCourse = userProgressDao.getCurrentVideoWatchCountForCurrentCourse(courseName);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    // insert
    private static class InsertUserProgressAsyncTask extends AsyncTask<UserProgressClass, Void, Void> {

        UserProgressDao dao;

        public InsertUserProgressAsyncTask(UserProgressDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(UserProgressClass... userProgressClasses) {
            dao.insert(userProgressClasses[0]);
            return null;
        }
    }

    // update
    private static class UpdateUserProgressAsyncTask extends AsyncTask<UserProgressClass, Void, Void> {

        UserProgressDao dao;

        public UpdateUserProgressAsyncTask(UserProgressDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(UserProgressClass... userProgressClasses) {
            dao.update(userProgressClasses[0]);
            return null;
        }
    }
}
