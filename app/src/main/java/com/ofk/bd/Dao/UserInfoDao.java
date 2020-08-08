package com.ofk.bd.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ofk.bd.HelperClass.UserInfo;

@Dao
public interface UserInfoDao {

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

    @Query("SELECT * FROM user_info WHERE id = 1")
    LiveData<UserInfo> getUser();
}
