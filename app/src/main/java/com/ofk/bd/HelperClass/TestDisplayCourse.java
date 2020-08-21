package com.ofk.bd.HelperClass;

public class TestDisplayCourse {

    String courseId;
    String courseTitle;
    String courseTitleEnglish;
    String thumbnailURL;

    public TestDisplayCourse() {
    }

    public TestDisplayCourse(String courseId, String courseTitle, String courseTitleEnglish, String thumbnailURL) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.courseTitleEnglish = courseTitleEnglish;
        this.thumbnailURL = thumbnailURL;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseTitleEnglish() {
        return courseTitleEnglish;
    }

    public void setCourseTitleEnglish(String courseTitleEnglish) {
        this.courseTitleEnglish = courseTitleEnglish;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }
}
