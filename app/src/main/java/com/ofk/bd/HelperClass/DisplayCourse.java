package com.ofk.bd.HelperClass;

public class DisplayCourse {
    String courseTitle;
    String thumbnailURL;

    public DisplayCourse() {
    }

    public DisplayCourse(String courseTitle, String thumbnailURL) {
        this.courseTitle = courseTitle;
        this.thumbnailURL = thumbnailURL;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }
}
