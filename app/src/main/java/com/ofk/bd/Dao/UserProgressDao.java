package com.ofk.bd.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ofk.bd.HelperClass.SectionCourseNameTuple;
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

    @Query("SELECT sectionName, courseNameEnglish FROM user_progress")
    LiveData<List<SectionCourseNameTuple>> getAllEnrolledCourses();

    @Query("SELECT courseNameEnglish FROM user_progress")
    LiveData<List<String>> getAllEnrolledCoursesOnly();

    @Query("SELECT videoWatched FROM user_progress WHERE courseNameEnglish = :courseName")
    int getCurrentVideoWatchCountForCurrentCourse(String courseName);

    @Query("SELECT currentVideoPosition FROM user_progress WHERE id = 1")
    LiveData<Long> getCurrentVideoPosition();

    @Query("UPDATE user_progress  SET currentVideoPosition = :pos WHERE id = 1")
    void setCurrentVideoPosition(long pos);

    @Query("SELECT totalVideos FROM user_progress WHERE courseNameEnglish = :courseName")
    int getTotalVideoCountForCurrentCourse(String courseName);

    @Query("SELECT sectionName, sectionNameBangla, courseEnrolled, isFinished, courseThumbnailURL, totalVideos,videoWatched FROM user_progress")
    LiveData<List<SectionCourseTuple>> loadFullName();

    @Query("UPDATE user_progress SET totalVideos = :count WHERE courseNameEnglish = :courseName")
    void upDateTotalVideo(String courseName, int count);

    @Query("UPDATE user_progress SET videoWatched = videoWatched + 1 WHERE courseNameEnglish = :courseName")
    void upDateVideoWatched(String courseName);
}
