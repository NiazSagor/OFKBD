package com.ofk.bd.HelperClass;

import androidx.room.ColumnInfo;

public class SectionCourseTuple {

    @ColumnInfo(name = "sectionName")
    String sectionName;

    @ColumnInfo(name = "sectionNameBangla")
    String sectionNameBangla;

    @ColumnInfo(name = "courseEnrolled")
    String courseEnrolled;

    @ColumnInfo(name = "courseNameEnglish")
    String courseNameEnglish;

    @ColumnInfo(name = "isFinished")
    boolean isFinished;

    @ColumnInfo(name = "courseThumbnailURL")
    String courseThumbnailURL;

    @ColumnInfo(name = "totalVideos")
    int totalVideos;

    @ColumnInfo(name = "videoWatched")
    int videoWatched;


    public SectionCourseTuple(String sectionName, String sectionNameBangla, String courseEnrolled, String courseNameEnglish, boolean isFinished, String courseThumbnailURL, int totalVideos, int videoWatched) {
        this.sectionName = sectionName;
        this.sectionNameBangla = sectionNameBangla;
        this.courseEnrolled = courseEnrolled;
        this.courseNameEnglish = courseNameEnglish;
        this.isFinished = isFinished;
        this.courseThumbnailURL = courseThumbnailURL;
        this.totalVideos = totalVideos;
        this.videoWatched = videoWatched;
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
