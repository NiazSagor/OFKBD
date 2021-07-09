package com.ofk.bd.Model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_info")//Table name
public class UserInfo {
    @PrimaryKey(autoGenerate = true)//id is primary key
    private Integer id;

    protected String firebaseUid;

    String userName;
    String userPhoneNumber;
    String userPassword;
    String userEmail;
    String userClass;
    String userSchool;
    String userDOB;
    String userGender;

    long videoCompleted;
    long courseCompleted;
    long quizCompleted;

    @Ignore
    public UserInfo() {
    }

    public UserInfo(long courseCompleted, long quizCompleted, String userClass, String userDOB, String userEmail, String userGender, String userName, String userPassword, String userPhoneNumber, String userSchool, long videoCompleted) {
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.userClass = userClass;
        this.userSchool = userSchool;
        this.userDOB = userDOB;
        this.userGender = userGender;
        this.videoCompleted = videoCompleted;
        this.courseCompleted = courseCompleted;
        this.quizCompleted = quizCompleted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public long getVideoCompleted() {
        return videoCompleted;
    }

    public void setVideoCompleted(long videoCompleted) {
        this.videoCompleted = videoCompleted;
    }

    public long getCourseCompleted() {
        return courseCompleted;
    }

    public void setCourseCompleted(long courseCompleted) {
        this.courseCompleted = courseCompleted;
    }

    public long getQuizCompleted() {
        return quizCompleted;
    }

    public void setQuizCompleted(long quizCompleted) {
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

    public String getFirebaseUid() {
        return firebaseUid;
    }

    public void setFirebaseUid(String firebaseUid) {
        this.firebaseUid = firebaseUid;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
