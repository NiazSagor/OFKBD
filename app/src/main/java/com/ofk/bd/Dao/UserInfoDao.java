package com.ofk.bd.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ofk.bd.Model.UserInfo;

@Dao
public interface UserInfoDao {

    /*********************User info table**************************/
    @Insert
    void insert(UserInfo userInfo);

    @Update
    void update(UserInfo userInfo);

    // update user
    @Query("UPDATE user_info SET userName = :userName, userEmail = :userEmail, userPhoneNumber = :userPhoneNumber WHERE id = 1")
    void updateUser(String userName, String userEmail, String userPhoneNumber);

    // update user video total count
    @Query("UPDATE user_info SET videoCompleted = videoCompleted + 1 WHERE id = 1")
    void updateUserVideoTotal();

    // update user course total count
    @Query("UPDATE user_info SET courseCompleted = courseCompleted + 1 WHERE id = 1")
    void updateUserCourseTotal();

    // update user quiz total count
    @Query("UPDATE user_info SET quizCompleted = quizCompleted + 1 WHERE id = 1")
    void updateUserQuizTotal();

    // update user email
    @Query("UPDATE user_info SET userEmail = :email WHERE id = 1")
    void updateUserEmail(String email);

    // update user class
    @Query("UPDATE user_info SET userClass = :userClass WHERE id = 1")
    void updateUserClass(String userClass);

    // update user school
    @Query("UPDATE user_info SET userSchool = :school WHERE id = 1")
    void updateUserSchool(String school);

    // update user dob
    @Query("UPDATE user_info SET userDOB = :dob WHERE id = 1")
    void updateUserDOB(String dob);

    // update user gender
    @Query("UPDATE user_info SET userGender = :gender WHERE id = 1")
    void updateUserGender(String gender);

    @Query("SELECT courseCompleted FROM user_info WHERE id = 1")
    LiveData<Integer> getCurrentCompletedCourseCount();

    @Query("SELECT videoCompleted FROM user_info WHERE id = 1")
    int getCurrentCompletedVideoCount();

    @Query("SELECT quizCompleted FROM user_info WHERE id = 1")
    int getCurrentCompletedQuizCount();

    @Query("SELECT * FROM user_info WHERE id = 1")
    LiveData<UserInfo> getUser();
}
