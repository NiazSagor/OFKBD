package com.ofk.bd.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

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

    private int videoWatchedInTotal;// video watched in total
    private int courseCompletedInTotal;// video watched in total
    private int quizCompletedInTotal;// quiz completed in total

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


    /**************************User info table*********************************/

    public void insert(UserInfo userInfo) {
        new InsertUserInfoAsyncTask(userProgressDao).execute(userInfo);
    }

    public void update(UserInfo userInfo) {
        new UpdateUserInfoAsyncTask(userProgressDao).execute(userInfo);
    }

    public static class InsertUserInfoAsyncTask extends AsyncTask<UserInfo, Void, Void> {

        UserProgressDao dao;

        public InsertUserInfoAsyncTask(UserProgressDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(UserInfo... userInfos) {
            dao.insert(userInfos[0]);
            return null;
        }
    }

    public static class UpdateUserInfoAsyncTask extends AsyncTask<UserInfo, Void, Void> {

        UserProgressDao dao;

        public UpdateUserInfoAsyncTask(UserProgressDao dao) {
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
            videoWatchedInTotal = userProgressDao.getCurrentCompletedVideoCount();
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
            courseCompletedInTotal = userProgressDao.getCurrentCompletedCourseCount();
            return null;
        }
    }

    public void updateUserVideoTotal(int count) {
        new UpdateUserVideoTotal(userProgressDao, count).execute();
    }

    private static class UpdateUserVideoTotal extends AsyncTask<Void, Void, Void> {

        UserProgressDao dao;
        int count;

        public UpdateUserVideoTotal(UserProgressDao dao, int count) {
            this.dao = dao;
            this.count = count;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.updateUserVideoTotal(count);
            return null;
        }
    }

    public void updateUserCourseTotal(int count) {
        new UpdateUserCourseTotal(userProgressDao, count).execute();
    }

    private class UpdateUserCourseTotal extends AsyncTask<Void, Void, Void> {

        UserProgressDao dao;
        int count;

        public UpdateUserCourseTotal(UserProgressDao dao, int count) {
            this.dao = dao;
            this.count = count;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.updateUserCourseTotal(count);
            return null;
        }
    }

    public int getQuizCompletedInTotal(){
        new GetQuizCompletedInTotal().execute();
        return quizCompletedInTotal;
    }

    private class GetQuizCompletedInTotal extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            quizCompletedInTotal = userProgressDao.getCurrentCompletedQuizCount();
            return null;
        }
    }
}
