package com.ofk.bd.Interface;

import com.ofk.bd.extractorlibrary.model.YTMedia;

public interface YtExtractorCallback {
    void onVideoThumbnail(String url);

    void onVideoStream(YTMedia ytMedia);
}
