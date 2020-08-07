package com.ofk.bd.HelperClass;

public class EnrolledCourse {
    String courseNameEnglish;
    int totalVideos;
    int videoCompleted;

    public EnrolledCourse() {
    }

    public EnrolledCourse(String courseNameEnglish, int totalVideos, int videoCompleted) {
        this.courseNameEnglish = courseNameEnglish;
        this.totalVideos = totalVideos;
        this.videoCompleted = videoCompleted;
    }

    public String getCourseNameEnglish() {
        return courseNameEnglish;
    }

    public void setCourseNameEnglish(String courseNameEnglish) {
        this.courseNameEnglish = courseNameEnglish;
    }

    public int getTotalVideos() {
        return totalVideos;
    }

    public void setTotalVideos(int totalVideos) {
        this.totalVideos = totalVideos;
    }

    public int getVideoCompleted() {
        return videoCompleted;
    }

    public void setVideoCompleted(int videoCompleted) {
        this.videoCompleted = videoCompleted;
    }
}
