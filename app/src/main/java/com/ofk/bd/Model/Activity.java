package com.ofk.bd.Model;

public class Activity {

    String title;
    String url;


    public Activity() {
    }

    public Activity(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

}
