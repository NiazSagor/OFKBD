package com.ofk.bd.Interface;

import com.ofk.bd.Model.Video;

import java.util.List;

public interface VideoLoadCallback {
    void onLoadCallback(List<Video> list);
}
