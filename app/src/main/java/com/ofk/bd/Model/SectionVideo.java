package com.ofk.bd.Model;

import java.util.List;

public class SectionVideo {
    private Section sectionName;
    private final List<Video> videos;
    private int totalVideos;

    public SectionVideo(Section sectionName, List<Video> videos) {
        this.sectionName = sectionName;
        this.videos = videos;
    }

    public Section getSectionName() {
        return sectionName;
    }

    public void setSectionName(Section sectionName) {
        this.sectionName = sectionName;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public int getTotalVideos() {
        return totalVideos;
    }

    public void setTotalVideos(int totalVideos) {
        this.totalVideos = totalVideos;
    }
}
