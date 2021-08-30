package com.ofk.bd.Repository;

import android.app.Application;
import android.os.AsyncTask;

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
import com.ofk.bd.Dao.UserProgressDao;
import com.ofk.bd.Database.UserProgressDatabase;
import com.ofk.bd.HelperClass.SectionCourseNameTuple;
import com.ofk.bd.HelperClass.SectionCourseTuple;
import com.ofk.bd.Model.UserProgress;

import java.util.List;

public class UserProgressRepository {
    private static final DatabaseReference USER_PROGRESS_REF = FirebaseDatabase.getInstance().getReference("User Progress");
    private static final String TAG = "UserProgressRepository";
    private UserProgressDao userProgressDao;

    /*
     *
     * User progress table variables
     *
     * */

    private LiveData<List<String>> enrolledCourseOnly;
    private LiveData<List<SectionCourseNameTuple>> courseEnrolled;// already enrolled courses
    private LiveData<List<SectionCourseTuple>> combinedSectionCourseList;//section name and course name combined list

    public UserProgressRepository(Application application) {
        UserProgressDatabase database = UserProgressDatabase.getInstance(application);
        userProgressDao = database.userProgressDao();
        courseEnrolled = userProgressDao.getAllEnrolledCourses();
        enrolledCourseOnly = userProgressDao.getAllEnrolledCoursesOnly();
        combinedSectionCourseList = userProgressDao.loadFullName();
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

    // insert new course to local and cloud db
    public void insert(UserProgress progressClass) {
        new InsertUserProgressAsyncTask(userProgressDao).execute(progressClass);
    }


    // update total video of a course method
    public void updateVideoCount(int videoCount, String courseName) {
        new UpdateVideoCountAsyncTask(userProgressDao, courseName, videoCount).execute();
    }

    // if the enrolled first course if completed or not
    public LiveData<Boolean> getIsFirstCourseCompleted() {
        return userProgressDao.isFirstCourseCompleted();
    }

    // update total video count of a course
    private static class UpdateVideoCountAsyncTask extends AsyncTask<Void, Void, Void> {

        private UserProgressDao dao;
        private String course;
        private int count;
        private static final DatabaseReference USER_PROGRESS = FirebaseDatabase.getInstance().getReference("User Progress");

        public UpdateVideoCountAsyncTask(UserProgressDao dao, String course, int count) {
            this.dao = dao;
            this.course = course;
            this.count = count;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.upDateTotalVideo(course, count);
            USER_PROGRESS.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                    .child(course).child("totalVideos")
                    .setValue(count);
            return null;
        }
    }

    // update total video watched of a course method
    public void updateVideoWatched(String courseName) {
        new UpdateVideoWatched(userProgressDao, courseName).execute();
    }

    // update video watch count on a specific course
    private static class UpdateVideoWatched extends AsyncTask<Void, Void, Void> {

        private final UserProgressDao dao;
        private final String course;

        public UpdateVideoWatched(UserProgressDao dao, String course) {
            this.dao = dao;
            this.course = course;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            DatabaseReference db = USER_PROGRESS_REF.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                    .child(course);

            if (dao.getTotalVideoCountForCurrentCourse(course) != dao.getCurrentVideoWatchCountForCurrentCourse(course)) {
                // if total videos and currently watched videos are not equal then increase the video watch count
                dao.upDateVideoWatched(course);

                if (dao.getTotalVideoCountForCurrentCourse(course) == dao.getCurrentVideoWatchCountForCurrentCourse(course)) {
                    dao.updateCourseStatus(course);
                    db.child("isFinished").setValue(true);
                }

                db.child("videoWatched")
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

                            }
                        });
            }
            return null;
        }
    }

    // insert
    private static class InsertUserProgressAsyncTask extends AsyncTask<UserProgress, Void, Void> {

        private final UserProgressDao dao;

        public InsertUserProgressAsyncTask(UserProgressDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(UserProgress... userProgresses) {
            UserProgress userProgress = userProgresses[0];
            dao.insert(userProgress);
            userProgress.setId(null);
            USER_PROGRESS_REF.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                    .child(userProgress.getCourseNameEnglish())
                    .setValue(userProgress);
            return null;
        }
    }
}
