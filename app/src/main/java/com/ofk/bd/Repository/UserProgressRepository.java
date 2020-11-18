package com.ofk.bd.Repository;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.ofk.bd.Dao.UserProgressDao;
import com.ofk.bd.Database.UserProgressDatabase;
import com.ofk.bd.HelperClass.SectionCourseNameTuple;
import com.ofk.bd.HelperClass.SectionCourseTuple;
import com.ofk.bd.HelperClass.UserProgressClass;
import com.ofk.bd.Model.YTMedia;
import com.ofk.bd.Model.YTSubtitles;
import com.ofk.bd.Model.YoutubeMeta;
import com.ofk.bd.Utility.ExtractorException;
import com.ofk.bd.Utility.YoutubeStreamExtractor;

import java.util.List;

public class UserProgressRepository {
    private static final String TAG = "UserProgressRepository";
    private UserProgressDao userProgressDao;

    /*
     *
     * User progress table variables
     *
     * */

    private LiveData<List<String>> enrolledCourseOnly;
    private LiveData<List<UserProgressClass>> allProgress;// all progress
    private LiveData<List<SectionCourseNameTuple>> courseEnrolled;// already enrolled courses
    private LiveData<List<SectionCourseTuple>> combinedSectionCourseList;//section name and course name combined list
    private LiveData<Long> currentVideoPosition = new MutableLiveData<>();
    private static MutableLiveData<MediaSource> videoSource = new MutableLiveData<>();


    public UserProgressRepository(Application application) {
        UserProgressDatabase database = UserProgressDatabase.getInstance(application);
        userProgressDao = database.userProgressDao();
        allProgress = userProgressDao.getAllProgress();
        courseEnrolled = userProgressDao.getAllEnrolledCourses();
        enrolledCourseOnly = userProgressDao.getAllEnrolledCoursesOnly();
        combinedSectionCourseList = userProgressDao.loadFullName();
        currentVideoPosition = userProgressDao.getCurrentVideoPosition();
    }

    /**************************User progress table*********************************/
    public LiveData<List<UserProgressClass>> getAllProgress() {
        return allProgress;
    }

    public LiveData<List<SectionCourseNameTuple>> getCourseEnrolled() {
        return courseEnrolled;
    }

    public LiveData<List<String>> getEnrolledCourseOnly() {
        return enrolledCourseOnly;
    }

    public LiveData<List<SectionCourseTuple>> getCombinedSectionCourseList() {
        return combinedSectionCourseList;
    }

    public LiveData<Long> getCurrentVideoPosition() {
        return currentVideoPosition;
    }

    public void insertCurrentVideoPosition(long videoPos) {
        new InsertCurrentVideoPositionAsyncTask(userProgressDao, videoPos).execute();
    }


    public void insert(UserProgressClass progressClass) {
        new InsertUserProgressAsyncTask(userProgressDao).execute(progressClass);
    }

    public void update(UserProgressClass progressClass) {
        new UpdateUserProgressAsyncTask(userProgressDao).execute(progressClass);
    }

    // update total video of a course method
    public void updateVideoCount(int videoCount, String courseName) {
        new UpdateVideoCountAsyncTask(userProgressDao, courseName, videoCount).execute();
    }

    // update total video count of a course
    private static class UpdateVideoCountAsyncTask extends AsyncTask<Void, Void, Void> {

        UserProgressDao dao;
        String course;
        int count;

        public UpdateVideoCountAsyncTask(UserProgressDao dao, String course, int count) {
            this.dao = dao;
            this.course = course;
            this.count = count;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.upDateTotalVideo(course, count);
            return null;
        }
    }


    public static class InsertCurrentVideoPositionAsyncTask extends AsyncTask<Void, Void, Void> {

        UserProgressDao dao;
        long pos;

        public InsertCurrentVideoPositionAsyncTask(UserProgressDao dao, long pos) {
            this.dao = dao;
            this.pos = pos;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.setCurrentVideoPosition(pos);
            return null;
        }
    }

    // update total video watched of a course method
    public void updateVideoWatched(String courseName) {
        new UpdateVideoWatched(userProgressDao, courseName).execute();
    }

    // update video watch count on a specific course
    private static class UpdateVideoWatched extends AsyncTask<Void, Void, Void> {

        UserProgressDao dao;
        String course;

        public UpdateVideoWatched(UserProgressDao dao, String course) {
            this.dao = dao;
            this.course = course;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            if (dao.getTotalVideoCountForCurrentCourse(course) != dao.getCurrentVideoWatchCountForCurrentCourse(course)) {
                // if total videos and currently watched videos are not equal then increase the video watch count
                dao.upDateVideoWatched(course);
            }
            return null;
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
