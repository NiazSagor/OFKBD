package com.ofk.bd.HelperClass;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_info")//Table name
public class UserInfo {
    @PrimaryKey(autoGenerate = true)//id is primary key
    private int id;

    String userName;
    String userPhoneNumber;
    String userEmail;
    String userClass;
    String userSchool;
    String userDOB;
    String userGender;

    int videoCompleted;
    int courseCompleted;
    int quizCompleted;


    public UserInfo(String userName, String userPhoneNumber, String userEmail, String userClass, String userSchool, String userDOB, String userGender, int videoCompleted, int courseCompleted, int quizCompleted) {
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
        this.userEmail = userEmail;
        this.userClass = userClass;
        this.userSchool = userSchool;
        this.userDOB = userDOB;
        this.userGender = userGender;
        this.videoCompleted = videoCompleted;
        this.courseCompleted = courseCompleted;
        this.quizCompleted = quizCompleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getVideoCompleted() {
        return videoCompleted;
    }

    public void setVideoCompleted(int videoCompleted) {
        this.videoCompleted = videoCompleted;
    }

    public int getCourseCompleted() {
        return courseCompleted;
    }

    public void setCourseCompleted(int courseCompleted) {
        this.courseCompleted = courseCompleted;
    }

    public int getQuizCompleted() {
        return quizCompleted;
    }

    public void setQuizCompleted(int quizCompleted) {
        this.quizCompleted = quizCompleted;
    }

    public String getUserClass() {
        return userClass;
    }

    public void setUserClass(String userClass) {
        this.userClass = userClass;
    }

    public String getUserSchool() {
        return userSchool;
    }

    public void setUserSchool(String userSchool) {
        this.userSchool = userSchool;
    }

    public String getUserDOB() {
        return userDOB;
    }

    public void setUserDOB(String userDOB) {
        this.userDOB = userDOB;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }
}
