package com.ofk.bd.extractorlibrary.model;

public class StreamingData {
    private String hlsManifestUrl;
    private long expiresInSeconds;
    private YTMedia[] formats;
    private YTMedia[] adaptiveFormats;

    public YTMedia[] getFormats() {
        return formats;
    }

    public void setFormats(YTMedia[] formats) {
        this.formats = formats;
    }

    public YTMedia[] getAdaptiveFormats() {
        return adaptiveFormats;
    }

    public void setAdaptiveFormats(YTMedia[] adaptiveFormats) {
        this.adaptiveFormats = adaptiveFormats;
    }

    public long getExpiresInSeconds() {
        return expiresInSeconds;
    }

    public void setExpiresInSeconds(long expiresInSeconds) {
        this.expiresInSeconds = expiresInSeconds;
    }

    public String getHlsManifestUrl() {
        return hlsManifestUrl;
    }

    public void setHlsManifestUrl(String hlsManifestUrl) {
        this.hlsManifestUrl = hlsManifestUrl;
    }
}
