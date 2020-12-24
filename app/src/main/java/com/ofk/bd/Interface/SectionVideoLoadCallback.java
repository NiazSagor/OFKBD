package com.ofk.bd.Interface;

import com.ofk.bd.Model.SectionVideo;

import java.util.List;

public interface SectionVideoLoadCallback {
    void onSectionVideoLoadCallback(List<SectionVideo> sectionVideoList, int totalVideos);
}
