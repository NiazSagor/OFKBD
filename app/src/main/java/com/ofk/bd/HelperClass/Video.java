package com.ofk.bd.HelperClass;

public class Video {
    String videoThumbNail;
    String videoTitle;
    String videoURL;

    public Video() {
    }


    public Video(String videoThumbNail, String videoTitle, String videoURL) {
        this.videoThumbNail = videoThumbNail;
        this.videoTitle = videoTitle;
        this.videoURL = videoURL;
    }

    public String getVideoThumbNail() {
        return videoThumbNail;
    }

    public void setVideoThumbNail(String videoThumbNail) {
        this.videoThumbNail = videoThumbNail;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }
}
