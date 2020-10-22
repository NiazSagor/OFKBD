package com.ofk.bd.HelperClass;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_progress")//Table name
public class UserProgressClass {
    @PrimaryKey(autoGenerate = true)//id is primary key
    private int id;

    private String sectionName;
    private String sectionNameBangla;
    private String courseEnrolled;
    private String courseNameEnglish;
    private boolean isFinished;
    private String courseThumbnailURL;
    private int totalVideos;
    private int videoWatched;

    public UserProgressClass(String sectionName, String sectionNameBangla, String courseEnrolled, String courseNameEnglish, boolean isFinished, String courseThumbnailURL, int totalVideos, int videoWatched) {
        this.sectionName = sectionName;
        this.sectionNameBangla = sectionNameBangla;
        this.courseEnrolled = courseEnrolled;
        this.courseNameEnglish = courseNameEnglish;
        this.isFinished = isFinished;
        this.courseThumbnailURL = courseThumbnailURL;
        this.totalVideos = totalVideos;
        this.videoWatched = videoWatched;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getSectionNameBangla() {
        return sectionNameBangla;
    }

    public void setSectionNameBangla(String sectionNameBangla) {
        this.sectionNameBangla = sectionNameBangla;
    }

    public String getCourseEnrolled() {
        return courseEnrolled;
    }

    public void setCourseEnrolled(String courseEnrolled) {
        this.courseEnrolled = courseEnrolled;
    }

    public String getCourseNameEnglish() {
        return courseNameEnglish;
    }

    public void setCourseNameEnglish(String courseNameEnglish) {
        this.courseNameEnglish = courseNameEnglish;
    }

    public String getCourseThumbnailURL() {
        return courseThumbnailURL;
    }

    public void setCourseThumbnailURL(String courseThumbnailURL) {
        this.courseThumbnailURL = courseThumbnailURL;
    }

    public int getTotalVideos() {
        return totalVideos;
    }

    public void setTotalVideos(int totalVideos) {
        this.totalVideos = totalVideos;
    }

    public int getVideoWatched() {
        return videoWatched;
    }

    public void setVideoWatched(int videoWatched) {
        this.videoWatched = videoWatched;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }
}
