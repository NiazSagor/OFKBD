package com.ofk.bd.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ofk.bd.HelperClass.SectionCourseTuple;
import com.ofk.bd.HelperClass.UserProgressClass;

import java.util.List;

@Dao
public interface UserProgressDao {

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

    @Query("UPDATE user_progress SET totalVideos = totalVideos + 1 WHERE courseNameEnglish = :courseName")
    int upDateTotalVideo(String courseName);

    @Query("UPDATE user_progress SET videoWatched = videoWatched + 1 WHERE courseNameEnglish = :courseName")
    int upDateVideoWatched(String courseName);
}
