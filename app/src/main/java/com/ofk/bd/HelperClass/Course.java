package com.ofk.bd.HelperClass;

public class Course {
    String courseTitle;
    String courseSubtitle;
    //String courseImage;

    public Course() {
    }

    public Course(String courseTitle, String courseSubtitle) {
        this.courseTitle = courseTitle;
        this.courseSubtitle = courseSubtitle;
    }

    public Course(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseSubtitle() {
        return courseSubtitle;
    }

    public void setCourseSubtitle(String courseSubtitle) {
        this.courseSubtitle = courseSubtitle;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }
/*
    public String getCourseImage() {
        return courseImage;
    }

    public void setCourseImage(String courseImage) {
        this.courseImage = courseImage;
    }

 */
}
