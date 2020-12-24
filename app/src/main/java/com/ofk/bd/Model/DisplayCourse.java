package com.ofk.bd.Model;

public class DisplayCourse {
    String courseTitle;
    String courseTitleEnglish;
    String thumbnailURL;

    public DisplayCourse() {
    }

    public DisplayCourse(String courseTitle, String courseTitleEnglish, String thumbnailURL) {
        this.courseTitle = courseTitle;
        this.courseTitleEnglish = courseTitleEnglish;
        this.thumbnailURL = thumbnailURL;
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
