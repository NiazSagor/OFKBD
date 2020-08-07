package com.ofk.bd.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ofk.bd.HelperClass.SectionCourseTuple;
import com.ofk.bd.HelperClass.UserInfo;
import com.ofk.bd.HelperClass.UserProgressClass;

import java.util.List;

@Dao
public interface UserProgressDao {

    /*********************User info table**************************/
    @Insert
    void insert(UserInfo userInfo);

    @Update
    void update(UserInfo userInfo);

    @Query("UPDATE user_info SET videoCompleted = :count WHERE id = 1")
    void updateUserVideoTotal(int count);

    @Query("UPDATE user_info SET courseCompleted = :count WHERE id = 1")
    void updateUserCourseTotal(int count);

    @Query("UPDATE user_info SET quizCompleted = :count WHERE id = 1")
    void updateUserQuizTotal(int count);

    @Query("SELECT courseCompleted FROM user_info WHERE id = 1")
    int getCurrentCompletedCourseCount();

    @Query("SELECT videoCompleted FROM user_info WHERE id = 1")
    int getCurrentCompletedVideoCount();

    @Query("SELECT quizCompleted FROM user_info WHERE id = 1")
    int getCurrentCompletedQuizCount();

    /*********************User progress table**********************/
    @Insert
    void insert(UserProgressClass userProgressClass);

    @Update
    void update(UserProgressClass userProgressClass);

    @Delete
    void delete(UserProgressClass userProgressClass);

    @Query("SELECT * FROM user_progress")
    LiveData<List<UserProgressClass>> getAllProgress();

    @Query("SELECT courseNameEnglish FROM user_progress")
    LiveData<List<String>> getAllEnrolledCourses();

    @Query("SELECT videoWatched FROM user_progress WHERE courseNameEnglish = :courseName")
    int getCurrentVideoWatchCountForCurrentCourse(String courseName);

    @Query("SELECT sectionName, courseEnrolled, courseThumbnailURL, totalVideos,videoWatched FROM user_progress")
    LiveData<List<SectionCourseTuple>> loadFullName();

    @Query("UPDATE user_progress SET totalVideos = :count WHERE courseNameEnglish = :courseName")
    void upDateTotalVideo(int count, String courseName);

    @Query("UPDATE user_progress SET videoWatched = :count WHERE courseNameEnglish = :courseName")
    void upDateVideoWatched(int count, String courseName);
}
