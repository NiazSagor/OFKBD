package com.ofk.bd.HelperClass;

import java.util.List;

public class SectionVideo {
    Section sectionName;
    List<Video> videos;

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
}
